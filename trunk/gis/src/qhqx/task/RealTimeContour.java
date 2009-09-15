/**
 * 
 */
package qhqx.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import qhqx.ags.AgsObjAccess;
import qhqx.ags.CustomMapLegend;
import qinghai.qixiang.winddirectionandspeed.WindDirectionSpeed;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.adf.web.ags.data.AGSMapFunctionality;
import com.esri.adf.web.ags.data.AGSOverviewFunctionality;
import com.esri.adf.web.data.GraphicElement;
import com.esri.adf.web.data.MapFunctionality;
import com.esri.adf.web.data.OverviewFunctionality;
import com.esri.adf.web.data.WebOverview;
import com.esri.adf.web.data.geometry.WebPoint;
import com.esri.adf.web.data.symbol.WebPictureMarkerSymbol;
import com.esri.adf.web.data.symbol.WebTrueTypeMarkerSymbol;
import com.esri.arcgis.carto.IRasterLayer;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgisws.LayerDescription;
import com.esri.arcgisws.MapDescription;
import com.esri.arcgisws.MapLayerInfo;
import com.esri.arcgisws.MapServerInfo;

/**
 * @author yan
 *
 */
public class RealTimeContour extends GPServerInfo {
	/**
	 * 
	 */
	private static final String MODEL_NAME = "servertask";

	private String mapEndpoint = null;
	
	private String localMapResID = null;
	
	private String featureName = null;
	private String picHead = null;
	
	private String lgdurl = null;
	
	
	public void gpResultDisplay() throws IOException{
		
		AGSLocalMapResource localResource = (AGSLocalMapResource) webContext.getResourceById(localMapResID);
		MapServer localMapServer = localResource.getLocalMapServer();
		localMapServer.refreshServerObjects();
		
		try {
			this.generateContout(MODEL_NAME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		changeADFLyrSourceByID(localResource);
		localResource.getServerContext().releaseContext();
	}

	private void changeRasterLyrRender(AGSLocalMapResource localResource) throws AutomationException, IOException{
		
		MapServer localMapServer = localResource.getLocalMapServer();
		String mapName = localMapServer.getDefaultMapName();
		
		IRasterLayer rasterLayer = (IRasterLayer) localMapServer.getLayer(mapName, 3);
		CustomRasterRender render = new CustomRasterRender();
		render.setPid(pid);
		if(this.featureName != null){
			render.setFeatureName(featureName);
		}else{
			render.setFeatureName("common");
			System.out.println("featureName is null, common");
		}
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
		
		webContext.getWebGraphics().clearGraphics();
		
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
			}
			//Ìæ»»Í¼²ã
			if(i == 3){
				System.out.println(JobID);
				adfLayerDescriptions[i].setSourceID(JobID);
				adfLayerDescriptions[i].setVisible(true);
			}
		}
		//localResource = (AGSLocalMapResource) webContext.getResourceById(localMapResID);
		CustomMapLegend mapLgd = new CustomMapLegend(localResource);
		lgdurl = mapLgd.printLegendWithUrl();
		
		mapFunc = (AGSMapFunctionality) localResource.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
		mapFunc.getMapServerInfo().setMapLayerInfos(adfMapLayerInfos);
		mapFunc.getMapServerInfo().getDefaultMapDescription().setLayerDescriptions(adfLayerDescriptions);
		mapFunc.getLayerDescriptions()[1].setVisible(false);
		
		WebTrueTypeMarkerSymbol label = new WebTrueTypeMarkerSymbol();
		label.setFontName("ËÎÌå");
		label.addTextValue(this.picHead + " " + WeatherRenderInfo.getChName(featureName));
		GraphicElement ge = new GraphicElement();
		//ge.setGeometry(new WebPoint(webContext.getWebMap().getCurrentExtent().getMinX() + 1, webContext.getWebMap().getCurrentExtent().getMaxY() - 1));
		ge.setGeometry(new WebPoint(96, 40));
		ge.setSymbol(label);
		webContext.getWebGraphics().addGraphics(ge);
		
		try{
			WebPictureMarkerSymbol pic = new WebPictureMarkerSymbol();
			System.out.println(this.featureName);
			//pic.setURL(new java.net.URL("http://localhost:8080/gis/images/" + this.featureName + ".jpg"));
			pic.setURL(new java.net.URL(lgdurl));
			GraphicElement picGe = new GraphicElement();
			//picGe.setGeometry(new WebPoint(webContext.getWebMap().getCurrentExtent().getMinX()+1, webContext.getWebMap().getCurrentExtent().getMinY()-1));
			picGe.setGeometry(new WebPoint(103.9, 35));
			picGe.setSymbol(pic);
			webContext.getWebGraphics().addGraphics(picGe);
		}catch(NullPointerException err){
			System.out.println(this.featureName + " Í¼ÀýÎ´ÕÒµ½,Í¼ÀýÌí¼ÓÊ§°Ü");
		}
		
		//MapImage img1 = (MapImage)webContext.getWebMap().exportMapImage();
		
		if(featureName.equals("fengsu") || featureName.equals("fengxiang")){
			WindDirectionSpeed wind = new WindDirectionSpeed();
			wind.setGContext(webContext);
			wind.setPid(pid);
			wind.addWindSymbol();
		}
		
		
		// refresh overview control.
		WebOverview webOv = webContext.getWebOverview();
		if (webOv != null) {
			AGSOverviewFunctionality of = (AGSOverviewFunctionality) localResource.getFunctionality(OverviewFunctionality.FUNCTIONALITY_NAME);
			of.initFunctionality(localResource);
			webOv.exportImage();
		}
		
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
