/**
 * 
 */
package qhqx.server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Hashtable;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.arcgis.interop.AuthInfo;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.IServerConnection;
import com.esri.arcgis.server.IServerObjectAdmin;
import com.esri.arcgis.server.IServerObjectManager;
import com.esri.arcgis.server.ServerConnection;

/**
 * @author yan
 *看来有点问题，mapresource的获取都是目前都是通过webContext获取。。待确认
 */
public class GISResourceManager {
	private static GISResourceManager instance;
	private static int clients;
	private Hashtable pools;
	
	private ServerConnection agsConn;
	private IServerObjectAdmin somAdmin;
	private IServerObjectManager som;
	
	public static int gpFailedTimes = 0;
	private GISResourceManager(){
		try {
			agsConn = new ServerConnection();
			agsConn.connect("localhost", "localhost", "arcgismanager", "gis");
			this.somAdmin = agsConn.getServerObjectAdmin();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized GISResourceManager getInstance(){
		if(instance == null){
			instance = new GISResourceManager();
		}
		clients++;
		return instance;
	}
	
	public AGSLocalMapResource getLocalMapResource(){
		return null;
	}
	
	public void restartMapService(){
		System.out.println(AuthInfo.getThreadAuthInfo());
		if(somAdmin != null){
			try {
				somAdmin.stopConfiguration("qhbound", "MapServer");
				somAdmin.stopConfiguration("GIS", "MapServer");
				somAdmin.startConfiguration("qhbound", "MapServer");
				somAdmin.startConfiguration("GIS", "MapServer");
			} catch (AutomationException e) {
				e.printStackTrace();
				System.out.println("ags restart FAILED!!");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("ags restart FAILED!!");
			}
		}
	}
	
	public void restartGPService(){
		if(somAdmin != null){
			try {
				somAdmin.stopConfiguration("GIS", "GPServer");
				somAdmin.startConfiguration("GIS", "GPServer");
				gpFailedTimes = 0;
			} catch (AutomationException e) {
				e.printStackTrace();
				System.out.println("ags restart FAILED!!");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("ags restart FAILED!!");
			}
		}
	}

	public IServerConnection getAgsConn() {
		return agsConn;
	}

	public void setAgsConn(IServerConnection agsConn) {
		this.agsConn = (ServerConnection)agsConn;
	}

	public IServerObjectAdmin getSomAdmin() {
		return somAdmin;
	}

	public void setSomAdmin(IServerObjectAdmin somAdmin) {
		this.somAdmin = somAdmin;
	}

	public IServerObjectManager getSom() {
		return som;
	}

	public void setSom(IServerObjectManager som) {
		this.som = som;
	}
}
