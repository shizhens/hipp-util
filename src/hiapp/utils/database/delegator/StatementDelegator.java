/**
 * 
 */
package hiapp.utils.database.delegator;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author zhang
 *
 */
public class StatementDelegator<T extends Statement> extends Delegator<T> {
	protected Set<ResultSet> recordSets = new HashSet<ResultSet>();

	public StatementDelegator(T realObject) {
		super(realObject, realObject.getClass().getInterfaces() );
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see hiapp.utils.database.delegator.Delegator#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Logger logger = LogManager.getLogger("hiapp");
		// TODO Auto-generated method stub
		switch (method.getName()) {
		case "close":
			for (ResultSet resultSet : this.recordSets) {
				if (null != resultSet && !resultSet.isClosed()) {
					try {
						resultSet.close();
						//logger.debug("close resultSet by proxy");
					} catch (Exception ex) {
						//logger.debug(String.format("close resultSet by proxy; exception; %s", ex.getMessage()));
					}
				}
			}
			return super.invoke(proxy, method, args);
			
		case "executeQuery":
		case "getGeneratedKeys":
			ResultSet resultSet = (ResultSet) super.invoke(proxy, method, args);
			if (null != resultSet) {
				recordSets.add(resultSet);
			}
			return resultSet;
			
		default:
			return super.invoke(proxy, method, args);
		}
	}

}
