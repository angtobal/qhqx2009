/**
 * 
 */
package qhqx.task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import qhqx.ags.AgsObjAccess;
import qhqx.server.GISResourceManager;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.adf.web.ags.data.AGSMapFunctionality;
import com.esri.adf.web.ags.data.AGSOverviewFunctionality;
import com.esri.adf.web.data.MapFunctionality;
import com.esri.adf.web.data.OverviewFunctionality;
import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.WebOverview;
import com.esri.adf.web.data.geometry.WebExtent;
import com.esri.arcgis.carto.IRasterLayer;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.IServerContext;
import com.esri.arcgis.server.IServerObjectAdmin;
import com.esri.arcgisws.EsriJobStatus;
import com.esri.arcgisws.GPDouble;
import com.esri.arcgisws.GPResult;
import com.esri.arcgisws.GPResultOptions;
import com.esri.arcgisws.GPServerBindingStub;
import com.esri.arcgisws.GPString;
import com.esri.arcgisws.GPToolInfo;
import com.esri.arcgisws.GPValue;

/**
 * @author yan
 * 
 */
public class GPServerInfo {

	private String gpEndpoint = null;
	protected WebContext webContext = null;// face config
	private GPToolInfo toolInfo = null;
	private GPResult result = null;
	private GPResultOptions resultOpt = null;
	protected String pid = null;
	private String base = null;
	private String interval = null;
	protected String JobID = null;
	protected String featureName = null;
	protected AGSLocalMapResource localResource;
	protected MapServer localMapServer;
	protected IServerContext serverContext;
	protected AGSMapFunctionality mapFunc;
	protected String mapName;

	/**
	 * 
	 */
	protected GPServerInfo(WebContext webContext) {
		super();

		this.webContext = webContext;

		try{
			localResource = (AGSLocalMapResource) this.webContext.getResourceById("ags1");

			localMapServer = localResource.getLocalMapServer();
			serverContext = localResource.getServerContext();
			mapFunc = (AGSMapFunctionality) localResource
				.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
		}catch(NullPointerException e){
			e.printStackTrace();
			GISResourceManager grm = GISResourceManager.getInstance();
			//grm.restartGPService();
			grm.restartMapService();
			grm.notifyAll();
			System.out.println("arcgis service restarted...");
		}
	}

	@SuppressWarnings("static-access")
	public void generateContout(WebContext webContext, String modelName)
			throws MalformedURLException, RemoteException, InterruptedException {
		GPString queryString = new GPString();
		queryString.setValue("PID=\'" + pid + "\'");
		// queryString.setValue("\"F10\"=\'" + pid + "\'");

		GPDouble baseContour = new GPDouble();
		baseContour.setValue(Double.parseDouble(base));

		GPDouble contourInterval = new GPDouble();
		contourInterval.setValue(Double.parseDouble(interval));

		GPValue[] gpValues = new GPValue[3];
		gpValues[0] = contourInterval;
		gpValues[1] = baseContour;
		gpValues[2] = queryString;

		GPServerBindingStub gpServer = new GPServerBindingStub(
				new java.net.URL(gpEndpoint), null);
		toolInfo = gpServer.getToolInfo(modelName);
		resultOpt = new GPResultOptions();
		resultOpt.setDensifyFeatures(true);

		JobID = gpServer.submitJob(toolInfo.getName(), gpValues, resultOpt,
				null);
		System.out.println("JobID:" + JobID);
		EsriJobStatus gpJobStatus = EsriJobStatus.esriJobSubmitted;
		int waitCount = 0;
		int processCount = 0;
		// Check the status of the job; if it's finished and successful, proceed
		// - false delay.
		while (gpJobStatus != EsriJobStatus.esriJobSucceeded
				&& gpJobStatus != EsriJobStatus.esriJobFailed) {
			Thread.currentThread().sleep(3000);

			gpJobStatus = gpServer.getJobStatus(JobID);
			System.out.println(gpJobStatus.toString() + ": " + JobID.toString());

			if (gpJobStatus == EsriJobStatus.esriJobWaiting) {
				waitCount += 1;
			} else if (gpJobStatus == EsriJobStatus.esriJobExecuting) {
				processCount += 1;
			}
			if (waitCount > 40 || processCount > 40) {
				gpServer.cancelJob(JobID);
				System.out.println(gpServer.getJobStatus(JobID));
				break;
			}
		}
		if(gpJobStatus == EsriJobStatus.esriJobFailed && waitCount < 2){
			GISResourceManager.gpFailedTimes += 1;
		}

		if (gpServer.getResultMapServerName() != null) {
			// System.out.println(gpServer.getResultMapServerName().toString() +
			// "aaa");
			// result = gpServer.getJobResult(JobID, null, resultOpt);
		}

	}

