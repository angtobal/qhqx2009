/**
 * 
 */
package qhqx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import qhqx.ags.GraphicLayerConfig;
import qhqx.task.IServerTask;
import qhqx.task.PictureBuilder;
import qhqx.task.RealTimeContour;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.adf.web.ags.data.AGSMapFunctionality;
import com.esri.adf.web.data.MapFunctionality;
import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.WebContextInitialize;
import com.esri.adf.web.data.WebToc;
import com.esri.adf.web.data.geometry.WebExtent;
import com.esri.adf.web.faces.event.TaskEvent;
import com.esri.adf.web.util.WebUtil;
import com.esri.arcgisws.LayerDescription;
import com.esri.arcgisws.MapLayerInfo;

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
	public void afterPhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String, String> paramMap = externalContext.getRequestParameterMap();
		
		WebContext webContext = WebUtil.getWebContext(facesContext.getViewRoot());
		
		if(paramMap.get(PID) == null || paramMap.get(BASE) == null || paramMap.get(INTERVAL) == null){
			System.out.println("pid base or interval is null");
			return;
		}
		System.out.println("pid = " + paramMap.get(PID).toString());
		
		if(paramMap.get(Task).toString().equals("realtime")){
			doRealtimeTask(webContext, paramMap);
		}else if(paramMap.get(Task).toString().equals("buildpic")){
			try {
				doPicbuildTask(webContext, paramMap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		/*try {
			@SuppressWarnings("unused")
			CustomMapLegend mapLegend = new CustomMapLegend((AGSLocalMapResource) webContext.getResourceById("ags1"));
			mapLegend.createLegend();
		} catch (AutomationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		WebToc newToc = new WebToc();
		newToc.init(webContext);
		
		webContext.getWebToc().destroy();
		newToc.setExpandLevel(3);
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
			if(paramMap.get(PID) == null || paramMap.get(BASE) == null || paramMap.get(INTERVAL) == null){
				System.out.println("pid is null");
				return;
			}
			System.out.println("pid = " + paramMap.get(PID).toString());
			
			if(paramMap.get(Task).toString().equals("realtime")){
				doRealtimeTask(webContext, paramMap);
				
			}else if(paramMap.get(Task).toString().equals("buildpic")){
				try {
					doPicbuildTask(webContext, paramMap);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
				
			
			WebToc newToc = new WebToc();
			newToc.init(webContext);
			
			webContext.getWebToc().destroy();
			//newToc.setExpandLevel(3);
			webContext.setWebToc(newToc);
			
			webContext.refresh();
		}
	}

	/**
	 * @param webContext
	 * @param paramMap
	 * @throws IOException 
	 */
	public void doPicbuildTask(WebContext webContext,
			Map<String, String> paramMap) throws IOException {
		if(paramMap.get(FILE_NAME) == null){
			System.out.println("filename is null");
			return;
		}
		
		GraphicLayerConfig glf = new GraphicLayerConfig(webContext);
		try {
			glf.addGraphicLayerByPid(paramMap.get(PID).toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		IServerTask task = new PictureBuilder(webContext);
		task.setWebContext(webContext);
		task.setEndpoint("http://localhost:8399/arcgis/services/GIS/GPServer?");
		task.setMapEndpoint("http://localhost:8399/arcgis/services/GIS/MapServer");
		task.setLocalMapResID("ags1");
		task.setPid(paramMap.get(PID).toString());
		task.setBase(paramMap.get(BASE).toString());
		task.setInterval(paramMap.get(INTERVAL).toString());
		if(paramMap.get(FeatureName) != null){
			task.setFeatureName(paramMap.get(FeatureName).toString());
		}
		if(paramMap.get(PICTURE_HEAD) != null){
			task.setPicHead(paramMap.get(PICTURE_HEAD).toString());
		}else{
			task.setPicHead("图片描述信息...");
		}
		if (task instanceof PictureBuilder) {
			PictureBuilder picBuild = (PictureBuilder) task;
			picBuild.setFileName(paramMap.get(FILE_NAME).toString());
		}	
		
		task.gpResultDisplay();
	}

	/**
	 * @param webContext
	 * @param paramMap
	 */
	public void doRealtimeTask(WebContext webContext,
			Map<String, String> paramMap) {
		
		GraphicLayerConfig glf = new GraphicLayerConfig(webContext);
		try {
			glf.addGraphicLayerByPid(paramMap.get(PID).toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		IServerTask task = new RealTimeContour(webContext);
		task.setEndpoint("http://localhost:8399/arcgis/services/GIS/GPServer?");
		task.setMapEndpoint("http://localhost:8399/arcgis/services/GIS/MapServer");
		task.setLocalMapResID("ags1");
		task.setPid(paramMap.get(PID).toString());
		task.setBase(paramMap.get(BASE).toString());
		task.setInterval(paramMap.get(INTERVAL).toString());
		if(paramMap.get(FeatureName) != null){
			task.setFeatureName(paramMap.get(FeatureName).toString());
		}
		if(paramMap.get(PICTURE_HEAD) != null){
			task.setPicHead(paramMap.get(PICTURE_HEAD).toString());
		}else{
			task.setPicHead(" ");
		}
		
		try {
			task.gpResultDisplay();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void concentrateQingHai(TaskEvent event){
		WebContext ctx = event.getWebContext();
		ctx.getWebMap().setCurrentExtent(new WebExtent(90,30,100,35));
	}
}
