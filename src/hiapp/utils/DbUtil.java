package hiapp.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbUtil {
	public static void DbCloseQuery(ResultSet rs,Statement stmt){
		if(rs!=null){
			try {
				rs.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			rs = null;
		}
		
		if(stmt!=null){
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
		}
		
	}
	public static void DbCloseExecute(Statement stmt){
		if(stmt!=null){
			try {
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
		}
		
	}
	public static void DbCloseConnection(Connection dbConnection){
		if(dbConnection!=null){
			try {
				dbConnection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
