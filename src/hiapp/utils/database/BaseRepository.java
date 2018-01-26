package hiapp.utils.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseRepository {
	@Autowired
	@Qualifier("tenantDBConnectionPool")
	private DBConnectionPool dbConnectionPool;
	
	protected Connection getDbConnection() throws SQLException {
		return this.dbConnectionPool.getDbConnection();
	}
	
	protected DatabaseType getDatabaseType() {
		return this.dbConnectionPool.getDatabaseType();
	}
	
	protected void closeConnection(Connection connection){
		if(connection!=null){
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			connection = null;
		}
	}
	
	protected void closeStatement(Statement statement){	
		if(statement != null){
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			statement = null;
		}
	}
	
	protected void closeResultSet(ResultSet resultSet){
		if(resultSet!=null){
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultSet = null;
		}
	}

}
