/**
 * 
 */
package qhqx.task;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;

import qhqx.ags.AgsObjAccess;
import qhqx.ags.CustomMapLegend;

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
import com.esri.arcgis.carto.TextElement;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.IServerContext;
import com.esri.arcgisws.EsriImageFormat;
import com.esri.arcgisws.EsriImageReturnType;
import com.esri.arcgisws.ImageDescription;
import com.esri.arcgisws.ImageDisplay;
import com.esri.arcgisws.MapImage;

/**
 * @author yan
 *
 */
public class PictureBuilder extends GPServerInfo{
	
	private static final String MODEL_NAME = "servertask";

	private String mapEndpoint = null;
	
	private String localMapResID = null;
	
	private String featureName = null;
	
	private String fileName = null;
	private String pictureHead;
	
	
	public void gpResultDisplay() throws IOException{
		
		AGSLocalMapResource localResource = (AGSLocalMapResource) webContext.getResourceById(localMapResID);
		MapServer localMapServer = localResource.getLocalMapServer();
		localMapServer.refreshServerObjects();
		
		try {
			this.generateContout(MODEL_NAME);
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
		render.setPid(pid);
		if(this.featureName != null){
			render.setFeatureName(featureName);
		}else{
			render.setFeatureName("common");
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
		textElement.setText(pictureHead + "  " +WeatherRenderInfo.getChName(featureName));
		container.addElement(textElement, 0);
		
		CustomMapLegend mapLegend = new CustomMapLegend(localResource);
		mapLegend.createLegend();
		
		/*PngPictureElement pngPicElem = (PngPictureElement) serverContext.createObject(PngPictureElement.getClsid());
		try{
			pngPicElem.importPictureFromFile("c:\\pic\\feature\\" + WeatherRenderInfo.getName(this.featureName) + ".jpg");
			IEnvelopeGEN env = (IEnvelopeGEN) serverContext.createObject(Envelope.getClsid());
			env.putCoords(103.5, 31.5, 104.5, 38.5);
			pngPicElem.setGeometry((IGeometry) env);
			container.addElement(pngPicElem, 0);
		}catch(NullPointerException err){
			System.out.println("所选要素图例不存在 " + this.featureName);
		}*/
		
		
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
		mapFunc.setCurrentExtent(new WebExtent(89.400228, 31.542524, 104.9, 40.339596));
		mapFunc.getLayerInfos()[6].setMinScale(0);
		
		FileOutputStream fileOutStream = new FileOutputStream("c:\\sharedata\\" + this.fileName + ".jpg");
		
		MapImage mapim = localResource.getMapServer().exportMapImage(mapFunc.getMapDescription(), idesc);
		fileOutStream.write(mapim.getImageData());
		fileOutStream.close();
		System.out.println("目标文件生成 " + this.fileName);
		
		mapFunc.getLayerDescriptions()[1].setVisible(true);
		container.deleteAllElements();
		
		try{
			java.net.URL url = new java.net.URL("http://222.18.139.70:8080/GisStutsPic_auto_SavePic.action?FILENAME=" + this.fileName + ".JPG");
			@SuppressWarnings("unused")
			URLConnection urlConn = url.openConnection();
			//urlConn.connect();
		}catch(Exception err){
			System.out.println(err.getMessage());
		}
		
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
