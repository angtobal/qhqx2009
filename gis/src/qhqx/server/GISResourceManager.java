/**
 * 
 */
package qhqx.server;

import java.util.Hashtable;

import com.esri.adf.web.ags.data.AGSLocalMapResource;

/**
 * @author yan
 *看来有点问题，mapresource的获取都是目前都是通过webContext获取。。待确认
 */
public class GISResourceManager {
	private static GISResourceManager instance;
	private static int clients;
	private Hashtable pools;
	
	private GISResourceManager(){
		
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
}
