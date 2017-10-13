/**
 * 
 */
package hiapp.utils.serviceresult;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
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
	public List<Map<String, String>> getRows() {
		return rows;
	}
	/**
	 * @param rows the rows to set
	 */
	public void setRows(ResultSet rs) {
		ResultSetMetaData rsmd = null;
		String columnName, columnValue = null;
		try {
		    rsmd = rs.getMetaData();
		    while (rs.next()) {
		    	Map<String, String> row = new HashMap<String, String>();
		        for (int i = 0; i < rsmd.getColumnCount(); i++) {
		            columnName = rsmd.getColumnName(i + 1);
		            columnValue = rs.getString(columnName);
		            row.put(columnName, columnValue);
		        }
		        rows.add(row);
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	}
	public void setRows(List<?> rowlist) {
		Gson gson = new Gson();
		@SuppressWarnings("rawtypes")
		JsonArray arrayRows = gson.toJsonTree(rowlist, new TypeToken<List>() {}.getType()).getAsJsonArray();
		for (JsonElement el : arrayRows) {
			JsonObject obj = el.getAsJsonObject();
			Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
			if (entrySet.size() < 1) {
				continue;
			}
			
			Map<String, String> row = new HashMap<String, String>();
			for (Map.Entry<String, JsonElement> ent : entrySet) {
				try {
					row.put(ent.getKey(), ent.getValue().getAsString());
				} catch (Exception e) {
				}
			}
			this.getRows().add(row);
		}
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
