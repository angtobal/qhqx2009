/**
 * 
 */
package qhqx.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import qhqx.ags.AgsObjAccess;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.adf.web.ags.data.AGSMapFunctionality;
import com.esri.adf.web.ags.data.AGSOverviewFunctionality;
import com.esri.adf.web.data.GraphicElement;
import com.esri.adf.web.data.MapFunctionality;
import com.esri.adf.web.data.OverviewFunctionality;
import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.WebOverview;
import com.esri.adf.web.data.geometry.WebPoint;
import com.esri.adf.web.data.symbol.WebPictureMarkerSymbol;
import com.esri.adf.web.data.symbol.WebTrueTypeMarkerSymbol;
import com.esri.arcgis.carto.IRasterLayer;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgisws.EsriJobStatus;
import com.esri.arcgisws.GPDouble;
import com.esri.arcgisws.GPResult;
import com.esri.arcgisws.GPResultOptions;
import com.esri.arcgisws.GPServerBindingStub;
import com.esri.arcgisws.GPString;
import com.esri.arcgisws.GPToolInfo;
import com.esri.arcgisws.GPValue;
import com.esri.arcgisws.LayerDescription;
import com.esri.arcgisws.MapDescription;
import com.esri.arcgisws.MapLayerInfo;
import com.esri.arcgisws.MapServerInfo;

/**
 * @author yan
 *
 */
public class RealTimeContour {
	private String gpEndpoint = null;
	private String mapEndpoint = null;
	private WebContext webContext = null;
	private GPToolInfo toolInfo = null;
	private GPResult result = null;
	private GPResultOptions resultOpt = null;
	private String pid = null;
	private String base = null;
	private String interval = null;
	private String JobID = null;
	private String localMapResID = null;
	
	private String featureName = null;
	private String picHead = null;
	
	
	@SuppressWarnings("static-access")
	public void generateContout(WebContext webContext, String modelName) throws MalformedURLException, RemoteException, InterruptedException{
		GPString queryString = new GPString();
		queryString.setValue("PID=\'" + pid + "\'");
		
		GPDouble baseContour = new GPDouble();
		baseContour.setValue(Double.parseDouble(base));
		
		GPDouble contourInterval = new GPDouble();
		contourInterval.setValue(Double.parseDouble(interval));
		
		GPValue[] gpValues = new GPValue[3];
		gpValues[0] = contourInterval;
		gpValues[1] = baseContour;
		gpValues[2] = queryString;
		
		
		
		GPServerBindingStub gpServer = new GPServerBindingStub(new java.net.URL(gpEndpoint), null);
		toolInfo = gpServer.getToolInfo(modelName);
		resultOpt = new GPResultOptions();
		resultOpt.setDensifyFeatures(true);
		
		JobID = gpServer.submitJob(toolInfo.getName(), gpValues, resultOpt, null);
		System.out.println(JobID);
		while (gpServer.getJobStatus(JobID) != EsriJobStatus.esriJobSucceeded && gpServer.getJobStatus(JobID) != EsriJobStatus.esriJobFailed){
		    Thread.currentThread().sleep(3000);
		    System.out.println(gpServer.getJobStatus(JobID).toString());
		}
		
		if(gpServer.getJobStatus(JobID) == EsriJobStatus.esriJobFailed){
			return;
		}
		
		if(gpServer.getResultMapServerName() != null){
			System.out.println(gpServer.getResultMapServerName().toString() + "aaa");
		}
		
	}
	
