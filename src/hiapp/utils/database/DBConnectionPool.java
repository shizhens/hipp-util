package hiapp.utils.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBConnectionPool {
	public Connection getDbConnection() throws SQLException;

	/**
	 * @return the dbConnectionUrl
	 */
	public String getDbConnectionUrl();

	/**
	 * @return the dbConnectionUser
	 */
	public String getDbConnectionUser();

	/**
	 * @return the dbConnectionPassword
	 */
	public String getDbConnectionPassword();

	/**
	 * @return the databaseType
	 */
	public DatabaseType getDatabaseType();
}
