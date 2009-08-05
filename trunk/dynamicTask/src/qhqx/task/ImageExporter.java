/**
 * 
 */
package qhqx.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.arcgis.carto.IImageResult;
import com.esri.arcgis.carto.ILayerDescription;
import com.esri.arcgis.carto.ILayerDescriptions;
import com.esri.arcgis.carto.IMapDescription;
import com.esri.arcgis.carto.IMapServerInfo;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.carto.esriImageFormat;
import com.esri.arcgis.carto.esriImageReturnType;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.IServerContext;
import com.esri.arcgisws.EsriImageFormat;
import com.esri.arcgisws.EsriImageReturnType;
import com.esri.arcgisws.ImageDescription;
import com.esri.arcgisws.ImageDisplay;
import com.esri.arcgisws.ImageType;
import com.esri.arcgisws.MapDescription;
import com.esri.arcgisws.MapImage;
import com.esri.arcgisws.MapServerBindingStub;
import com.esri.arcgisws.MapServerInfo;


/**
 * @author yan
 *
 */
public class ImageExporter {
	
	protected String mapEndpoint;
	protected ImageType type;
	protected ImageDisplay display;
	protected ImageDescription description;
	protected MapImage mapImage;
	
	public ImageExporter(EsriImageFormat format, EsriImageReturnType retType, int width, int height, double dpi){
		type = new ImageType();
		type.setImageFormat(format);
		type.setImageReturnType(retType);
		
		display = new ImageDisplay();
		display.setImageDPI(dpi);
		display.setImageWidth(width);
		display.setImageHeight(height);
		
		description = new ImageDescription();
		description.setImageDisplay(display);
		description.setImageType(type);
	}
	
	public MapImage connectServer(String mapEndpoint) throws MalformedURLException, RemoteException{
		MapServerBindingStub mapServer = new MapServerBindingStub(new java.net.URL(mapEndpoint), null);
		MapServerInfo mapServerInfo = mapServer.getServerInfo(mapServer.getDefaultMapName());
		
		MapDescription mapDescription = mapServerInfo.getDefaultMapDescription();
		mapImage = mapServer.exportMapImage(mapDescription, description);
		return mapImage;
	}
	
	public com.esri.arcgis.carto.MapImage connectServer(String mapEndpoint, AGSLocalMapResource localResource) throws AutomationException, IOException{
		IServerContext serverContext = (IServerContext) localResource.getServerContext();
		//MapServer mapServer = (MapServer)serverContext.getServerObject();
		MapServer mapServer = localResource.getLocalMapServer();
		com.esri.arcgis.carto.ImageType it = (com.esri.arcgis.carto.ImageType)serverContext.createObject(com.esri.arcgis.carto.ImageType.getClsid());
		it.setFormat(esriImageFormat.esriImageJPG);
		it.setReturnType(esriImageReturnType.esriImageReturnURL);
		com.esri.arcgis.carto.ImageDisplay idisp = (com.esri.arcgis.carto.ImageDisplay)serverContext.createObject(com.esri.arcgis.carto.ImageDisplay.getClsid());
		idisp.setHeight(500);
		idisp.setWidth(600);
		idisp.setDeviceResolution(96);
		com.esri.arcgis.carto.ImageDescription pID = (com.esri.arcgis.carto.ImageDescription)serverContext.createObject
		    (com.esri.arcgis.carto.ImageDescription.getClsid());
		pID.setDisplay(idisp);
		pID.setType(it);
		IMapServerInfo mapServerInfo = mapServer.getServerInfo(mapServer.getDefaultMapName());
		IMapDescription pMD = mapServerInfo.getDefaultMapDescription();
		IImageResult pMI = mapServer.exportMapImage(pMD, pID);
		ILayerDescriptions layerDescs = pMD.getLayerDescriptions();
		ILayerDescription pLD = layerDescs.getElement(0);
		pLD.setVisible(true);
		
		/*FileOutputStream fileOutStream = new FileOutputStream();
		fileOutStream.write(pMI.getMimeData());
		fileOutStream.close();
		System.out.println("目标文件生成");*/
		
		System.out.println(pMI.getURL());
		
		serverContext.releaseContext();
		
		return (com.esri.arcgis.carto.MapImage)pMI;
	}
	
	public MapImage getMapImage() {
		return mapImage;
	}

}