	public void generateContout(String modelName) throws MalformedURLException, RemoteException, InterruptedException{
		GPString queryString = new GPString();
		queryString.setValue("PID=\'" + pid + "\'");
		//queryString.setValue("\"F10\"=\'" + pid + "\'");
		
		GPDouble baseContour = new GPDouble();
		baseContour.setValue(Double.parseDouble(base));
		
		GPDouble contourInterval = new GPDouble();
		contourInterval.setValue(Double.parseDouble(interval));
		
		GPValue[] gpValues = new GPValue[3];
		gpValues[0] = contourInterval;
		gpValues[1] = baseContour;
		gpValues[2] = queryString;
		
		GPServerBindingStub gpServer = new GPServerBindingStub(new java.net.URL(gpEndpoint), null);
		toolInfo = gpServer.getToolInfo(modelName);
		resultOpt = new GPResultOptions();
		resultOpt.setDensifyFeatures(true);
		
		JobID = gpServer.submitJob(toolInfo.getName(), gpValues, resultOpt, null);
		System.out.println(JobID);
		//Check the status of the job; if it's finished and successful, proceed - false delay.
		while (gpServer.getJobStatus(JobID) != EsriJobStatus.esriJobSucceeded && gpServer.getJobStatus(JobID) != EsriJobStatus.esriJobFailed){
		    Thread.sleep(3000);
		    System.out.println(gpServer.getJobStatus(JobID).toString());
		}
		
		if(gpServer.getResultMapServerName() != null){
			System.out.println(gpServer.getResultMapServerName().toString() + "aaa");
			//result = gpServer.getJobResult(JobID, null, resultOpt);
		}
		
	}
	
