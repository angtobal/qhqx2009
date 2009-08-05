/**
 * 
 */
package qhqx.ags;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.adf.web.ags.data.AGSMapFunctionality;
import com.esri.adf.web.ags.data.AGSOverviewFunctionality;
import com.esri.adf.web.ags.util.AGSUtil;
import com.esri.adf.web.data.MapFunctionality;
import com.esri.adf.web.data.OverviewFunctionality;
import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.WebOverview;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.carto.IMapDescription;
import com.esri.arcgis.carto.IMapServerInfo;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.datasourcesfile.ShapefileWorkspaceFactory;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IFeatureWorkspace;
import com.esri.arcgis.geodatabase.IWorkspaceFactory;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.IServerContext;
import com.esri.arcgis.server.ServerContext;
import com.esri.arcgisws.LayerDescription;
import com.esri.arcgisws.MapDescription;
import com.esri.arcgisws.MapLayerInfo;
import com.esri.arcgisws.MapServerInfo;
import com.esri.arcgisws.MapServerPort;

/**
 * @author yan
 * 
 */
public class MapLayerConfig {

	private WebContext webContext;
	private AGSLocalMapResource localMapResource;
	private AGSMapFunctionality mapFunc;
	private MapServerPort mapServerPort;
	private MapServerInfo mapServerInfo;
	private MapLayerInfo[] mapLayerInfo;
	private MapDescription mapDescription;
	private LayerDescription[] layerDescription;
	private IMap targetMap;// IMap接口可以用来添加数据到ServerObject

