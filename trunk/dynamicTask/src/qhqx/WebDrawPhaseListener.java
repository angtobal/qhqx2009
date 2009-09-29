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
	 * 要素：有关于渲染和图例
	 */
	private static String FeatureName = "feature";
	private static String Task = "task";
	private static String FILE_NAME = "filename";
	private static String PICTURE_HEAD = "describe";

	@SuppressWarnings("unchecked")
	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, String> paramMap = externalContext.getRequestParameterMap();
		
		WebContext webContext = WebUtil.getWebContext(facesContext.getViewRoot());
		
		if(paramMap.get(PID) == null || paramMap.get(INTERVAL) == null){
			System.out.println("pid is null");
			System.out.println("interval is null");
			return;
		}
		if(paramMap.get(BASE) == null){
			System.out.println("base is null");
			//return;
		}
		System.out.println(" pid:" + paramMap.get(PID).toString() + ";interval:" + paramMap.get(INTERVAL).toString());
		
		if(paramMap.get(Task).toString().equals("realtime")){
			RealTimeContour realTimeSurface = new RealTimeContour();
			if(paramMap.get(PICTURE_HEAD) != null){
				realTimeSurface.setPicHead(paramMap.get(PICTURE_HEAD).toString());
			}else{
				realTimeSurface.setPicHead(" ");
			}
			backgroundJob(paramMap, webContext, realTimeSurface);
			
		}else if(paramMap.get(Task).toString().equals("buildpic")){
			PictureBuilder picBuild = new PictureBuilder();
			picBackgroundJob(paramMap, webContext, picBuild);
		}
			
		
		refreshTOC(webContext);
	}

	/**
	 * @param paramMap
	 * @param webContext
	 * @param picBuild
	 */
	private void picBackgroundJob(Map<String, String> paramMap,
			WebContext webContext, PictureBuilder picBuild) {
		if(paramMap.get(FILE_NAME) == null){
			System.out.println("filename is null");
			return;
		}else{
			picBuild.setFileName(paramMap.get(FILE_NAME).toString());
		}
		if(paramMap.get(PICTURE_HEAD) != null){
			picBuild.setPictureHead(paramMap.get(PICTURE_HEAD).toString());
		}else{
			picBuild.setPictureHead("图片描述信息...");
		}
		
		picBuild.setWebContext(webContext);
		picBuild.setEndpoint("http://localhost:8399/arcgis/services/sjy/GPServer?");
		picBuild.setMapEndpoint("http://localhost:8399/arcgis/services/sjy/MapServer");
		picBuild.setLocalMapResID("ags1");
		picBuild.setPid(paramMap.get(PID).toString());
		picBuild.setBase(paramMap.get(BASE).toString());
		picBuild.setInterval(paramMap.get(INTERVAL).toString());
		
		if(paramMap.get(FeatureName) != null){
			picBuild.setFeatureName(paramMap.get(FeatureName).toString());
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

	/**
	 * @param paramMap
	 * @param webContext
	 * @param realTimeSurface
	 */
	private void backgroundJob(Map<String, String> paramMap,
			WebContext webContext, RealTimeContour realTimeSurface) {
		realTimeSurface.setWebContext(webContext);
		realTimeSurface.setEndpoint("http://localhost:8399/arcgis/services/sjy/GPServer?");
		realTimeSurface.setMapEndpoint("http://localhost:8399/arcgis/services/sjy/MapServer");
		realTimeSurface.setLocalMapResID("ags1");
		realTimeSurface.setPid(paramMap.get(PID).toString());
		realTimeSurface.setBase(paramMap.get(BASE).toString());
		realTimeSurface.setInterval(paramMap.get(INTERVAL).toString());
		if(paramMap.get(FeatureName) != null){
			realTimeSurface.setFeatureName(paramMap.get(FeatureName).toString());
		}
		
		
		try {
			realTimeSurface.gpResultDisplay();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void beforePhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		@SuppressWarnings("unused")
		Map<String, String> paramMap = externalContext.getRequestParameterMap();
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.INVOKE_APPLICATION;
		//return PhaseId.APPLY_REQUEST_VALUES;
		//return PhaseId.ANY_PHASE;
	}

	/* (non-Javadoc)
	 * @see com.esri.adf.web.data.WebContextInitialize#destroy()
	 */
	@Override
	public void destroy() {
		
	}

	/* (non-Javadoc)
	 * @see com.esri.adf.web.data.WebContextInitialize#init(com.esri.adf.web.data.WebContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init(WebContext webContext) {
		//wctx
		System.out.println("first time");
		if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap() != null){
			Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			if(paramMap.get(PID) == null || paramMap.get(INTERVAL) == null){
				System.out.println("pid is null or interval is null");
				return;
			}
			if(paramMap.get(BASE) == null){
				System.out.println("base is null");
				//return;
			}
			System.out.println(" pid:" + paramMap.get(PID).toString() + ";interval:" + paramMap.get(INTERVAL).toString());
			
			if(paramMap.get(Task).toString().equals("realtime")){
				RealTimeContour realTimeSurface = new RealTimeContour();
				backgroundJob(paramMap, webContext, realTimeSurface);
				
			}else if(paramMap.get(Task).toString().equals("buildpic")){
				PictureBuilder picBuild = new PictureBuilder();
				picBackgroundJob(paramMap, webContext, picBuild);
			}
				
			refreshTOC(webContext);
		}
	}

	/**
	 * @param webContext
	 */
	private void refreshTOC(WebContext webContext) {
		WebToc newToc = new WebToc();
		newToc.init(webContext);
		
		webContext.getWebToc().destroy();
		newToc.setExpandLevel(2);
		webContext.setWebToc(newToc);
		
		webContext.refresh();
	}

	public void concentrateQingHai(TaskEvent event){
		WebContext ctx = event.getWebContext();
		ctx.getWebMap().setCurrentExtent(new WebExtent(90,30,100,35));
	}
}