	public void generateContout(String modelName) throws MalformedURLException,
			RemoteException, InterruptedException {
		GPString queryString = new GPString();
		queryString.setValue("PID=\'" + pid + "\'");
		// queryString.setValue("\"F10\"=\'" + pid + "\'");

		GPDouble baseContour = new GPDouble();
		baseContour.setValue(Double.parseDouble(base));

		GPDouble contourInterval = new GPDouble();
		contourInterval.setValue(Double.parseDouble(interval));

		GPValue[] gpValues = new GPValue[3];
		gpValues[0] = contourInterval;
		gpValues[1] = baseContour;
		gpValues[2] = queryString;

		GPServerBindingStub gpServer = new GPServerBindingStub(
				new java.net.URL(gpEndpoint), null);
		toolInfo = gpServer.getToolInfo(modelName);//问题
		resultOpt = new GPResultOptions();
		resultOpt.setDensifyFeatures(true);

		JobID = gpServer.submitJob(toolInfo.getName(), gpValues, resultOpt,
				null);
		/*System.out.println(JobID);
		// Check the status of the job; if it's finished and successful, proceed
		// - false delay.
		while (gpServer.getJobStatus(JobID) != EsriJobStatus.esriJobSucceeded
				&& gpServer.getJobStatus(JobID) != EsriJobStatus.esriJobFailed) {
			Thread.sleep(3000);
			System.out.println(gpServer.getJobStatus(JobID).toString());
		}

		if (gpServer.getResultMapServerName() != null) {
			// System.out.println(gpServer.getResultMapServerName().toString() +
			// "aaa");
			// result = gpServer.getJobResult(JobID, null, resultOpt);
		}
		System.out.println("JobID:" + JobID);
		EsriJobStatus gpJobStatus = EsriJobStatus.esriJobSubmitted;
		int waitCount = 0;
		int processCount = 0;*/
		// Check the status of the job; if it's finished and successful, proceed
		// - false delay.
		System.out.println("JobID:" + JobID);
		EsriJobStatus gpJobStatus = EsriJobStatus.esriJobSubmitted;
		int waitCount = 0;
		int processCount = 0;
		while (gpJobStatus != EsriJobStatus.esriJobSucceeded
				&& gpJobStatus != EsriJobStatus.esriJobFailed) {
			Thread.currentThread().sleep(3000);

			gpJobStatus = gpServer.getJobStatus(JobID);
			System.out.println(gpJobStatus.toString() + ": " + JobID.toString());

			if (gpJobStatus == EsriJobStatus.esriJobWaiting) {
				waitCount += 1;
			} else if (gpJobStatus == EsriJobStatus.esriJobExecuting) {
				processCount += 1;
			}
			if (waitCount > 40 || processCount > 40) {
				gpServer.cancelJob(JobID);
				System.out.println(gpServer.getJobStatus(JobID));
				break;
			}
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

	@SuppressWarnings("deprecation")
	protected void changeRasterLyrRender() throws AutomationException,
			IOException {

		IRasterLayer rasterLayer = (IRasterLayer) localMapServer.getLayer(
				mapName, 6);
		CustomRasterRender render = new CustomRasterRender();
		render.setPid(pid);
		if (this.featureName != null) {
			render.setFeatureName(featureName);
		} else {
			render.setFeatureName("common");
			System.out.println("featureName is null");
		}
		// IServerContext serverContext = localResource.getServerContext();

		render.setRasterLayer(rasterLayer);
		render.renderByRasterClassify(serverContext);
		localMapServer.getMap(mapName).deleteLayer(rasterLayer);
		rasterLayer = render.rasterLayer;
		localMapServer.getMap(mapName).addLayer(rasterLayer);
		localMapServer.getMap(mapName).moveLayer(rasterLayer, 7);
			
		// IGraphicsContainer container = makeUpSurround(mapName);

		/*
		 * PngPictureElement pngPicElem = (PngPictureElement)
		 * serverContext.createObject(PngPictureElement.getClsid()); try{
		 * pngPicElem.importPictureFromFile("c:\\pic\\feature\\" +
		 * WeatherRenderInfo.getName(this.featureName) + ".jpg"); IEnvelopeGEN
		 * env = (IEnvelopeGEN) serverContext.createObject(Envelope.getClsid());
		 * env.putCoords(103.5, 31.5, 104.5, 38.5);
		 * pngPicElem.setGeometry((IGeometry) env);
		 * container.addElement(pngPicElem, 0); }catch(NullPointerException
		 * err){ System.out.println("所选要素图例不存在 " + this.featureName); }
		 */

		/*
		 * try{ java.net.URL url = new
		 * java.net.URL("http://222.18.139.70:8080/GisStutsPic_auto_SavePic.action?FILENAME=" +
		 * this.fileName + ".JPG"); @SuppressWarnings("unused") URLConnection
		 * urlConn = url.openConnection(); //urlConn.connect(); }catch(Exception
		 * err){ System.out.println(err.getMessage()); }
		 */

	}

	protected void changeADFLyrSourceByID() throws AutomationException,
			IOException {
		// changeRasterLyrRender();
		for (int i = 4; i < 4 + 3; i++) {
			/*if (i == 1 || i == 2) {*/
				mapFunc.getLayerDescriptions()[i].setSourceID(JobID);
				mapFunc.getLayerDescriptions()[i].setVisible(true);
			/*}
			// 替换图层
			if (i == 3+3) {

				mapFunc.getLayerDescriptions()[i].setSourceID(JobID);
				mapFunc.getLayerDescriptions()[i].setVisible(true);
			}*/
		}
		//mapFunc.getLayerDescriptions()[1 + 3].setVisible(true);
		mapFunc.setCurrentExtent(new WebExtent(89.400228, 31.542524, 104.9,
				40.339596));
		// mapFunc.getLayerInfos()[6].setMinScale(0);
	}

	/**
	 * 
	 */
	protected void refreshWebOverview() {
		WebOverview webOv = webContext.getWebOverview();
		if (webOv != null) {
			AGSOverviewFunctionality of = (AGSOverviewFunctionality) localResource
					.getFunctionality(OverviewFunctionality.FUNCTIONALITY_NAME);
			of.initFunctionality(localResource);
			//webOv.setDrawExtent(new WebExtent(90,30,100,35));
			webOv.setShowFullExtent(false);
			webOv.exportImage();
		}
	}

	/**
	 * @throws AutomationException
	 * @throws IOException
	 */
	protected void sortLayers() throws AutomationException, IOException {
		AgsObjAccess access = new AgsObjAccess(localResource
				.getLocalMapServer(), serverContext);
		// serverContext.releaseContext();

		// AGSMapFunctionality mapFunc = (AGSMapFunctionality)
		// localResource.getFunctionality(MapFunctionality.FUNCTIONALITY_NAME);
		mapFunc.getMapServerInfo().setMapLayerInfos(
				access.getFromAOMapLyrInfos());
		mapFunc.getMapServerInfo().getDefaultMapDescription()
				.setLayerDescriptions(access.getFromAOMapLyrDescs());
		mapFunc.getMapServerInfo().getMapLayerInfos()[0 + 3]
				.setSubLayerIDs(new int[] { 4, 5, 6 });
		mapFunc.getMapServerInfo().getMapLayerInfos()[6]
				.setParentLayerID(3);
		mapFunc.getMapServerInfo().getMapLayerInfos()[3].setName("动态图层");
	}

	protected void freeResource() {
		try {
			serverContext.removeAll();
			serverContext.releaseContext();
			System.out.println("resouce released");
		} catch (AutomationException e) {
			e.printStackTrace();
			System.out.println("not released:" + pid + ":" + featureName);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("not released:" + pid + ":" + featureName);
		}
	}

}