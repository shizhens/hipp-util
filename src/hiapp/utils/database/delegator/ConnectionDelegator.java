/**
 * 
 */
package hiapp.utils.database.delegator;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

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
		// TODO Auto-generated method stub
		switch (method.getName()) {
		case "close":
			for (StatementDelegator<?> statementDelegator : this.statements) {
				if (null != statementDelegator && !statementDelegator.getProxyObject().isClosed()) {
					statementDelegator.getProxyObject().close();
				}
			}
			this.onClose();
			return super.invoke(proxy, method, args);
			
		case "createStatement":
			Statement statement = (Statement) super.invoke(proxy, method, args);
			if (null != statement) {
				StatementDelegator<Statement> statementDelegator = new StatementDelegator<Statement>(statement);
				this.statements.add(statementDelegator);
				return statementDelegator.getProxyObject();
			} else {
				return null;
			}
			
		case "prepareStatement":
			PreparedStatement preparedStatement = (PreparedStatement) super.invoke(proxy, method, args);
			if (null != preparedStatement) {
				StatementDelegator<PreparedStatement> statementDelegator = new StatementDelegator<PreparedStatement>(preparedStatement);
				this.statements.add(statementDelegator);
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
