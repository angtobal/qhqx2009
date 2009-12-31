/**
 * 
 */
package qhqx.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

import qhqx.ags.CustomMapLegend;
import qinghai.qixiang.winddirectionandspeed.WindDirectionSpeed;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.adf.web.ags.data.AGSMapFunctionality;
import com.esri.adf.web.data.GraphicElement;
import com.esri.adf.web.data.MapFunctionality;
import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.geometry.WebPoint;
import com.esri.adf.web.data.symbol.WebTrueTypeMarkerSymbol;
import com.esri.arcgis.interop.AutomationException;

/**
 * @author yan
 *
 */
public class RealTimeContour extends GPServerInfo implements IServerTask{
	/**
	 * 
	 */
	private static final String MODEL_NAME = "servertask";

	private String mapEndpoint = null;
	
	private String localMapResID = null;
	
	private String picHead = null;
	
	private String lgdurl = null;
	
	
	public RealTimeContour(WebContext webContext){
		super(webContext);
	}
	
	public void gpResultDisplay(){
		
		//AGSLocalMapResource localResource = (AGSLocalMapResource) webContext.getResourceById(localMapResID);
		//MapServer localMapServer = localResource.getLocalMapServer();
		try {
			localMapServer.refreshServerObjects();
		} catch (AutomationException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			try {
				this.generateContout(MODEL_NAME);
				mapName = localMapServer.getDefaultMapName();//AutomationException
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (AutomationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			changeRasterLyrRender();
			sortLayers();
			changeADFLyrSourceByID();
		} catch (AutomationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		makeUpMap();
		refreshWebOverview();
		freeResource();
		
	}

	/**
	 * 
	 */
	private void makeUpMap() {
		CustomMapLegend mapLgd;
		try {
			mapLgd = new CustomMapLegend(localResource);
			lgdurl = mapLgd.printLegendWithUrl();
		} catch (AutomationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		WebTrueTypeMarkerSymbol label = new WebTrueTypeMarkerSymbol();
		label.setFontName("ËÎÌå");
		label.addTextValue(this.picHead + " ");// + WeatherRenderInfo.getChName(featureName));
		GraphicElement ge = new GraphicElement();
		//ge.setGeometry(new WebPoint(webContext.getWebMap().getCurrentExtent().getMinX() + 1, webContext.getWebMap().getCurrentExtent().getMaxY() - 1));
		ge.setGeometry(new WebPoint(96, 40));
		ge.setSymbol(label);
		webContext.getWebGraphics().addGraphics(ge);
		
		/*try{
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
		}*/
		
		//MapImage img1 = (MapImage)webContext.getWebMap().exportMapImage();
		
		if(featureName.equals("fengsu") || featureName.equals("fengxiang")){
			WindDirectionSpeed wind = new WindDirectionSpeed();
			wind.setGContext(webContext);
			wind.setPid(pid);
			wind.addWindSymbol();
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