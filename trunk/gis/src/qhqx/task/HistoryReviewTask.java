package qhqx.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import qhqx.ags.MapLayerConfig;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.adf.web.ags.data.AGSMapFunctionality;
import com.esri.adf.web.ags.data.AGSOverviewFunctionality;
import com.esri.adf.web.ags.util.AGSUtil;
import com.esri.adf.web.data.MapFunctionality;
import com.esri.adf.web.data.OverviewFunctionality;
import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.WebContextInitialize;
import com.esri.adf.web.data.WebContextObserver;
import com.esri.adf.web.data.WebLifecycle;
import com.esri.adf.web.data.WebOverview;
import com.esri.adf.web.data.WebToc;
import com.esri.adf.web.faces.event.TaskEvent;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.IMapServerInfo;
import com.esri.arcgis.carto.Map;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.datasourcesfile.ShapefileWorkspaceFactory;
import com.esri.arcgis.geodatabase.Workspace;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.ServerContext;
import com.esri.arcgisws.LayerDescription;
import com.esri.arcgisws.MapLayerInfo;
import com.esri.arcgisws.MapServerInfo;

public class HistoryReviewTask implements WebContextObserver,
		WebContextInitialize, WebLifecycle {

	private AGSLocalMapResource localMapRes;
	private WebContext webContext;

	public LinkedHashMap<String, Integer> addedLayers = new LinkedHashMap<String, Integer>();
	public ArrayList<FeatureLayer> removeLayers = new ArrayList<FeatureLayer>();

	private String year;
	private String month;
	private String day;
	private String time;
	private HistoryReviewTaskInfo taskInfo = new HistoryReviewTaskInfo();

	public String getYear() {
		return year;
	}

	public void setYear(String value) {
		year = value;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String value) {
		month = value;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String value) {
		day = value;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String value) {
		time = value;
	}

	public void search(TaskEvent event) throws AutomationException, IOException {
		System.out.println(this.getClass().getName() + ":search()");
		WebContext webContext = event.getWebContext();
		this.webContext = webContext;
		if (Integer.parseInt(getMonth()) < 10) {
			month = "0" + month;
		}
		if (Integer.parseInt(getDay()) < 10) {
			day = "0" + day;
		}
		System.out.println(getYear() + getMonth() + getDay());
		webContext.getWebToc().destroy();

		String addLayerPath = "D:\\testData\\" + getYear() + getMonth()
				+ getDay() + "\\本站气压等值线.shp";
		int addLayerIndex = 0;
		if (addedLayers.keySet().contains(addLayerPath)) {
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage(event.getComponent().getClientId(fc),
					new FacesMessage("LayerPath '" + addLayerPath
							+ "' has already been added to the map."));
			return;
		}
		processAddLayer(addLayerPath, addLayerIndex, true);
	}

	public void remove(TaskEvent event) throws AutomationException, IOException {
		WebContext webContext = event.getWebContext();
		MapLayerConfig config = new MapLayerConfig(webContext);
		config.removeLayer();
	}

	public HistoryReviewTaskInfo getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(HistoryReviewTaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esri.adf.web.data.WebContextObserver#update(com.esri.adf.web.data.WebContext,
	 *      java.lang.Object)
	 */
	public void update(WebContext webContext, Object obj) {
		System.out.println("update");
		webContext.refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esri.adf.web.data.WebContextInitialize#destroy()
	 */
	public void destroy() {
		if (localMapRes.getServerContext() == null)
			return;
		passivate();
		addedLayers = null;
		removeLayers = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esri.adf.web.data.WebContextInitialize#init(com.esri.adf.web.data.WebContext)
	 */
	public void init(WebContext webContext) {
		// webContext.addObserver(this);
		localMapRes = (AGSLocalMapResource) webContext.getResourceById("ags2");
		this.webContext = webContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esri.adf.web.data.WebLifecycle#activate()
	 */
	public void activate() {
		try {
			if (addedLayers == null || addedLayers.size() <= 0)
				return;
			for (Iterator<java.util.Map.Entry<String, Integer>> iter = addedLayers
					.entrySet().iterator(); iter.hasNext();) {
				java.util.Map.Entry<String, Integer> item = iter.next();
				processAddLayer(item.getKey(), item.getValue().intValue(),
						false);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.esri.adf.web.data.WebLifecycle#passivate()
	 */
	public void passivate() {
		try {
			// remove layer from the AO
			if (removeLayers.size() <= 0 || removeLayers == null)
				return;
			MapServer mapServer = localMapRes.getLocalMapServer();
			Map map = (Map) mapServer.getMap(mapServer.getDefaultMapName());

			for (Iterator<FeatureLayer> iter = removeLayers.iterator(); iter
					.hasNext();) {
				ILayer removeLayer = iter.next();
				map.deleteLayer(removeLayer);
			}
			mapServer.refreshServerObjects();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			removeLayers.clear();
		}
	}

	private void processAddLayer(String layerPath, int index,
			boolean firstTimeLayerAdd) {
		try {
			File file = new File(layerPath);

			ServerContext sc = new ServerContext(localMapRes.getServerContext());
			MapServer mapServer = localMapRes.getLocalMapServer();

			Map map = (Map) mapServer.getMap(mapServer.getDefaultMapName());
			FeatureLayer flayer = (FeatureLayer) sc.createObject(FeatureLayer
					.getClsid());
			ShapefileWorkspaceFactory factory = (ShapefileWorkspaceFactory) sc
					.createObject(ShapefileWorkspaceFactory.getClsid());
			Workspace ws = (Workspace) factory
					.openFromFile(file.getParent(), 0);
			flayer.setFeatureClassByRef(ws.openFeatureClass(file.getName()));
			// layer.setRendererByRef();
			System.out.println(file.getName());
			flayer.setName(year + month + day + file.getName());
			map.addLayer(flayer);
			map.moveLayer(flayer, index);

			removeLayers.add(flayer);

			// RasterLayer rlayer =
			// (RasterLayer)sc.createObject(RasterLayer.getClsid());

			refreshControls(file.getName(), firstTimeLayerAdd);

			if (!addedLayers.keySet().contains(layerPath)) {
				addedLayers.put(layerPath, Integer.valueOf(index));
				this.init(webContext);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("not found !");
		}
	}

	public void refreshControls(String layerName, boolean firstTimeLayerAdd) {
		try {
			// refresh the map server
			MapServer mapServer = localMapRes.getLocalMapServer();
			mapServer.refreshServerObjects();
			ServerContext sc = new ServerContext(localMapRes.getServerContext());

			// refresh the SOAP stubs with the changes from underlying
			// ArcObjects
			IMapServerInfo newmsi = mapServer.getServerInfo(mapServer
					.getDefaultMapName());
			com.esri.arcgis.carto.IMapDescription newmdesc = newmsi
					.getDefaultMapDescription();

			com.esri.arcgisws.MapServerInfo mapControlMSI = (com.esri.arcgisws.MapServerInfo) AGSUtil
					.createStubFromArcObject(newmsi,
							com.esri.arcgisws.MapServerInfo.class, sc);

			com.esri.arcgisws.MapDescription mapControlMD = (com.esri.arcgisws.MapDescription) AGSUtil
					.createStubFromArcObject(newmdesc,
							com.esri.arcgisws.MapDescription.class, sc);

			// Get the descriptions from the ArcObjects and convert them to stub
			// objects
			MapLayerInfo[] fromAOMapLyrInfos = mapControlMSI.getMapLayerInfos();
			LayerDescription[] fromAOLyrDescss = mapControlMD
					.getLayerDescriptions();

			// Get the current Status of the MapDescription at the web level
			AGSMapFunctionality mf = (AGSMapFunctionality) localMapRes
					.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
			MapServerInfo adfServerInfo = mf.getMapServerInfo();
			MapLayerInfo[] adfMapLyrInfos = adfServerInfo.getMapLayerInfos();
			LayerDescription[] adfLyrDescs = adfServerInfo
					.getDefaultMapDescription().getLayerDescriptions();

			// Create new descriptions based on the current web descriptions and
			// the AO descriptions so that we can
			// combine these two form one single set of descriptions that
			// reflects both underlying AO and web mapdescriptions.
			mf = (AGSMapFunctionality) localMapRes
					.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
			MapLayerInfo[] newAdfMapLyrInfos;
			LayerDescription[] newAdfLyrDescs;

			if (fromAOMapLyrInfos.length > adfMapLyrInfos.length) {
				newAdfMapLyrInfos = new MapLayerInfo[fromAOMapLyrInfos.length];
				newAdfLyrDescs = new LayerDescription[fromAOLyrDescss.length];
				for (int i = 0; i < newAdfMapLyrInfos.length; i++) {
					newAdfMapLyrInfos[i] = fromAOMapLyrInfos[i];
					newAdfLyrDescs[i] = fromAOLyrDescss[i];
				}
			} else {
				newAdfMapLyrInfos = new MapLayerInfo[adfMapLyrInfos.length];
				newAdfLyrDescs = new LayerDescription[adfLyrDescs.length];
				for (int i = 0; i < adfMapLyrInfos.length; i++) {
					newAdfMapLyrInfos[i] = adfMapLyrInfos[i];
					newAdfLyrDescs[i] = adfLyrDescs[i];
				}
			}

			for (int i = 0; i < newAdfMapLyrInfos.length; i++) {
				// First time when a layer is added.
				if (firstTimeLayerAdd
						&& newAdfMapLyrInfos[i].getName().equals(layerName)) {
					newAdfMapLyrInfos[i] = fromAOMapLyrInfos[i];
					newAdfLyrDescs[i] = fromAOLyrDescss[i];
				}
				// For all other requests
				else {
					for (int j = 0; j < adfMapLyrInfos.length; j++) {
						if (newAdfMapLyrInfos[i].getName().equals(
								adfMapLyrInfos[j].getName())) {
							newAdfMapLyrInfos[i] = adfMapLyrInfos[j];
							newAdfLyrDescs[i] = adfLyrDescs[j];

							// Assigning the layerId from the latest AO so that
							// the web layer ids and the AO ids match
							if (i < fromAOMapLyrInfos.length) {
								newAdfMapLyrInfos[i]
										.setLayerID(fromAOMapLyrInfos[i]
												.getLayerID());
								newAdfLyrDescs[i].setLayerID(fromAOLyrDescss[i]
										.getLayerID());
							}

						}
					}
				}
			}

			// refresh the mf with new values
			mf = (AGSMapFunctionality) localMapRes
					.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
			mf.getMapServerInfo().setMapLayerInfos(newAdfMapLyrInfos);
			mf.getMapServerInfo().getDefaultMapDescription()
					.setLayerDescriptions(newAdfLyrDescs);

			// refresh overview control.
			WebOverview webOv = webContext.getWebOverview();
			if (webOv != null) {
				AGSOverviewFunctionality of = (AGSOverviewFunctionality) localMapRes.getFunctionality(OverviewFunctionality.FUNCTIONALITY_NAME);
				of.initFunctionality(localMapRes);
				webOv.exportImage();
			}

			WebToc newToc = new WebToc();
			newToc.init(webContext);
			webContext.getWebToc().destroy();
			newToc.setExpandLevel(0);
			webContext.setWebToc(newToc);
			// webContext.getWebToc().setExpandLevel(0);

			webContext.refresh();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
