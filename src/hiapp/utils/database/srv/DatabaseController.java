/**
 * 
 */
package hiapp.utils.database.srv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hiapp.utils.database.TenantDBConnectionPool;
import hiapp.utils.serviceresult.ServiceResult;
import hiapp.utils.serviceresult.ServiceResultCode;

/**
 * @author zhang
 *
 */
@RestController
public class DatabaseController {
	@Autowired
	private TenantDBConnectionPool dbConnectionPool;
	
	@RequestMapping(value="/srv/db/refreshExternalDBConnection.srv", method= { RequestMethod.GET, RequestMethod.POST }, produces="text/plain")
	public ServiceResult refreshExternalDBConnection() {
		ServiceResult result = new ServiceResult();
		
		if (null != this.dbConnectionPool) {
			this.dbConnectionPool.refreshExternalDBConnection();
			result.setResultCode(ServiceResultCode.SUCCESS);
		} else {
			result.setResultCode(ServiceResultCode.INVALID_DBCONNECTION);
		}
		
		return result;
	}
}
