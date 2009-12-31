/**
 * 
 */
package qhqx.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * @author yan
 *
 */
public class AttributeOfStn {
	//private String pid;
	
	public ResultSet getStnNameByPid(String pid) throws SQLException{
		DBConnector connector = DBConnector.getInstance();
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Connection conn = connector.getConnection("oracle");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select longitude,latitude,stn_name from v_gis_info where pid='" + pid.toString() + "'");
			
		return rs;	
	}
}
