/**
 * 
 */
package qhqx.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author yan
 *
 */
public class TestDBPool {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBConnector connector = DBConnector.getInstance();
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//connector.getConnection("Oracle");
		Connection conn1 = connector.getConnection("oracle");
		/*if(conn1 == null){
			conn1 = 
		}*/
		System.out.println(conn1);
		try {
			Statement state = conn1.createStatement();
			ResultSet rs = state.executeQuery("select count(zvalue2) from gx.v_gis_info where zvalue2 is not null");
			while(rs.next()){
				System.out.println("shumu:" + rs.getDouble(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		connector.freeConnection("oracle", conn1);
	}

}
