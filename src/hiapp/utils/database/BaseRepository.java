package hiapp.utils.database;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BaseRepository {
	@Autowired
	@Qualifier("tenantDBConnectionPool")
	private DBConnectionPool dbConnectionPool;
	
	protected Connection getDbConnection() throws SQLException {
		return this.dbConnectionPool.getDbConnection();
	}
}
