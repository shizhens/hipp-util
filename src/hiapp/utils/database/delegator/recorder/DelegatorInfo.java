/**
 * 
 */
package hiapp.utils.database.delegator.recorder;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import hiapp.utils.database.delegator.ConnectionDelegator;
import hiapp.utils.database.delegator.Delegator;
import hiapp.utils.database.delegator.StatementDelegator;

/**
 * @author zhang
 *
 */
public class DelegatorInfo {
	private int hashCode;
	private Delegator<?> delegator;
	private String delegatorClass;
	private String invokerInfo;
	private Date time;
	private String startTimeString;
	
	public DelegatorInfo(Delegator<?> delegator, String invokerInfo) {
		this.delegator = delegator;
		this.hashCode = this.delegator.getRealObject().hashCode();
		this.delegatorClass = this.delegator.getRealObject().getClass().getName();
		this.invokerInfo = invokerInfo;
		this.time = new Date();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.startTimeString = df.format(this.time);
	}
	
	public String getLogContent() {
		Date now = new Date();
		return String.format("[Dele=%s; HASH=%d; I=%s; ST=%s; L=%d; C=%d]", this.delegatorClass, this.hashCode, this.invokerInfo, this.startTimeString
				, now.getTime() - this.time.getTime(), this.isClosed());
	}
	
	public int isClosed() {
		try {
			if (this.delegator.getRealObject() instanceof Connection) {
				return ((ConnectionDelegator)this.delegator).getRealObject().isClosed() ? 1 : 0;
			} else if (this.delegator.getRealObject() instanceof Statement) {
				return ((StatementDelegator<?>)this.delegator).getRealObject().isClosed() ? 1 : 0;
			} else {
				return -2;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
}
