/**
 * 
 */
package qhqx.ags;

import java.io.IOException;

import com.esri.arcgis.carto.HorizontalLegendItem;
import com.esri.arcgis.carto.IImageDescription;
import com.esri.arcgis.carto.IImageDisplay;
import com.esri.arcgis.carto.IImageResult;
import com.esri.arcgis.carto.IImageType;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.ILegend;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.carto.IMapDescription;
import com.esri.arcgis.carto.IMapServerLayout;
import com.esri.arcgis.carto.ImageDescription;
import com.esri.arcgis.carto.ImageDisplay;
import com.esri.arcgis.carto.ImageType;
import com.esri.arcgis.carto.Legend;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.carto.esriImageFormat;
import com.esri.arcgis.carto.esriImageReturnType;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.ServerContext;

/**
 * @author yan
 *
 */
public class MapLegend {
	private MapServer mapServer;
	private ILayer layer;
	private ServerContext serverContext;
	private ILegend legend;
	
	public MapLegend() throws AutomationException, IOException{
		legend = (ILegend)serverContext.createObject(Legend.getClsid());
	}
	
	public void generateLegendFromLayer() throws AutomationException, IOException{
		
		HorizontalLegendItem hLegendItem = (HorizontalLegendItem)serverContext.createObject(HorizontalLegendItem.getClsid());
		layer = mapServer.getLayer(mapServer.getDefaultMapName(), 3);
		hLegendItem.setLayerByRef(layer);
		legend.addItem(hLegendItem);
		
	}
	
	public void generateLegendFromMap() throws AutomationException, IOException{
		IMap map = mapServer.getMap(mapServer.getDefaultMapName());
		for(int i = 0; i < map.getMapSurroundCount(); i++){
			if(map.getMapSurround(i) instanceof com.esri.arcgis.carto.Legend){
				legend = (ILegend) map.getMapSurround(i);
			}
		}
		//legend.removeItem(arg0);
	}
	
	public IImageResult printLegend() throws AutomationException, IOException{
		IImageType imgType = (IImageType) serverContext.createObject(ImageType.getClsid());
		imgType.setFormat(esriImageFormat.esriImageJPG);
		imgType.setFormat(esriImageReturnType.esriImageReturnURL);
		
		IImageDisplay imgDisp = (IImageDisplay) serverContext.createObject(ImageDisplay.getClsid());
		imgDisp.setHeight(10);
		imgDisp.setWidth(10);
		imgDisp.setDeviceResolution(96);
		
		IImageDescription imgDesc = (IImageDescription) serverContext.createObject(ImageDescription.getClsid());
		imgDesc.setDisplay(imgDisp);
		imgDesc.setType(imgType);
		
		IImageDisplay mapDisp = (IImageDisplay) serverContext.createObject(ImageDisplay.getClsid());
		mapDisp.setDeviceResolution(96);
		mapDisp.setHeight(10);
		mapDisp.setWidth(10);
		
		IMapServerLayout mapSvrLayout = mapServer;
		IMapDescription mapDesc = mapServer.getServerInfo(mapServer.getDefaultMapName()).getDefaultMapDescription();
		IImageResult imgResult = mapSvrLayout.exportLegend(legend, mapDesc, mapDisp, null, imgDesc);
		return imgResult;
	}

}
