/**
 * 
 */
package hiapp.utils.idfactory.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hiapp.utils.database.BaseRepository;
import hiapp.utils.idfactory.IdGenerator;
import hiapp.utils.idfactory.IdHeadData;
import redis.clients.jedis.JedisPool;

/**
 * @author zhang
 *
 */
@Repository
public class IdRepository extends BaseRepository {
	@Autowired
	private JedisPool jedisPool;

	public IdGenerator getIdGenerator(String idHead) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		IdGenerator idGenerator = null;
		try {
			connection = this.getDbConnection();
			String sql = "SELECT BATCHSIZE FROM HASYS_ID_IDHEAD WHERE IDHEAD=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, idHead);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				idGenerator = new IdGenerator(this, idHead, resultSet.getInt("BATCHSIZE"), jedisPool);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.closeResultSet(resultSet);
			this.closeStatement(statement);
			this.closeConnection(connection);
		}
		
		return idGenerator;
	}
	
	public IdHeadData getIdHeadData(String idHead) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		IdHeadData idHeadData = null;
		try {			
			connection = this.getDbConnection();
			String sql = "SELECT BATCHSIZE, IDDATE, IDSEED FROM HASYS_ID_IDHEAD WHERE IDHEAD=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, idHead);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				idHeadData = new IdHeadData();
				idHeadData.setIdHead(idHead);
				idHeadData.setBatchSize(resultSet.getInt("BATCHSIZE"));
				idHeadData.setSeedDate(resultSet.getDate("IDDATE"));
				idHeadData.setSeed(resultSet.getInt("IDSEED"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			idHeadData = null;
			e.printStackTrace();
		} finally {
			this.closeResultSet(resultSet);
			this.closeStatement(statement);
			this.closeConnection(connection);
		}
		
		return idHeadData;
	}
	
	public boolean updateBatchIds(IdHeadData idHeadData) {
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
		Connection connection = null;
		PreparedStatement statement = null;
		try {			
			connection = this.getDbConnection();			
			String sql = "UPDATE HASYS_ID_IDHEAD SET IDDATE=TO_DATE(?,'YYYY-MM-DD'), IDSEED=? WHERE IDHEAD=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, dateFmt.format(idHeadData.getSeedDate()));
			statement.setInt(2, idHeadData.getSeed());
			statement.setString(3, idHeadData.getIdHead());
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.closeStatement(statement);
			this.closeConnection(connection);
		}
		
		return false;
	}
}
