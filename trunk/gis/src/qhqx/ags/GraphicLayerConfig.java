/**
 * 
 */
package qhqx.ags;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import qhqx.db.AttributeOfStn;

import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.geometry.WebPoint;
import com.esri.adf.web.data.graphics.GraphicFeature;
import com.esri.adf.web.data.graphics.GraphicsLayer;
import com.esri.adf.web.data.graphics.GraphicsResource;
import com.esri.adf.web.data.renderer.WebUniqueValueInfo;
import com.esri.adf.web.data.renderer.WebUniqueValueRenderer;
import com.esri.adf.web.data.symbol.WebSimpleMarkerSymbol;
import com.esri.adf.web.data.symbol.WebTrueTypeMarkerSymbol;

/**
 * @author yan
 *
 */
public class GraphicLayerConfig {
	private GraphicsResource graphicsResource = null;
	
	public GraphicLayerConfig(WebContext webContex){
		this.graphicsResource = (GraphicsResource)webContex.getResourceById("graphicsResource");//getDefaultResource
		this.graphicsResource.removeAllGraphicsLayer();
		
	}
	
	public void addGraphicLayerByPid(String pid) throws SQLException{
		
		GraphicsLayer gl = new GraphicsLayer();
		GraphicsLayer gl2 = new GraphicsLayer();
		GraphicsLayer gl3 = new GraphicsLayer();
		
		WebSimpleMarkerSymbol defaultMarkerSymbol = new WebSimpleMarkerSymbol();
	    defaultMarkerSymbol.setColor("0,0,255");
	    defaultMarkerSymbol.setWidth(0);
	    defaultMarkerSymbol.setMarkerType(WebSimpleMarkerSymbol.CIRCLE);
		
		AttributeOfStn stn = new AttributeOfStn();
		ResultSet rs = stn.getStnNameByPid(pid);
		
		ArrayList<WebUniqueValueInfo> infos = new ArrayList<WebUniqueValueInfo>();
		WebUniqueValueInfo uvi;
		
		ArrayList<WebUniqueValueInfo> infos2 = new ArrayList<WebUniqueValueInfo>();
		WebUniqueValueInfo uvi2;
		
		ArrayList<WebUniqueValueInfo> infos3 = new ArrayList<WebUniqueValueInfo>();
		WebUniqueValueInfo uvi3;
		while(rs.next()){
			String stnName = rs.getString(3).trim();
			String zvalue1 = Double.toString(rs.getDouble(4));
			String stnNum = rs.getString(5).trim();
			
			Map<String, String> attributes = new LinkedHashMap<String, String>();
			attributes.put("STN_NAME", stnName);
			attributes.put("ZVALUE1", zvalue1);
			attributes.put("STN_NUM", stnNum);
			WebPoint point = new WebPoint(rs.getDouble(1), rs.getDouble(2));
			
			GraphicFeature gf = new GraphicFeature();
			gf.setAttributes(attributes);
			gf.setGeometry(point);
			
			gl.addGraphicFeature(gf);
			gl2.addGraphicFeature(gf);
			gl3.addGraphicFeature(gf);
			
			uvi = new WebUniqueValueInfo(stnName, " ", stnName, this.createLabelSymbol(stnName, WebTrueTypeMarkerSymbol.BOTTOM));
			infos.add(uvi);
			uvi2 = new WebUniqueValueInfo(zvalue1, " ", zvalue1, this.createLabelSymbol(zvalue1, WebTrueTypeMarkerSymbol.TOP));
			infos2.add(uvi2);
			uvi3 = new WebUniqueValueInfo(stnNum, " ", stnNum, this.createLabelSymbol(stnNum, WebTrueTypeMarkerSymbol.LEFT));
			infos3.add(uvi3);
		}
		WebUniqueValueRenderer uniqueRender3 = new WebUniqueValueRenderer();
		uniqueRender3.setDefaultLabel(" ");
		uniqueRender3.setDefaultSymbol(defaultMarkerSymbol);
		uniqueRender3.setField1("STN_NUM");
		uniqueRender3.setField2(null);
		uniqueRender3.setField3(null);
		uniqueRender3.setFieldDelimiter(null);
		uniqueRender3.setUniqueValueInfos(infos3);
		gl3.setName("显示站号");
		gl3.setRenderer(uniqueRender3);
		gl3.setVisible(false);
		graphicsResource.addGraphicsLayer(gl3);
		
		WebUniqueValueRenderer uniqueRender = new WebUniqueValueRenderer();
		uniqueRender.setDefaultLabel(" ");
		uniqueRender.setDefaultSymbol(defaultMarkerSymbol);
		uniqueRender.setField1("STN_NAME");
		uniqueRender.setField2(null);
		uniqueRender.setField3(null);
		uniqueRender.setFieldDelimiter(null);
		uniqueRender.setUniqueValueInfos(infos);
		gl.setName("显示站名");
		gl.setRenderer(uniqueRender);
		graphicsResource.addGraphicsLayer(gl);
		
		WebUniqueValueRenderer uniqueRender2 = new WebUniqueValueRenderer();	
		uniqueRender2.setDefaultLabel(" ");
		uniqueRender2.setDefaultSymbol(defaultMarkerSymbol);
		uniqueRender2.setField1("ZVALUE1");
		uniqueRender2.setField2(null);
		uniqueRender2.setField3(null);
		uniqueRender2.setFieldDelimiter(null);
		uniqueRender2.setUniqueValueInfos(infos2);
		gl2.setName("测量值");
		gl2.setRenderer(uniqueRender2);
		graphicsResource.addGraphicsLayer(gl2);
		
		/*WebRenderer wr = uniqueRender;
		GraphicRenderer gRender = new GraphicRenderer();
		gRender.setGeometries(geometries);
		gRender.setRenderer(wr);
		gRender.setValues(values);
		webContex.getWebGraphics().addRenderer(gRender);*/
	}
	
	public void addGraphicLayer(){
		try {
			this.addGraphicLayerByPid("aaa");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	private WebTrueTypeMarkerSymbol createLabelSymbol(String text, int pos) {
		WebTrueTypeMarkerSymbol charSymbol = new WebTrueTypeMarkerSymbol();
		charSymbol.addTextValue(text);
		charSymbol.setFontName("宋体");
		charSymbol.setFontSize(11);
		charSymbol.setFontColor("0,100,0");
		charSymbol.setFontStyle(WebTrueTypeMarkerSymbol.ITALIC);
		charSymbol.setTextPosition(pos);
		return charSymbol;
	}
	
	private WebTrueTypeMarkerSymbol createLabelSymbolWithoutFont(String text, int pos) {
		WebTrueTypeMarkerSymbol charSymbol = new WebTrueTypeMarkerSymbol();
		charSymbol.addTextValue(text);
		charSymbol.setFontSize(10);
		charSymbol.setFontStyle(WebTrueTypeMarkerSymbol.ITALIC);
		charSymbol.setTextPosition(pos);
		return charSymbol;
	}
	
	@SuppressWarnings("unused")
	private WebSimpleMarkerSymbol createMarkerSymbol(){
		WebSimpleMarkerSymbol simpleSymbol = new WebSimpleMarkerSymbol();
		simpleSymbol.setMarkerType(WebSimpleMarkerSymbol.CIRCLE);
		simpleSymbol.setColor("255,0,0");
		simpleSymbol.setWidth(20);
		return simpleSymbol;
		
	}
	
}
