/**
 * 
 */
package hiapp.utils.idfactory.data;

import java.sql.Connection;
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
		Connection dbConnection = null;
		IdGenerator idGenerator = null;
		try {
			dbConnection = this.getDbConnection();
			Statement statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "";
			sql = String.format("SELECT IDHEAD, IDDATE FROM HASYS_ID_IDHEAD WHERE IDHEAD='%s'", idHead);
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next()) {
				idGenerator = new IdGenerator(this, idHead);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return idGenerator;
	}
	
	public boolean pullBatchIds(IdGenerator idGenerator, int batchSize) {
		Connection dbConnection = null;
		try {
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
			Date seedDate = null;
			int pullBatchSize = batchSize;
			int idSeed = 1;
			
			dbConnection = this.getDbConnection();
			Statement statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "";
			sql = String.format("SELECT BATCHSIZE, IDDATE, IDSEED FROM HASYS_ID_IDHEAD WHERE IDHEAD='%s'", idGenerator.getIdHead());
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next()) {
				pullBatchSize = rs.getInt("BATCHSIZE");
				seedDate = rs.getDate("IDDATE");
				idSeed = rs.getInt("IDSEED");
			}
			rs.close();
			
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
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}
}
