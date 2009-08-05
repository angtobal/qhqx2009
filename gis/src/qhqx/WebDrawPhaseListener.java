/**
 * 
 */
package qhqx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import qhqx.task.PictureBuilder;
import qhqx.task.RealTimeContour;

import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.WebContextInitialize;
import com.esri.adf.web.data.WebToc;
import com.esri.adf.web.data.geometry.WebExtent;
import com.esri.adf.web.faces.event.TaskEvent;
import com.esri.adf.web.util.WebUtil;

/**
 * @author yan
 *
 */
public class WebDrawPhaseListener implements PhaseListener, WebContextInitialize {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7800778696424480298L;
	private static String PID = "pid";
	private static String BASE = "base";
	private static String INTERVAL = "interval";
	
	/**
	 * ÒªËØ£ºÓÐ¹ØÓÚäÖÈ¾ºÍÍ¼Àý
	 */
	private static String FeatureName = "feature";
	private static String Task = "task";
	private static String FILE_NAME = "filename";
	private static String PICTURE_HEAD = "describe";

	@SuppressWarnings("unchecked")
	public void afterPhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, String> paramMap = externalContext.getRequestParameterMap();
		
		WebContext webContext = WebUtil.getWebContext(facesContext.getViewRoot());
		
		if(paramMap.get(PID) == null){
			System.out.println("pid is null");
			return;
		}
		System.out.println("pid = " + paramMap.get(PID).toString());
		if(paramMap.get(BASE) == null){
			System.out.println("base is null");
			//return;
		}
		//System.out.println("base = " + paramMap.get(BASE).toString());
		if(paramMap.get(INTERVAL) == null){
			System.out.println("interval is null");
			return;
		}
		System.out.println("interval = " + paramMap.get(INTERVAL).toString());
		
		if(paramMap.get(Task).toString().equals("realtime")){
			RealTimeContour realTimeSurface = new RealTimeContour();
			realTimeSurface.setWebContext(webContext);
			realTimeSurface.setEndpoint("http://localhost:8399/arcgis/services/GIS/GPServer?");
			realTimeSurface.setMapEndpoint("http://localhost:8399/arcgis/services/GIS/MapServer");
			realTimeSurface.setLocalMapResID("ags1");
			realTimeSurface.setPid(paramMap.get(PID).toString());
			//realTimeSurface.setBase(paramMap.get(BASE).toString());
			realTimeSurface.setBase(paramMap.get(BASE).toString());
			realTimeSurface.setInterval(paramMap.get(INTERVAL).toString());
			if(paramMap.get(FeatureName) != null){
				realTimeSurface.setFeatureName(paramMap.get(FeatureName).toString());
			}
			if(paramMap.get(PICTURE_HEAD) != null){
				realTimeSurface.setPicHead(paramMap.get(PICTURE_HEAD).toString());
			}else{
				realTimeSurface.setPicHead("Í¼Æ¬ÃèÊöÐÅÏ¢...");
			}
			
			try {
				//realTimeSurface.generateContout(webContext, "servertask");
				realTimeSurface.gpResultDisplay();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} /*catch (InterruptedException e) {
				e.printStackTrace();
			} */catch (IOException e) {
				e.printStackTrace();
			}
			
		}else if(paramMap.get(Task).toString().equals("buildpic")){
			if(paramMap.get(FILE_NAME) == null){
				System.out.println("filename is null");
				return;
			}
			PictureBuilder picBuild = new PictureBuilder();
			picBuild.setWebContext(webContext);
			picBuild.setEndpoint("http://localhost:8399/arcgis/services/GIS/GPServer?");
			picBuild.setMapEndpoint("http://localhost:8399/arcgis/services/GIS/MapServer");
			picBuild.setLocalMapResID("ags1");
			picBuild.setPid(paramMap.get(PID).toString());
			picBuild.setBase(paramMap.get(BASE).toString());
			picBuild.setInterval(paramMap.get(INTERVAL).toString());
			picBuild.setFileName(paramMap.get(FILE_NAME).toString());
			if(paramMap.get(FeatureName) != null){
				picBuild.setFeatureName(paramMap.get(FeatureName).toString());
			}
			if(paramMap.get(PICTURE_HEAD) != null){
				picBuild.setPictureHead(paramMap.get(PICTURE_HEAD).toString());
			}else{
				picBuild.setPictureHead("Í¼Æ¬ÃèÊöÐÅÏ¢...");
			}
			try {
				//realTimeSurface.generateContout(webContext, "servertask");
				picBuild.gpResultDisplay();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} /*catch (InterruptedException e) {
				e.printStackTrace();
			} */catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		
		WebToc newToc = new WebToc();
		newToc.init(webContext);
		
		webContext.getWebToc().destroy();
		newToc.setExpandLevel(2);
		webContext.setWebToc(newToc);
		
		webContext.refresh();
		//facesContext.responseComplete();
	}

