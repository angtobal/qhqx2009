/**
 * 
 */
package qhqx.task;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import qhqx.ags.CustomMapLegend;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.adf.web.ags.data.AGSMapFunctionality;
import com.esri.adf.web.data.MapFunctionality;
import com.esri.adf.web.data.WebContext;
import com.esri.arcgis.carto.IGraphicsContainer;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.carto.TextElement;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgisws.EsriImageFormat;
import com.esri.arcgisws.EsriImageReturnType;
import com.esri.arcgisws.ImageDescription;
import com.esri.arcgisws.ImageDisplay;
import com.esri.arcgisws.MapImage;

/**
 * @author yan
 *
 */
public class PictureBuilder extends GPServerInfo implements IServerTask{

	private static final String MODEL_NAME = "servertask";

	private String mapEndpoint = null;
	
	private String localMapResID = null;
	
	private String fileName = null;
	private String pictureHead;
	//AGSLocalMapResource localResource;
	//AGSMapFunctionality mapFunc;
	/**
	 * @param webContext
	 */
	public PictureBuilder(WebContext webContext) {
		super(webContext);
	}
	
	
	public void gpResultDisplay(){
		
		try {
			localMapServer.refreshServerObjects();
			mapName = localMapServer.getDefaultMapName();
		} catch (AutomationException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
				
		try {
			this.generateContout(MODEL_NAME);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
			//freeResource();
			//return;
		}	
		
		try {
			System.out.println("sort");
			//this.changeRasterLyrRender();
			sortLayers();
			System.out.println("sourceID");
			this.changeADFLyrSourceByID();
			System.out.println("overview");
			refreshWebOverview();
			this.exportImage(makeUpSurround(mapName));
		} catch (AutomationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} /*finally{*/
			freeResource();
		//}
		
	}

	/**
	 * @param container
	 * @throws FileNotFoundException
	 * @throws RemoteException
	 * @throws IOException
	 * @throws AutomationException
	 */
	private void exportImage(IGraphicsContainer container)
			throws FileNotFoundException, RemoteException, IOException,
			AutomationException {
		FileOutputStream fileOutStream = new FileOutputStream("c:\\sharedata\\" + this.fileName + ".jpg");
		
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
		
		MapImage mapim = localResource.getMapServer().exportMapImage(mapFunc.getMapDescription(), idesc);
		fileOutStream.write(mapim.getImageData());
		fileOutStream.close();
		System.out.println("目标文件生成 " + this.fileName);
		
		mapFunc.getLayerDescriptions()[1].setVisible(true);
		container.deleteAllElements();
	}


	/**
	 * @param mapName
	 * @return
	 * @throws IOException
	 * @throws AutomationException
	 */
	private IGraphicsContainer makeUpSurround(String mapName)
			throws IOException, AutomationException {
		//以下可以改到ImageExporter中去，结合更紧密
		
		//MapServer mapServer = (MapServer) serverContext.getServerObject();
		MapServer mapServer = localMapServer;
		//IMap focusMap = mapServer.getMap(mapServer.getDefaultMapName());
		IMap focusMap = mapServer.getMap(mapName);
		IGraphicsContainer container = (IGraphicsContainer)focusMap;
		
		TextElement textElement = (TextElement)serverContext.createObject(TextElement.getClsid());
		Point point = (Point)serverContext.createObject(Point.getClsid());
		point.setX(96);
		point.setY(40);
		textElement.setGeometry(point);
		textElement.setFontName("宋体");
		textElement.setSize(10);
		textElement.setText("         " + pictureHead);// + "  " +WeatherRenderInfo.getChName(featureName));
		container.addElement(textElement, 0);
		/*System.out.println(textElement.getSize());
		System.out.println(textElement.getCharacterWidth());*/
		
		CustomMapLegend mapLegend = new CustomMapLegend(localResource);
		mapLegend.attachLegendToFocusMap();
		return container;
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
		/*String str = null;
		if(pictureHead != null){
			System.out.println(pictureHead.toString());
			try {
				byte[] temp = pictureHead.toString().getBytes("ISO-8859-1");
				str = new String(temp, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		
		this.pictureHead = str;*/
	}


	/* (non-Javadoc)
	 * @see qhqx.task.IServerTask#setPicHead(java.lang.String)
	 */
	public void setPicHead(String pictureHead) {
		this.pictureHead = pictureHead;
	}

}
