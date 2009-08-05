/**
 * 
 */
package qhqx.task;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.rmi.RemoteException;

import qhqx.ags.AgsObjAccess;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.adf.web.ags.data.AGSMapFunctionality;
import com.esri.adf.web.ags.data.AGSOverviewFunctionality;
import com.esri.adf.web.data.MapFunctionality;
import com.esri.adf.web.data.OverviewFunctionality;
import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.WebOverview;
import com.esri.adf.web.data.geometry.WebExtent;
import com.esri.arcgis.carto.IGraphicsContainer;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.carto.IRasterLayer;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.carto.PngPictureElement;
import com.esri.arcgis.carto.TextElement;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelopeGEN;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.IServerContext;
import com.esri.arcgisws.EsriImageFormat;
import com.esri.arcgisws.EsriImageReturnType;
import com.esri.arcgisws.EsriJobStatus;
import com.esri.arcgisws.GPDouble;
import com.esri.arcgisws.GPResult;
import com.esri.arcgisws.GPResultOptions;
import com.esri.arcgisws.GPServerBindingStub;
import com.esri.arcgisws.GPString;
import com.esri.arcgisws.GPToolInfo;
import com.esri.arcgisws.GPValue;
import com.esri.arcgisws.ImageDescription;
import com.esri.arcgisws.ImageDisplay;
import com.esri.arcgisws.MapImage;

/**
 * @author yan
 *
 */
public class PictureBuilder {
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
	private String fileName = null;
	private String pictureHead;
	
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
		
		if(gpServer.getResultMapServerName() != null){
			System.out.println(gpServer.getResultMapServerName().toString() + "aaa");
		}
		
	}
	
	public void generateContout(String modelName) throws MalformedURLException, RemoteException, InterruptedException{
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
		    Thread.sleep(3000);
		    System.out.println(gpServer.getJobStatus(JobID).toString() + " " + this.fileName);
		}
		
		if(gpServer.getResultMapServerName() != null){
			System.out.println(gpServer.getResultMapServerName().toString() + "aaa");
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

	@SuppressWarnings("deprecation")
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
		render.setRasterLayer(rasterLayer);
		render.renderByRasterClassify(localResource.getServerContext());
		localMapServer.getMap(mapName).deleteLayer(rasterLayer);
		rasterLayer = render.rasterLayer;
		localMapServer.getMap(mapName).addLayer(rasterLayer);
		localMapServer.getMap(mapName).moveLayer(rasterLayer, 1);
		
		
		//以下可以改到ImageExporter中去，结合更紧密
		IServerContext serverContext = localResource.getServerContext();
		MapServer mapServer = (MapServer) serverContext.getServerObject();
		IMap focusMap = mapServer.getMap(mapServer.getDefaultMapName());
		IGraphicsContainer container = (IGraphicsContainer)focusMap;
		
		TextElement textElement = (TextElement)serverContext.createObject(TextElement.getClsid());
		Point point = (Point)serverContext.createObject(Point.getClsid());
		point.setX(96);
		point.setY(40);
		textElement.setGeometry(point);
		textElement.setFontName("宋体");
		textElement.setText(pictureHead + "  " +WeatherRenderInfo.getName(featureName));
		container.addElement(textElement, 0);
		
		PngPictureElement pngPicElem = (PngPictureElement) serverContext.createObject(PngPictureElement.getClsid());
		pngPicElem.importPictureFromFile("c:\\pic\\feature\\" + WeatherRenderInfo.getName(this.featureName) + ".jpg");
		IEnvelopeGEN env = (IEnvelopeGEN) serverContext.createObject(Envelope.getClsid());
		env.putCoords(103.5, 31.5, 104.5, 38.5);
		pngPicElem.setGeometry((IGeometry) env);
		container.addElement(pngPicElem, 0);
		
		AgsObjAccess access = new AgsObjAccess(localResource.getLocalMapServer(), localResource.getServerContext());
		
		com.esri.arcgisws.ImageType itype = new com.esri.arcgisws.ImageType();
	    itype.setImageFormat(EsriImageFormat.esriImageJPG);
	    itype.setImageReturnType(EsriImageReturnType.esriImageReturnMimeData);
	    ImageDisplay disp = new ImageDisplay();
	    disp.setImageHeight(290);
	    disp.setImageWidth(464);
	    disp.setImageDPI(96);
	    ImageDescription idesc = new ImageDescription();
	    idesc.setImageDisplay(disp);
	    idesc.setImageType(itype);
		
		AGSMapFunctionality mapFunc = (AGSMapFunctionality) localResource.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
		mapFunc.getMapServerInfo().setMapLayerInfos(access.getFromAOMapLyrInfos());
		mapFunc.getMapServerInfo().getDefaultMapDescription().setLayerDescriptions(access.getFromAOMapLyrDescs());
		mapFunc.getMapServerInfo().getMapLayerInfos()[0].setSubLayerIDs(new int[]{1, 2, 3});
		mapFunc.getMapServerInfo().getMapLayerInfos()[3].setParentLayerID(1);
		mapFunc.getMapServerInfo().getMapLayerInfos()[0].setName("动态图层");
		
		for(int i = 0; i < 4; i++){
			if(i == 1 || i == 2){
				mapFunc.getLayerDescriptions()[i].setSourceID(JobID);
				mapFunc.getLayerDescriptions()[i].setVisible(true);
				System.out.println("1,2");
			}
			//替换图层
			if(i == 3){
				
				mapFunc.getLayerDescriptions()[i].setSourceID(JobID);
				mapFunc.getLayerDescriptions()[i].setVisible(true);
			}
		}
		
		mapFunc.getLayerDescriptions()[1].setVisible(false);
		mapFunc.setCurrentExtent(new WebExtent(89.400228, 31.542524, 104.5, 40.339596));
		mapFunc.getLayerInfos()[6].setMinScale(0);
		
		FileOutputStream fileOutStream = new FileOutputStream("c:\\sharedata\\" + this.fileName + ".jpg");
		
		MapImage mapim = localResource.getMapServer().exportMapImage(mapFunc.getMapDescription(), idesc);
		fileOutStream.write(mapim.getImageData());
		fileOutStream.close();
		System.out.println("目标文件生成" + " " + this.fileName);
		
		mapFunc.getLayerDescriptions()[1].setVisible(true);
		container.deleteAllElements();
		
		java.net.URL url = new java.net.URL("http://http://222.18.139.70:8080/GisStutsPic_auto_SavePic.action?FILENAME=" + this.fileName + ".JPG");
		URLConnection urlConn = url.openConnection();
		//urlConn.connect();
	}
	
	private void changeADFLyrSourceByID(AGSLocalMapResource localResource) throws AutomationException, IOException{
		changeRasterLyrRender(localResource);
		
		//刷新鹰眼
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

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPictureHead() {
		return pictureHead;
	}

	public void setPictureHead(String pictureHead) {
		this.pictureHead = pictureHead;
	}

}
