/**
 * 
 */
package hiapp.utils.database.delegator;

/**
 * @author zhang
 *
 */
public interface ConnectionEventHandler {
	void onClose(ConnectionDelegator connectionDelegator);
}
