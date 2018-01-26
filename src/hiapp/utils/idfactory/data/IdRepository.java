/**
 * 
 */
package hiapp.utils.idfactory.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Repository;

import hiapp.utils.database.BaseRepository;
import hiapp.utils.idfactory.IdGenerator;

/**
 * @author zhang
 *
 */
@Repository
public class IdRepository extends BaseRepository {
	
	public IdGenerator getIdGenerator(String idHead) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		IdGenerator idGenerator = null;
		try {
			connection = this.getDbConnection();
			String sql = String.format("SELECT IDHEAD, IDDATE FROM HASYS_ID_IDHEAD WHERE IDHEAD='%s'", idHead);
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				idGenerator = new IdGenerator(this, idHead);
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
	
	public boolean pullBatchIds(IdGenerator idGenerator, int batchSize) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			Date seedDate = null;
			int pullBatchSize = batchSize;
			int idSeed = 1;
			
			connection = this.getDbConnection();
			String sql = String.format("SELECT BATCHSIZE, IDDATE, IDSEED FROM HASYS_ID_IDHEAD WHERE IDHEAD='%s'", idGenerator.getIdHead());
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				pullBatchSize = resultSet.getInt("BATCHSIZE");
				seedDate = resultSet.getDate("IDDATE");
				idSeed = resultSet.getInt("IDSEED");
			}
			this.closeResultSet(resultSet);
			resultSet = null;
			
			if (seedDate == null) {
				return false;
			}
			
			int maxId = 0;
			if (dateFmt.format(seedDate).compareTo(dateFmt.format(idGenerator.getDate())) != 0) {
				seedDate = idGenerator.getDate();
				idSeed = 1;
			}
			
			if (batchSize > 0) {
				pullBatchSize = batchSize;
			}
			maxId = idSeed + pullBatchSize;
			
			sql = String.format("UPDATE HASYS_ID_IDHEAD SET IDDATE=TO_DATE('%s','YYYY-MM-DD'), IDSEED=%d WHERE IDHEAD='%s'"
					, dateFmt.format(seedDate), maxId, idGenerator.getIdHead());
			statement.execute(sql);
			idGenerator.setCurrentId(idSeed);
			idGenerator.setMaxId(maxId - 1);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.closeResultSet(resultSet);
			this.closeStatement(statement);
			this.closeConnection(connection);
		}
		
		return false;
	}
}
