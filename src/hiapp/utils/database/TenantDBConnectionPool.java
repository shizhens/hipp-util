/**
 * 
 */
package hiapp.utils.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import hiapp.utils.base.HiAppContext;
import hiapp.utils.tenant.Tenant;

/**
 * @author zhangguanghao
 *
 */
@Service("tenantDBConnectionPool")
@Qualifier("tenantDBConnectionPool")
public class TenantDBConnectionPool extends Thread implements DBConnectionPool {
	private Tenant tenant = null;
	
	private HiAppContext appContext = null;
	private BasicDataSource dataSource;
	private String driverClassName;
	private String dbConnectionUrl;
	private String dbConnectionUser;
	private String dbConnectionPassword;
	private DatabaseType databaseType;
	
	@Autowired
	private Logger logger;
	private List<DBConnectionLogInfo> connectInfoList = Collections.synchronizedList(new ArrayList<DBConnectionLogInfo>());
	
	@Autowired
	private void setAppContext(HiAppContext appContext) {
		// TODO Auto-generated method stub
		this.appContext = appContext;
		this.startup();
	}
	
	@Autowired
	private void setTenant(Tenant tenant) {
		this.tenant = tenant;
		this.startup();
	}
	
	private void startup() {
		if (null == this.appContext || null == this.tenant) {
			return;
		}
		
		String dbConnectionUrl = this.appContext.getServletContext().getInitParameter("dbConnectionUrl");
		String dbConnectionUser = this.appContext.getServletContext().getInitParameter("dbConnectionUser");
		String dbConnectionPassword = this.appContext.getServletContext().getInitParameter("dbConnectionPassword");
    	
    	String webRoot = this.appContext.getServletContext().getContextPath();
    	System.out.println(webRoot);
    	
    	String tenantId = webRoot.substring(webRoot.lastIndexOf('/') + 1);
    	System.out.println(tenantId);
    	
		Connection connection = null;
		try{
			NormalDBConnectionPool sysDbConnectionConfig = 
					new NormalDBConnectionPool(dbConnectionUrl, dbConnectionUser, dbConnectionPassword);
			connection = sysDbConnectionConfig.getDbConnection();
			Statement statement = connection.createStatement();
			String sql = "";
			sql = String.format("SELECT TENANTID, CODE, NAME, COMPANYNAME, DBURL, DBUSER, DBPWD "
					+ "FROM TENANTBASEINFO WHERE TENANTID='%s'", tenantId);
		
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next()) {
				this.tenant.setId(rs.getString("TENANTID"));
				this.tenant.setCode(rs.getString("CODE"));				
				this.tenant.setName(rs.getString("NAME"));
				this.tenant.setCompanyName(rs.getString("COMPANYNAME"));
				this.tenant.setRootUrl(webRoot);
				this.initialize(rs.getString("DBURL"), rs.getString("DBUSER"), rs.getString("DBPWD"));
			}
			rs = null;
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (null != connection) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        }
		this.start();
	}
	
	protected void initialize(String dbConnectionUrl, String dbConnectionUser, String dbConnectionPassword) {
		if (null != this.dataSource) {
			return;
		}
		
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
	
	public void destory() {
		try {
			this.dataSource.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the dbConnectionUrl
	 */
	public String getDUrl() {
		return this.dbConnectionUrl;
	}

	/**
	 * @return the dbConnectionUser
	 */
	public String getUser() {
		return this.dbConnectionUser;
	}

	/**
	 * @return the dbConnectionPassword
	 */
	public String getPassword() {
		return this.dbConnectionPassword;
	}
	
	/**
	 * @return the databaseType
	 */
	public DatabaseType getDbType() {
		return this.databaseType;
	}
	
	@Override
	public Connection getDbConnection() throws SQLException {
		Connection connection = this.dataSource.getConnection();
		
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String invokerInfo = String.format("%s.%s", stackTraceElements[3].getClassName(), stackTraceElements[3].getMethodName());
		synchronized(this.connectInfoList) {
			this.connectInfoList.add(new DBConnectionLogInfo(connection, invokerInfo));
		}
		return connection;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Thread.sleep(5 * 60 * 1000);
				synchronized(this.connectInfoList) {
					logger.info("==================================================DBConnectionInfo=======================================");
					for (int index = this.connectInfoList.size() - 1; index > 0; --index) {
						DBConnectionLogInfo logInfo = this.connectInfoList.get(index);
						int closeFlag = logInfo.isClosed();
						logger.info(logInfo.getLogContent());
						if (closeFlag == 1) {
							this.connectInfoList.remove(index);
						}
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
