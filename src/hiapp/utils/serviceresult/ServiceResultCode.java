/**
 * 
 */
package hiapp.utils.serviceresult;

/**
 * @author zhangguanghao
 *
 */
public enum ServiceResultCode {
	SUCCESS(0, "成功"),
	INVALID_SESSION(10001, "会话无效"),
	INVALID_TENANT(10002, "租户无效"),
	INVALID_USER(10003, "用户无效"),
	INVALID_PARAM(10004, "无效的参数"),
	EXECUTE_SQL_FAIL(10005, "执行SQL失败"),
	INVALID_AUTHORITY(10006, "没有权限"),
	FILE_EXISTS(10007, "文件已存在"),
	FILE_FAIL(10008, "操作文件失败"),
	CUSTOMER_NONE(10009, "取不到客户");
	
	private int value;
	private String name;
	
	private ServiceResultCode(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return this.value;
	}
	
    public String getName() {
    	return this.name;
    }
    
    public static ServiceResultCode valueOf(int value) {
        for (ServiceResultCode code : ServiceResultCode.values()) {
            if (code.getValue() == value)
            	return code;
        }
        return null;
    }
}
