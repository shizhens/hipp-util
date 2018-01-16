/**
 * 
 */
package hiapp.utils.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhang
 *
 */
public class DBConnectionLogInfo {
	private int hashCode;
	private Connection connection;
	private String invokerInfo;
	private Date time;
	private String startTimeString;
	
	public DBConnectionLogInfo(Connection connection, String invokerInfo) {
		this.connection = connection;
		this.hashCode = this.connection.hashCode();
		this.invokerInfo = invokerInfo;
		this.time = new Date();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.startTimeString = df.format(this.time);
	}
	
	public String getLogContent() {
		Date now = new Date();
		return String.format("[DBC=%d; I=%s; ST=%s; L=%d; C=%d]", this.hashCode, this.invokerInfo, this.startTimeString
				, now.getTime() - this.time.getTime(), this.isClosed());
	}
	
	public int isClosed() {
		try {
			return this.connection.isClosed() ? 1 : 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
}
