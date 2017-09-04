/**
 * 
 */
package hiapp.utils.database;

/**
 * @author zhangguanghao
 *
 */
public enum DatabaseType {
	INVALID(-1, "Invalid"),
	ORACLE(0, "Oracle"),
	MYSQL(1, "Mysql"),
	SQLSERVER(2, "SqlServer"),;
	
	private int value;
	private String name;
	
	private DatabaseType(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return this.value;
	}
	
    public String getName() {
    	return this.name;
    }
    
    public static DatabaseType valueOf(int value) {
        for (DatabaseType code : DatabaseType.values()) {
            if (code.getValue() == value)
            	return code;
        }
        return null;
    }
}
