/**
 * 
 */
package hiapp.utils.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * @author zhangguanghao
 *
 */
public class NormalDBConnectionPool implements DBConnectionPool {
	private BasicDataSource dataSource;
	private String driverClassName;
	private String dbConnectionUrl;
	private String dbConnectionUser;
	private String dbConnectionPassword;
	private DatabaseType databaseType;

	public NormalDBConnectionPool(String dbConnectionUrl, String dbConnectionUser, String dbConnectionPassword) {
		setDbConnectionUrl(dbConnectionUrl);
		setDbConnectionUser(dbConnectionUser);
		setDbConnectionPassword(dbConnectionPassword);
		
		if (dbConnectionUrl.startsWith("jdbc:oracle")) {
			setDatabaseType(DatabaseType.ORACLE);
			this.driverClassName = "oracle.jdbc.driver.OracleDriver";
		} else if (dbConnectionUrl.startsWith("jdbc:mysql")) {
			setDatabaseType(DatabaseType.MYSQL);
			this.driverClassName = "com.mysql.cj.jdbc.Driver";
		} else if (dbConnectionUrl.startsWith("jdbc:sqlserver")) {
			setDatabaseType(DatabaseType.SQLSERVER);
			this.driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		} else {
			setDatabaseType(DatabaseType.INVALID);
		}
		
		this.dataSource = new BasicDataSource();
		this.dataSource.setDriverClassName(this.driverClassName);
		this.dataSource.setUrl(dbConnectionUrl);
		this.dataSource.setUsername(dbConnectionUser);
		this.dataSource.setPassword(dbConnectionPassword);
		this.dataSource.setMaxActive(100);
		this.dataSource.setMaxIdle(10);
		this.dataSource.setPoolPreparedStatements(true);
	}
	
	@Override
	public Connection getDbConnection() throws SQLException {
		return this.dataSource.getConnection();
	}

	/**
	 * @return the dbConnectionUrl
	 */
	@Override
	public String getDbConnectionUrl() {
		return this.dbConnectionUrl;
	}

	/**
	 * @param dbConnectionUrl the dbConnectionUrl to set
	 */
	private void setDbConnectionUrl(String dbConnectionUrl) {
		this.dbConnectionUrl = dbConnectionUrl;
	}

	/**
	 * @return the dbConnectionUser
	 */
	@Override
	public String getDbConnectionUser() {
		return this.dbConnectionUser;
	}

	/**
	 * @param dbConnectionUser the dbConnectionUser to set
	 */
	private void setDbConnectionUser(String dbConnectionUser) {
		this.dbConnectionUser = dbConnectionUser;
	}

	/**
	 * @return the dbConnectionPassword
	 */
	@Override
	public String getDbConnectionPassword() {
		return this.dbConnectionPassword;
	}

	/**
	 * @param dbConnectionPassword the dbConnectionPassword to set
	 */
	private void setDbConnectionPassword(String dbConnectionPassword) {
		this.dbConnectionPassword = dbConnectionPassword;
	}

	/**
	 * @return the databaseType
	 */
	@Override
	public DatabaseType getDatabaseType() {
		return this.databaseType;
	}

	/**
	 * @param databaseType the databaseType to set
	 */
	private void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}
}
