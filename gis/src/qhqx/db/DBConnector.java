/**
 * 
 */
package qhqx.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author yan
 *
 */
public class DBConnector {
	
	private static DBConnector instance;
	private static int clients;
	private Vector drivers = new Vector();
	private Hashtable pools = new Hashtable();
	private Properties dbProps;
	private PrintWriter log;
	
	private DBConnector(){
		this.init();
	}
	
	public static synchronized DBConnector getInstance(){
		if(instance == null){
			instance = new DBConnector();
		}
		clients++;
		return instance;
	}
	
	public Connection getConnection(String name){
		DBPool dbPool = (DBPool) pools.get(name);
		if(dbPool != null){
			return dbPool.getConnection();
		}
		System.out.println("dbpool is null"+ new Date());
		return null;
	}
	
	public Connection getConnection(String name, long time){
		DBPool dbPool = (DBPool) pools.get(name);
		if(dbPool != null){
			return dbPool.getConnection(time);
		}
		return null;
	}
	
	public void freeConnection(String name, Connection conn){
		DBPool dbPool = (DBPool) pools.get(name);
		if(dbPool != null){
			dbPool.freeConnection(conn);
		}
	}
	
	public synchronized void release(){
		if(--clients != 0){
			return;
		}
		
		Enumeration allPools = pools.elements();
		while(allPools.hasMoreElements()){
			DBPool dbPool = (DBPool) allPools.nextElement();
			dbPool.release();
		}
		
		Enumeration allDrivers = drivers.elements();
		while(allDrivers.hasMoreElements()){
			Driver driver = (Driver) allDrivers.nextElement();
			try{
				DriverManager.deregisterDriver(driver);//撤销驱动程序的注册
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	private void init() {
		InputStream fileInputStream = null;
		try{
			fileInputStream = new FileInputStream(System.getProperty("user.dir") + java.io.File.separator + "db.properties");
			//fileInputStream = new FileInputStream(System.getProperty("user.dir") + java.io.File.separator + "src" + java.io.File.separator + "db.properties");
			//fileInputStream = new FileInputStream("d:\\workspace\\gis\\src\\db.properties");
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		try{
			dbProps = new Properties();
			dbProps.load(fileInputStream);
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		String logFile = dbProps.getProperty("logfile", "DBConnector.log");
		try{
			log = new PrintWriter(new FileWriter(logFile, true), true);
		}catch(IOException e){
			System.err.println("");
			log = new PrintWriter(System.err);
		}
		
		//加载驱动
		loadDriver(dbProps);
		//创建连接池
		createPools(dbProps);
	}
	
	@SuppressWarnings("unchecked")
	private void loadDriver(Properties props){
		String driverClasses = props.getProperty("drivers");
		StringTokenizer strTokenizer = new StringTokenizer(driverClasses);
		while(strTokenizer.hasMoreElements()){
			String driverClassName = strTokenizer.nextToken().trim();
			try{
				System.out.println(driverClassName);
				Driver driver = (Driver) Class.forName(driverClassName).newInstance();
				drivers.addElement(driver);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void createPools(Properties props){
		Enumeration propNames = props.propertyNames();
		while(propNames.hasMoreElements()){
			String name = (String) propNames.nextElement();
			if(name.endsWith(".url")){
				String poolName = name.substring(0, name.lastIndexOf("."));
				String url = props.getProperty(poolName + ".url");
				if(url == null){
					continue;
				}
				String user = props.getProperty(poolName + ".user");
				String password = props.getProperty(poolName + ".password");
				String maxconn = props.getProperty(poolName + ".maxconn", "0");
				
				int max;
				try{
					max = Integer.valueOf(maxconn).intValue();
				}catch(NumberFormatException e){
					e.printStackTrace();
					max = 0;
				}
				System.out.println(poolName + ":" + url + ":" + user + ":" +password+ ": " +maxconn);
				DBPool dbPool = new DBPool(poolName, url, user, password, max);
				pools.put(poolName, dbPool);
			}
		}
	}
}
