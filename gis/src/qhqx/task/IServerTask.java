/**
 * 
 */
package qhqx.task;

import java.io.IOException;

import com.esri.adf.web.data.WebContext;

/**
 * @author yan
 *
 */
public interface IServerTask {
	public void setWebContext(WebContext webContext);
	
	public void setEndpoint(String gpEndpoint);
	public void setMapEndpoint(String mapEndpoint);
	public void setLocalMapResID(String id);
	
	public void setPid(String pid);
	public void setBase(String base);
	public void setInterval(String interval);
	
	public void setFeatureName(String featureName);
	public void setPicHead(String picHead);
	
	public void gpResultDisplay() throws IOException;
}
