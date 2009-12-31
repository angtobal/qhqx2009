/**
 * 
 */
package qhqx.server;

import java.util.Hashtable;

import com.esri.adf.web.ags.data.AGSLocalMapResource;

/**
 * @author yan
 *�����е����⣬mapresource�Ļ�ȡ����Ŀǰ����ͨ��webContext��ȡ������ȷ��
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
