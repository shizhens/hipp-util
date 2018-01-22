/**
 * 
 */
package hiapp.utils.database;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hiapp.utils.database.delegator.ConnectionDelegator;

/**
 * @author zhang
 *
 */
public class DBConnectionLogInfo {
	private int hashCode;
	private ConnectionDelegator connectionDelegator;
	private String invokerInfo;
	private Date time;
	private String startTimeString;
	
	public DBConnectionLogInfo(ConnectionDelegator connectionDelegator, String invokerInfo) {
		this.connectionDelegator = connectionDelegator;
		this.hashCode = this.connectionDelegator.getProxyObject().hashCode();
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
			return this.connectionDelegator.getProxyObject().isClosed() ? 1 : 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
}
