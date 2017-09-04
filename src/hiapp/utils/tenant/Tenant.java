/**
 * 
 */
package hiapp.utils.tenant;

/**
 * @author zhang
 *
 */
public interface Tenant {
	void cloneTo(Tenant tenant);
	
	/**
	 * @return the tenantId
	 */
	public String getId();
	
	/**
	 * @param tenantId the tenantId to set
	 */
	public void setId(String tenantId);
	
	/**
	 * @return the code
	 */
	public String getCode();
	
	/**
	 * @param code the code to set
	 */
	public void setCode(String code);
	
	/**
	 * @return the tenantName
	 */
	public String getName();
	
	/**
	 * @param tenantName the tenantName to set
	 */
	public void setName(String tenantName);
	
	/**
	 * @return the companyName
	 */
	public String getCompanyName();
	
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName);
	
	/**
	 * @return the rootUrl
	 */
	public String getRootUrl();
	
	/**
	 * @param rootUrl the rootUrl to set
	 */
	public void setRootUrl(String rootUrl);
}
