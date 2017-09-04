/**
 * 
 */
package hiapp.utils.tenant;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author zhangguanghao
 *
 */
@Component("tenant")
public class TenantImpl implements Tenant {
	private String id;
	private String code;
	private String name;
	private String companyName;
	private String rootUrl;
	
	public void cloneTo(Tenant tenant) {
		try {
			BeanUtils.copyProperties(tenant, this);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the tenantId
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param tenantId the tenantId to set
	 */
	public void setId(String tenantId) {
		this.id = tenantId;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the tenantName
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param tenantName the tenantName to set
	 */
	public void setName(String tenantName) {
		this.name = tenantName;
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return the rootUrl
	 */
	public String getRootUrl() {
		return rootUrl;
	}
	/**
	 * @param rootUrl the rootUrl to set
	 */
	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}
}
