/**
 * 
 */
package hiapp.utils.serviceresult;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

/**
 * @author zhangguanghao
 *
 */
public class RecordsetResult extends ServiceResult {
	private int total;
	private int page;
	private int pageSize;
	private JsonArray rows = new JsonArray();
	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}
	/**
	 * @return the rows
	 */
	public JsonArray getRows() {
		return rows;
	}
	/**
	 * @param rows the rows to set
	 */
	public void setRows(ResultSet rs) {
		JsonObject element = null;
		ResultSetMetaData rsmd = null;
		String columnName, columnValue = null;
		try {
		    rsmd = rs.getMetaData();
		    while (rs.next()) {
		        element = new JsonObject();
		        for (int i = 0; i < rsmd.getColumnCount(); i++) {
		            columnName = rsmd.getColumnName(i + 1);
		            columnValue = rs.getString(columnName);
		            element.addProperty(columnName, columnValue);
		        }
		        rows.add(element);
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	}
	public void setRows(List rowlist) {
		Gson gson = new Gson();
		rows = gson.toJsonTree(rowlist, new TypeToken<List>() {}.getType()).getAsJsonArray();
	}
	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
