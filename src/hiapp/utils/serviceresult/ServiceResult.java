/**
 * 
 */
package hiapp.utils.serviceresult;

import com.google.gson.Gson;

/**
 * @author zhangguanghao
 *
 */
public class ServiceResult {
	private int returnCode = -1;
	private String returnMessage = "";
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	/**
	 * @return the returnCode
	 */
	public int getReturnCode() {
		return returnCode;
	}
	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	/**
	 * @return the returnMessage
	 */
	public String getReturnMessage() {
		return returnMessage;
	}
	/**
	 * @param returnMessage the returnMessage to set
	 */
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public void setResultCode(ServiceResultCode resultCode) {
		this.returnCode = resultCode.getValue();
		this.returnMessage = resultCode.getName();
	}
}
