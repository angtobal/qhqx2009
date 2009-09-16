/**
 * 
 */
package qhqx.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author yan
 *
 */
public class MaxMinValue {
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	private String pid;
	private double max;
	private double min;
	
	private ResultSet getDate(String sql) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			//String url = "jdbc:oracle:thin:@10.181.22.41:1521:sjy";
			String url = "jdbc:oracle:thin:@220.167.220.15:1521:sjyrdb";
			
			conn = DriverManager.getConnection(url, "gx", "gx");

			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);

			//rs = stmt.executeQuery("select * from SJY_GIS_INFO where pid='" + pid.toString() + "'");

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}
	@SuppressWarnings("unused")
	private ResultSet getDate(){
		return getDate("select * from SJY_GIS_INFO where pid='" + pid.toString() + "'");
	}

	public void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}

	public void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt = null;
		}
	}

	public void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
	}
	
	public double[] selectMaxMinValue(){
		ResultSet gisData = getDate("select max(ZVALUE1),min(ZVALUE1) from GX.SJY_GIS_INFO where pid='" + pid.toString() + "'");
		try {
			System.out.println(gisData.getRow());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		try {
			while(gisData.next()){
				/*max = gisData.getDouble(1);//.getDouble("ZVALUE1");
				min = gisData.getDouble("ZVALUE1");*/
				max = gisData.getDouble(1);
				min = gisData.getDouble(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(max < min){
			double temp = max;
			max = min;
			min = temp;
		}
		double[] maxmin = {max, min};
		
		this.close(rs);
		this.close(stmt);
		this.close(conn);
		System.out.println(maxmin[0] + " " + maxmin[1]);
		return maxmin;
		
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}

}
