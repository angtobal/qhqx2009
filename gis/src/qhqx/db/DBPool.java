/**
 * 
 */
package qhqx.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author yan
 *
 */
public class DBPool {
	
	@SuppressWarnings("unused")
	private String poolName;
	private String dbConnUrl;
	private String dbUserName;
	private String dbPassWord;
	private int maxConnections;
	private int checkedOut;
	private Vector<Connection> availableConnections;
	
	public DBPool(String poolName, String dbConnUrl, String dbUserName, String dbPassWord, int maxConnections){
		this.poolName = poolName;
		this.dbConnUrl = dbConnUrl;
		this.dbUserName = dbUserName;
		this.dbPassWord = dbPassWord;
		this.maxConnections = maxConnections;
		
		this.availableConnections = new Vector<Connection>();
	}
	
	private Connection newConnection(){
		Connection conn = null;
		try{
			if(dbUserName == null || dbPassWord == null){
				conn = DriverManager.getConnection(dbConnUrl);
			}else{
				conn = DriverManager.getConnection(dbConnUrl, dbUserName, dbPassWord);
			}
		}catch(SQLException e){
			return null;
		}
		
		return conn;
	}
	
	public synchronized void freeConnection(Connection conn){
		availableConnections.addElement(conn);
		checkedOut--;
		notifyAll();
	}
	
	public synchronized void release(){
		Enumeration<Connection> allConnections = availableConnections.elements();
		while(allConnections.hasMoreElements()){
			Connection conn = allConnections.nextElement();
			try{
				conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		availableConnections.removeAllElements();
	}
	
	public synchronized Connection getConnection(){
		Connection conn = null;
		if(availableConnections != null && availableConnections.size() > 0){
			conn = availableConnections.firstElement();
			availableConnections.removeElementAt(0);
			try{
				if(conn.isClosed()){
					conn = getConnection();
				}
			}catch(SQLException e){
				conn = getConnection();
			}
		}else if(maxConnections == 0 || checkedOut < maxConnections){
			conn = newConnection();
			System.out.println(new Date());
		}
		if(conn != null){
			checkedOut++;
		}
		System.out.println("connection:" + conn);
		return conn;
	}
	
	public synchronized Connection getConnection(long timeout) {   
    	long startTime = System.currentTimeMillis();
        Connection conn = null;//定义连接标量  
        while ((conn = getConnection()) == null) {   
            try {   
                wait(timeout);   
            } catch (InterruptedException e) {  
            	e.printStackTrace();
            }   
            if ((System.currentTimeMillis() - startTime) >= timeout) {   
                // wait()返回的原因是超时
                return null;   
            }   
        }   
        return conn;   
    } 
}