	public void gpResultDisplay() throws IOException{
		
		AGSLocalMapResource localResource = (AGSLocalMapResource) webContext.getResourceById(localMapResID);
		MapServer localMapServer = localResource.getLocalMapServer();
		localMapServer.refreshServerObjects();
		
		
		try {
			this.generateContout("servertask");
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
		
		changeADFLyrSourceByID(localResource);
	}

	private void changeRasterLyrRender(AGSLocalMapResource localResource) throws AutomationException, IOException{
		
		MapServer localMapServer = localResource.getLocalMapServer();
		String mapName = localMapServer.getDefaultMapName();
		
		IRasterLayer rasterLayer = (IRasterLayer) localMapServer.getLayer(mapName, 3);
		CustomRasterRender render = new CustomRasterRender();
		if(this.featureName != null){
			render.setFeatureName(featureName);
		}else{
			render.setFeatureName("shidu");
			System.out.println("featureName is null");
		}
		//render.setFeatureName("shidu");
		render.setRasterLayer(rasterLayer);
		render.renderByRasterClassify(localResource.getServerContext());
		localMapServer.getMap(mapName).deleteLayer(rasterLayer);
		rasterLayer = render.rasterLayer;
		localMapServer.getMap(mapName).addLayer(rasterLayer);
		localMapServer.getMap(mapName).moveLayer(rasterLayer, 1);
		
		AgsObjAccess access = new AgsObjAccess(localResource.getLocalMapServer(), localResource.getServerContext());
		
		AGSMapFunctionality mapFunc = (AGSMapFunctionality) localResource.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
		mapFunc.getMapServerInfo().setMapLayerInfos(access.getFromAOMapLyrInfos());
		mapFunc.getMapServerInfo().getDefaultMapDescription().setLayerDescriptions(access.getFromAOMapLyrDescs());
		mapFunc.getMapServerInfo().getMapLayerInfos()[0].setSubLayerIDs(new int[]{1, 2, 3});
		mapFunc.getMapServerInfo().getMapLayerInfos()[3].setParentLayerID(1);
		mapFunc.getMapServerInfo().getMapLayerInfos()[0].setName("¶¯Ì¬Í¼²ã");
	}
	
	private void changeADFLyrSourceByID(AGSLocalMapResource localResource) throws AutomationException, IOException{
		changeRasterLyrRender(localResource);
		
		
		
		//ADFÖÐ¸Ä±äSourceID£¬¸Ä±äÒ³ÃæÏÔÊ¾Í¼²ã
		AGSMapFunctionality mapFunc = (AGSMapFunctionality) localResource.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
		MapServerInfo adfMapServerInfo = mapFunc.getMapServerInfo();
		MapLayerInfo[] adfMapLayerInfos = adfMapServerInfo.getMapLayerInfos();
		MapDescription adfMapDescription = adfMapServerInfo.getDefaultMapDescription();
		LayerDescription[] adfLayerDescriptions = adfMapDescription.getLayerDescriptions();
		
		
		for(int i = 0; i < 4; i++){
			if(i == 1 || i == 2){
				adfLayerDescriptions[i].setSourceID(JobID);
				adfLayerDescriptions[i].setVisible(true);
				System.out.println("1,2");
			}
			//Ìæ»»Í¼²ã
			if(i == 3){
				
				adfLayerDescriptions[i].setSourceID(JobID);
				adfLayerDescriptions[i].setVisible(true);
			}
		}
		mapFunc = (AGSMapFunctionality) localResource.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
		mapFunc.getMapServerInfo().setMapLayerInfos(adfMapLayerInfos);
		mapFunc.getMapServerInfo().getDefaultMapDescription().setLayerDescriptions(adfLayerDescriptions);
		
		WebTrueTypeMarkerSymbol label = new WebTrueTypeMarkerSymbol();
		label.setFontName("ËÎÌå");
		label.addTextValue(this.picHead);
		GraphicElement ge = new GraphicElement();
		ge.setGeometry(new WebPoint(webContext.getWebMap().getCurrentExtent().getMinX(), webContext.getWebMap().getCurrentExtent().getMaxY()));
		ge.setSymbol(label);
		webContext.getWebGraphics().addGraphics(ge);
		
		try{
			WebPictureMarkerSymbol pic = new WebPictureMarkerSymbol();
			System.out.println(this.featureName);
			pic.setURL(new java.net.URL("http://localhost:8080/dynamicTask/images/" + this.featureName + ".jpg"));
			GraphicElement picGe = new GraphicElement();
			picGe.setGeometry(new WebPoint(webContext.getWebMap().getCurrentExtent().getMinX(), webContext.getWebMap().getCurrentExtent().getMinY()));
			picGe.setSymbol(pic);
			webContext.getWebGraphics().addGraphics(picGe);
		}catch(NullPointerException err){
			System.out.println(this.featureName + " Í¼ÀýÎ´ÕÒµ½,Í¼ÀýÌí¼ÓÊ§°Ü");
		}
		
		
		
		
		// refresh overview control.
		WebOverview webOv = webContext.getWebOverview();
		if (webOv != null) {
			AGSOverviewFunctionality of = (AGSOverviewFunctionality) localResource.getFunctionality(OverviewFunctionality.FUNCTIONALITY_NAME);
			of.initFunctionality(localResource);
			webOv.exportImage();
		}
	}
	
	public String getEndpoint() {
		return gpEndpoint;
	}

	public void setEndpoint(String endpoint) {
		this.gpEndpoint = endpoint;
	}

	public GPToolInfo getToolInfo() {
		return toolInfo;
	}

	public void setToolInfo(GPToolInfo toolInfo) {
		this.toolInfo = toolInfo;
	}

	public GPResult getResult() {
		return result;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public WebContext getWebContext() {
		return webContext;
	}

	public void setWebContext(WebContext webContext) {
		this.webContext = webContext;
	}

	public String getMapEndpoint() {
		return mapEndpoint;
	}

	public void setMapEndpoint(String mapEndpoint) {
		this.mapEndpoint = mapEndpoint;
	}

	public String getLocalMapResID() {
		return localMapResID;
	}

	public void setLocalMapResID(String localMapResID) {
		this.localMapResID = localMapResID;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public void setPicHead(String picHead) {
		String str = null;
		if(picHead != null){
			System.out.println(picHead.toString());
			try {
				byte[] temp = picHead.toString().getBytes("ISO-8859-1");
				str = new String(temp, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		
		this.picHead = str;
	}
}
