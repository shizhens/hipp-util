/**
 * 
 */
package hiapp.utils.idfactory;

import java.util.List;

/**
 * @author zhang
 *
 */
public interface IdFactory {
	String newId(String idHead);
	List<String> newIds(String idHead, int count);
}
