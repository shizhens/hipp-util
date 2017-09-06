/**
 * 
 */
package hiapp.utils.idfactory.srv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hiapp.utils.idfactory.IdFactory;
import hiapp.utils.idfactory.srv.result.IdFactoryResult;
import hiapp.utils.serviceresult.ServiceResult;
import hiapp.utils.serviceresult.ServiceResultCode;

/**
 * @author zhang
 *
 */
@RestController
public class IdFactoryController {
	@Autowired
	private IdFactory idFactory;
	
	@RequestMapping(value = "/srv/idfactory/newId.srv", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public ServiceResult newId(@RequestParam String idHead) {
		IdFactoryResult result = new IdFactoryResult();
		result.setResultCode(ServiceResultCode.SUCCESS);
		result.setId(idFactory.newId(idHead));
		return result;
	}
}
