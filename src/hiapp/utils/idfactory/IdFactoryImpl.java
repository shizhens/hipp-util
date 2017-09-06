/**
 * 
 */
package hiapp.utils.idfactory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hiapp.utils.idfactory.data.IdRepository;

/**
 * @author zhang
 *
 */
@Service("idFactory")
class IdFactoryImpl implements IdFactory {
	private Map<String, IdGenerator> idGeneratorMap = new HashMap<String, IdGenerator>();
	
	@Autowired
	private IdRepository idRepository;

	/* (non-Javadoc)
	 * @see hiapp.utils.idfactory.IdFactory#newId(java.lang.String)
	 */
	@Override
	public String newId(String idHead) {
		// TODO Auto-generated method stub
		IdGenerator idGenerator = this.getIdGenerator(idHead);
		if (null == idGenerator) {
			return null;
		}
		
		return idGenerator.newId();
	}

	private synchronized IdGenerator getIdGenerator(String idHead) {
		IdGenerator idGenerator = this.idGeneratorMap.get(idHead);
		if (idGenerator == null) {
			idGenerator = this.idRepository.getIdGenerator(idHead);
			if (idGenerator != null) {
				this.idGeneratorMap.put(idHead, idGenerator);
			}
		}
		return idGenerator;
	}
}