	public MapLayerConfig(WebContext webContext) throws IOException {
		this.setWebContext(webContext);
		localMapResource = (AGSLocalMapResource) webContext.getResources().get(
				"ags1");
		mapFunc = (AGSMapFunctionality) localMapResource.getFunctionalities()
				.get("map");
		mapServerPort = localMapResource.getMapServer();

		try {
			mapServerInfo = mapServerPort.getServerInfo(mapServerPort
					.getDefaultMapName());
			targetMap = localMapResource.getLocalMapServer().getMap(
					localMapResource.getLocalMapServer().getDefaultMapName());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AutomationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mapLayerInfo = mapServerInfo.getMapLayerInfos();
		mapDescription = mapFunc.getMapDescription();
		layerDescription = mapDescription.getLayerDescriptions();
	}

	public void addDataToIMap() throws IOException {
		// IFeatureWorkspace workspace = this.connectToFileDB("D:\\test.gdb",
		// localMapResource.getServerContext());
		IFeatureWorkspace workspace = this.connectToFileDB("D:\\testData",
				localMapResource.getServerContext());

		IFeatureClass featureClass = workspace.openFeatureClass("test");
		// workspace.openRelationshipClass("test");
		if (featureClass != null) {
			System.out.println(featureClass);
		} else {
			System.out.println("featureClass is null");
		}

		IFeatureLayer featureLayer = (IFeatureLayer) localMapResource
				.getServerContext().createObject(FeatureLayer.getClsid());
		featureLayer.setFeatureClassByRef(featureClass);

		featureLayer.setName("bbb");
		featureLayer.setVisible(true);
		
		System.out.println("a:" + mapFunc.getLayerInfos().length);
		
		targetMap.addLayer(featureLayer);
		
		System.out.println("b:" + mapFunc.getLayerInfos().length);
		
		localMapResource.getLocalMapServer().refreshServerObjects();
		System.out.println("c:" + mapFunc.getLayerInfos().length);
		mapServerInfo = mapServerPort.getServerInfo(mapServerPort.getDefaultMapName());
		mapFunc.getMapServerInfo().setDefaultMapDescription(mapServerInfo.getDefaultMapDescription());
		mapFunc.setMapDescription(mapServerInfo.getDefaultMapDescription());
		//mapFunc.getMapServerInfo().setMapLayerInfos(mapFunc.getLayerInfos());
		//mapFunc.getMapServerInfo().getDefaultMapDescription().setLayerDescriptions(newAdfLyrDescs);
		System.out.println("d:" + mapFunc.getLayerInfos().length);
		WebOverview webOverview = webContext.getWebOverview();
		if (webOverview != null) {
			AGSOverviewFunctionality of = (AGSOverviewFunctionality) localMapResource
					.getFunctionality(OverviewFunctionality.FUNCTIONALITY_NAME);
			of.initFunctionality(localMapResource);
			webOverview.exportImage();
		}

		com.esri.adf.web.data.WebToc newToc = new com.esri.adf.web.data.WebToc();
		newToc.init(webContext);
		webContext.setWebToc(newToc);
		
		/*WebMap newWebMap = new WebMap();
		newWebMap.init(webContext);
		webContext.setWebMap(newWebMap);*/
		

		webContext.refresh();

		/*// 添加图层到ServerObject后，调用下面的代码，让修改生效。调用下面的代码后，将会使ServerObject的状态发生改变
		localMapResource.getLocalMapServer().refreshServerObjects();

		// Server
		// Object状态改变后，需要重新得到serverInfo，并且设置MapFunctionality里面的LayerDescription，把新的layer加入进去
		mapServerInfo = mapServerPort.getServerInfo(mapServerPort
				.getDefaultMapName());
		System.out.println("defaultMapName:"
				+ mapServerPort.getDefaultMapName());
		mapFunc.getMapServerInfo().setDefaultMapDescription(
				mapServerInfo.getDefaultMapDescription());
		mapFunc.setMapDescription(mapServerInfo.getDefaultMapDescription());
		for (int i = 0; i < targetMap.getLayerCount(); i++) {
			System.out.println(mapDescription.getLayerDescriptions()[i]
					.getDefinitionExpression());
			System.out.println("targetMap description:"
					+ targetMap.getDescription());
			System.out.println(targetMap.getName());
		}
		webContext.getWebMap().init(webContext);
		webContext.refresh();*/
		
		//mapLayerConfigRefresh(layerName, firstTimeLayerAdd)
	}

	public IFeatureWorkspace connectToFileDB(String fileGDB,
			IServerContext serverContext) throws UnknownHostException,
			IOException {
		// IWorkspaceFactory factory =
		// (IWorkspaceFactory)serverContext.createObject(FileGDBWorkspaceFactory.getClsid());
		IWorkspaceFactory factory = (IWorkspaceFactory) serverContext
				.createObject(ShapefileWorkspaceFactory.getClsid());
		IFeatureWorkspace workspace = (IFeatureWorkspace) factory.openFromFile(
				fileGDB, 0);
		return workspace;
	}

	public void removeLayer() throws AutomationException, IOException {
		MapLayerInfo[] newMapLayerInfo = new MapLayerInfo[mapLayerInfo.length - 1];
		System.out.println(newMapLayerInfo.length);
		System.arraycopy(mapLayerInfo, 1, newMapLayerInfo, 0,
				newMapLayerInfo.length);
		mapServerInfo.setMapLayerInfos(newMapLayerInfo);

		LayerDescription[] newLayerDescription = new LayerDescription[layerDescription.length - 1];
		System.arraycopy(layerDescription, 1, newLayerDescription, 0,
				newLayerDescription.length);
		mapDescription.setLayerDescriptions(newLayerDescription);

		/*// 添加图层到ServerObject后，调用下面的代码，让修改生效。调用下面的代码后，将会使ServerObject的状态发生改变
		localMapResource.getLocalMapServer().refreshServerObjects();

		// Server
		// Object状态改变后，需要重新得到serverInfo，并且设置MapFunctionality里面的LayerDescription，把新的layer加入进去
		mapServerInfo = mapServerPort.getServerInfo(mapServerPort
				.getDefaultMapName());
		System.out.println("defaultMapName:"
				+ mapServerPort.getDefaultMapName());
		mapFunc.getMapServerInfo().setDefaultMapDescription(
				mapServerInfo.getDefaultMapDescription());
		mapFunc.setMapDescription(mapServerInfo.getDefaultMapDescription());
		for (int i = 0; i < targetMap.getLayerCount(); i++) {
		}
		webContext.refresh();
		System.out.println("end");*/
	}

	public void mapLayerConfigRefresh(String layerName, boolean firstTimeLayerAdd) throws IOException {
		MapServer mapServer = localMapResource.getLocalMapServer();
		mapServer.refreshServerObjects();
		ServerContext serverContext = new ServerContext(localMapResource
				.getServerContext());

		IMapServerInfo newMapServerInfo = mapServer.getServerInfo(mapServer
				.getDefaultMapName());
		IMapDescription newMapDescription = newMapServerInfo
				.getDefaultMapDescription();
		
		MapServerInfo mapControlServerInfo = (MapServerInfo) AGSUtil
				.createStubFromArcObject(newMapDescription,
						MapServerInfo.class, serverContext);
		MapDescription mapControlDescription = (MapDescription) AGSUtil
				.createStubFromArcObject(newMapDescription,
						MapDescription.class, serverContext);

		// 从AO获取描述并将之转化为桩对象
		MapLayerInfo[] fromAOMapLayerInfos = mapControlServerInfo
				.getMapLayerInfos();
		LayerDescription[] fromAOLayerDescriptions = mapControlDescription
				.getLayerDescriptions();

		// 获取MapDescription在网页层中的当前状态
		AGSMapFunctionality agsMapFunc = (AGSMapFunctionality) localMapResource
				.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
		MapServerInfo adfServerInfo = agsMapFunc.getMapServerInfo();
		MapLayerInfo[] adfMapLayerInfos = adfServerInfo.getMapLayerInfos();
		LayerDescription[] adfLayerDescriptions = adfServerInfo
				.getDefaultMapDescription().getLayerDescriptions();

		// 基于当前的web description和AO descriptions创建新的descriptions(以层数多的为准)，这样连接这两个单一的descriptions集合
		// 以同时反映出后台AO和web map descriptions
		agsMapFunc = (AGSMapFunctionality) localMapResource
				.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
		MapLayerInfo[] newAdfMapLayerInfos;
		LayerDescription[] newAdfLayerDescriptions;

		if (fromAOMapLayerInfos.length > adfMapLayerInfos.length) {
			newAdfMapLayerInfos = new MapLayerInfo[fromAOMapLayerInfos.length];
			newAdfLayerDescriptions = new LayerDescription[fromAOLayerDescriptions.length];
			for (int i = 0; i < newAdfMapLayerInfos.length; i++) {
				newAdfMapLayerInfos[i] = fromAOMapLayerInfos[i];
				newAdfLayerDescriptions[i] = fromAOLayerDescriptions[i];
			}
		} else {
			newAdfMapLayerInfos = new MapLayerInfo[adfMapLayerInfos.length];
			newAdfLayerDescriptions = new LayerDescription[adfLayerDescriptions.length];
			for (int i = 0; i < adfMapLayerInfos.length; i++) {
				newAdfMapLayerInfos[i] = adfMapLayerInfos[i];
				newAdfLayerDescriptions[i] = adfLayerDescriptions[i];
			}
		}

		for (int i = 0; i < newAdfMapLayerInfos.length; i++) {
			// First time when a layer is added.
			if (firstTimeLayerAdd
					&& newAdfMapLayerInfos[i].getName().equals(layerName)) {
				newAdfMapLayerInfos[i] = fromAOMapLayerInfos[i];
				newAdfLayerDescriptions[i] = fromAOLayerDescriptions[i];
			}
			// For all other requests
			else {
				for (int j = 0; j < adfMapLayerInfos.length; j++) {
					if (newAdfMapLayerInfos[i].getName().equals(
							adfMapLayerInfos[j].getName())) {
						newAdfMapLayerInfos[i] = adfMapLayerInfos[j];
						newAdfLayerDescriptions[i] = adfLayerDescriptions[j];

						// Assigning the layerId from the latest AO so that the
						// web
						// layer ids and the AO ids match
						if (i < fromAOMapLayerInfos.length) {
							newAdfMapLayerInfos[i]
									.setLayerID(fromAOMapLayerInfos[i]
											.getLayerID());
							newAdfLayerDescriptions[i]
									.setLayerID(fromAOLayerDescriptions[i]
											.getLayerID());
						}

					}
				}
			}
		}

		// 刷新ArcGIS Map functionality
		agsMapFunc = (AGSMapFunctionality) localMapResource
				.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
		agsMapFunc.getMapServerInfo().setMapLayerInfos(newAdfMapLayerInfos);
		agsMapFunc.getMapServerInfo().getDefaultMapDescription().setLayerDescriptions(
				newAdfLayerDescriptions);

		// 刷新overview control.
		WebOverview webOverview = webContext.getWebOverview();
		if (webOverview != null) {
			AGSOverviewFunctionality of = (AGSOverviewFunctionality) localMapResource
					.getFunctionality(OverviewFunctionality.FUNCTIONALITY_NAME);
			of.initFunctionality(localMapResource);
			webOverview.exportImage();
		}

		com.esri.adf.web.data.WebToc newToc = new com.esri.adf.web.data.WebToc();
		newToc.init(webContext);
		webContext.setWebToc(newToc);

		webContext.refresh();
	}

	public void removeLayersFromAO() {
		
	}

	public WebContext getWebContext() {
		return webContext;
	}

	public void setWebContext(WebContext webContext) {
		this.webContext = webContext;
	}

	public MapLayerInfo[] getMapLayerInfo() {
		return mapLayerInfo;
	}

	public void setMapLayerInfo(MapLayerInfo[] mapLayerInfo) {
		this.mapLayerInfo = mapLayerInfo;
	}

}
