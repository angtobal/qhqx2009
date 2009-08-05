/**
 * 
 */
package qhqx.ags;

import java.io.IOException;

import com.esri.adf.web.ags.util.AGSUtil;
import com.esri.arcgis.carto.IMapDescription;
import com.esri.arcgis.carto.IMapServerInfo;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.IServerContext;
import com.esri.arcgisws.MapDescription;

/**
 * @author yan
 *
 */
public class AgsObjAccess {
	protected com.esri.arcgisws.MapLayerInfo[] fromAOMapLyrInfos;
	protected com.esri.arcgisws.LayerDescription[] fromAOMapLyrDescs;
	protected MapDescription mapDesc;
	
	public AgsObjAccess(MapServer mapServer, IServerContext serverContext) throws AutomationException, IOException{
		mapServer.refreshServerObjects();
		IMapServerInfo newMapServerInfo = mapServer.getServerInfo(mapServer.getDefaultMapName());
		IMapDescription newMapDescription = newMapServerInfo.getDefaultMapDescription();
		
		com.esri.arcgisws.MapServerInfo serverInfo = (com.esri.arcgisws.MapServerInfo) AGSUtil.createStubFromArcObject(newMapServerInfo, com.esri.arcgisws.MapServerInfo.class, serverContext);
		com.esri.arcgisws.MapDescription mapDesc = (com.esri.arcgisws.MapDescription) AGSUtil.createStubFromArcObject(newMapDescription, com.esri.arcgisws.MapDescription.class, serverContext);
		this.setMapDesc(mapDesc);
		
		this.fromAOMapLyrInfos = serverInfo.getMapLayerInfos();
		this.fromAOMapLyrDescs = mapDesc.getLayerDescriptions();
	}

	public com.esri.arcgisws.MapLayerInfo[] getFromAOMapLyrInfos() {
		return this.fromAOMapLyrInfos;
	}

	public com.esri.arcgisws.LayerDescription[] getFromAOMapLyrDescs() {
		return this.fromAOMapLyrDescs;
	}

	public MapDescription getMapDesc() {
		return mapDesc;
	}

	public void setMapDesc(MapDescription mapDesc) {
		this.mapDesc = mapDesc;
	}
	
	
}