	@SuppressWarnings("unchecked")
	public void beforePhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		@SuppressWarnings("unused")
		Map<String, String> paramMap = externalContext.getRequestParameterMap();
	}

	public PhaseId getPhaseId() {
		return PhaseId.INVOKE_APPLICATION;
		//return PhaseId.APPLY_REQUEST_VALUES;
		//return PhaseId.ANY_PHASE;
	}

	/* (non-Javadoc)
	 * @see com.esri.adf.web.data.WebContextInitialize#destroy()
	 */
	public void destroy() {
		
	}

	/* (non-Javadoc)
	 * @see com.esri.adf.web.data.WebContextInitialize#init(com.esri.adf.web.data.WebContext)
	 */
	@SuppressWarnings("unchecked")
	public void init(WebContext webContext) {
		//wctx
		System.out.println("first time");
		if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap() != null){
			Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			if(paramMap.get(PID) == null){
				System.out.println("pid is null");
				return;
			}
			System.out.println("pid = " + paramMap.get(PID).toString());
			if(paramMap.get(BASE) == null){
				System.out.println("base is null");
				//return;
			}
			//System.out.println("base = " + paramMap.get(BASE).toString());
			if(paramMap.get(INTERVAL) == null){
				System.out.println("interval is null");
				return;
			}
			
			if(paramMap.get(Task).toString().equals("realtime")){
				RealTimeContour realTimeSurface = new RealTimeContour();
				realTimeSurface.setWebContext(webContext);
				realTimeSurface.setEndpoint("http://localhost:8399/arcgis/services/GIS/GPServer?");
				realTimeSurface.setMapEndpoint("http://localhost:8399/arcgis/services/GIS/MapServer");
				realTimeSurface.setLocalMapResID("ags1");
				realTimeSurface.setPid(paramMap.get(PID).toString());
				//realTimeSurface.setBase(paramMap.get(BASE).toString());
				realTimeSurface.setBase("0");
				realTimeSurface.setInterval(paramMap.get(INTERVAL).toString());
				if(paramMap.get(FeatureName) != null){
					realTimeSurface.setFeatureName(paramMap.get(FeatureName).toString());
				}
				if(paramMap.get(PICTURE_HEAD) != null){
					realTimeSurface.setPicHead(paramMap.get(PICTURE_HEAD).toString());
				}else{
					realTimeSurface.setPicHead("Í¼Æ¬ÃèÊöÐÅÏ¢...");
				}
				try {
					//realTimeSurface.generateContout(webContext, "servertask");
					realTimeSurface.gpResultDisplay();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}else if(paramMap.get(Task).toString().equals("buildpic")){
				if(paramMap.get(FILE_NAME) == null){
					System.out.println("filename is null");
					return;
				}
				PictureBuilder picBuild = new PictureBuilder();
				picBuild.setWebContext(webContext);
				picBuild.setEndpoint("http://localhost:8399/arcgis/services/GIS/GPServer?");
				picBuild.setMapEndpoint("http://localhost:8399/arcgis/services/GIS/MapServer");
				picBuild.setLocalMapResID("ags1");
				picBuild.setPid(paramMap.get(PID).toString());
				picBuild.setBase("0");
				picBuild.setInterval(paramMap.get(INTERVAL).toString());
				picBuild.setFileName(paramMap.get(FILE_NAME).toString());
				if(paramMap.get(FeatureName) != null){
					picBuild.setFeatureName(paramMap.get(FeatureName).toString());
				}
				if(paramMap.get(PICTURE_HEAD) != null){
					picBuild.setPictureHead(paramMap.get(PICTURE_HEAD).toString());
				}else{
					picBuild.setPictureHead("Í¼Æ¬ÃèÊöÐÅÏ¢...");
				}
				try {
					picBuild.gpResultDisplay();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
				
			
			WebToc newToc = new WebToc();
			newToc.init(webContext);
			
			webContext.getWebToc().destroy();
			newToc.setExpandLevel(2);
			webContext.setWebToc(newToc);
			
			webContext.refresh();
		}
	}

	public void concentrateQingHai(TaskEvent event){
		WebContext ctx = event.getWebContext();
		ctx.getWebMap().setCurrentExtent(new WebExtent(90,30,100,35));
	}
}
