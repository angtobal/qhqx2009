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
		
		WebSimpleMarkerSymbol defaultMarkerSymbol = new WebSimpleMarkerSymbol();
	    defaultMarkerSymbol.setColor("0,0,255");
	    defaultMarkerSymbol.setWidth(12);
	    defaultMarkerSymbol.setMarkerType(WebSimpleMarkerSymbol.CIRCLE);
		
		AttributeOfStn stn = new AttributeOfStn();
		ResultSet rs = stn.getStnNameByPid(pid);
		ArrayList<WebUniqueValueInfo> infos = new ArrayList<WebUniqueValueInfo>();
		WebUniqueValueInfo uvi;
		while(rs.next()){
			String stnName = rs.getString(3).trim();
			
			Map<String, String> attributes = new LinkedHashMap<String, String>();
			attributes.put("STN_NAME", stnName);
			WebPoint point = new WebPoint(rs.getDouble(1), rs.getDouble(2));
			
			GraphicFeature gf = new GraphicFeature();
			gf.setAttributes(attributes);
			gf.setGeometry(point);
			
			gl.addGraphicFeature(gf);
			
			uvi = new WebUniqueValueInfo(stnName, " ", stnName, this.createLabelSymbol(stnName));
			infos.add(uvi);
			
		}
		
		
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
	private WebTrueTypeMarkerSymbol createLabelSymbol(String text) {
		WebTrueTypeMarkerSymbol charSymbol = new WebTrueTypeMarkerSymbol();
		charSymbol.addTextValue(text);
		charSymbol.setFontName("新宋体");
		charSymbol.setFontSize(12);
		charSymbol.setTextPosition(WebTrueTypeMarkerSymbol.LEFT);
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
