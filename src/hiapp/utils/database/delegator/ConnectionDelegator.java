/**
 * 
 */
package hiapp.utils.database.delegator;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hiapp.utils.database.delegator.recorder.DBDelegatorRecorder;

/**
 * @author zhang
 *
 */
public class ConnectionDelegator extends Delegator<Connection> {
	protected ConnectionEventHandler eventHandler;
	protected Set<StatementDelegator<?>> statements = new HashSet<StatementDelegator<?>>();

	public ConnectionDelegator(Connection realObject) {
		super(realObject, new Class[] { Connection.class });
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the eventHandler
	 */
	public ConnectionEventHandler getEventHandler() {
		return eventHandler;
	}

	/**
	 * @param eventHandler the eventHandler to set
	 */
	public void setEventHandler(ConnectionEventHandler eventHandler) {
		this.eventHandler = eventHandler;
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
			for (StatementDelegator<?> statementDelegator : this.statements) {
				if (null != statementDelegator && !statementDelegator.getProxyObject().isClosed()) {
					int hashCode = statementDelegator.getRealObject().hashCode();
					try {
						statementDelegator.getProxyObject().close();
						logger.debug(String.format("close statement by proxy; HASH=%d; %s;", hashCode, statementDelegator.getRealObject().getClass().toString()));
					} catch (Exception ex) {
						logger.debug(String.format("close statement by proxy; exception; HASH=%d; %s; %s", hashCode, statementDelegator.getClass().toString(), ex.getMessage()));
					}
				}
			}
			
			try {
				this.onClose();
			} catch (Exception ex) {
				logger.debug(String.format("close statement by proxy; exception; %s", ex.getMessage()));
			}
			return super.invoke(proxy, method, args);
			
		case "createStatement":
			Statement statement = (Statement) super.invoke(proxy, method, args);
			if (null != statement) {
				StatementDelegator<Statement> statementDelegator = new StatementDelegator<Statement>(statement);
				this.statements.add(statementDelegator);
				DBDelegatorRecorder.addDelegator(statementDelegator);
				return statementDelegator.getProxyObject();
			} else {
				return null;
			}
			
		case "prepareStatement":
			PreparedStatement preparedStatement = (PreparedStatement) super.invoke(proxy, method, args);
			if (null != preparedStatement) {
				StatementDelegator<PreparedStatement> statementDelegator = new StatementDelegator<PreparedStatement>(preparedStatement);
				this.statements.add(statementDelegator);
				DBDelegatorRecorder.addDelegator(statementDelegator);
				return statementDelegator.getProxyObject();
			} else {
				return null;
			}
			
		case "prepareCall":
			CallableStatement callableStatement = (CallableStatement) super.invoke(proxy, method, args);
			if (null != callableStatement) {
				StatementDelegator<CallableStatement> statementDelegator = new StatementDelegator<CallableStatement>(callableStatement);
				this.statements.add(statementDelegator);
				DBDelegatorRecorder.addDelegator(statementDelegator);
				return statementDelegator.getProxyObject();
			} else {
				return null;
			}
			
		default:
			return super.invoke(proxy, method, args);
		}
	}
	
	protected void onClose() {
		if (null != this.eventHandler) {
			this.eventHandler.onClose(this);
		}
	}
}
