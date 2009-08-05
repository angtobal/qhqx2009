/**
 * 
 */
package qhqx.task;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.esri.adf.web.ags.util.AGSUtil;
import com.esri.adf.web.data.GraphicRenderer;
import com.esri.adf.web.data.geometry.WebGeometry;
import com.esri.adf.web.data.renderer.WebRenderer;
import com.esri.adf.web.data.symbol.WebSimpleLineSymbol;
import com.esri.arcgisws.FeatureLayerDrawingDescription;
import com.esri.arcgisws.FeatureRenderer;
import com.esri.arcgisws.Field;
import com.esri.arcgisws.GPFeatureRecordSetLayer;
import com.esri.arcgisws.GPResult;
import com.esri.arcgisws.Geometry;
import com.esri.arcgisws.MapDescription;
import com.esri.arcgisws.MapServerBindingStub;
import com.esri.arcgisws.MapServerInfo;
import com.esri.arcgisws.Record;
import com.esri.arcgisws.RecordSet;

/**
 * @author yan
 *
 */
public class GPResultHandler {
	private GPResult result;
	private GPFeatureRecordSetLayer gpOutputRecordSet;
	private RecordSet recordSet;
	private int shapeField = -1;
	private GraphicRenderer graphicRenderer;
	private String mapEndpoint = null;
	
	public void gpResultDisplay() throws MalformedURLException, RemoteException{
		mapEndpoint = "http://yan:8399/arcgis/services/test/MapServer";
		MapServerBindingStub mapServer = new MapServerBindingStub(new java.net.URL(mapEndpoint), null);
		
		String dataframe = mapServer.getDefaultMapName();
		MapServerInfo mapServerInfo = mapServer.getServerInfo(dataframe);
		@SuppressWarnings("unused")
		MapDescription mapDescription = mapServerInfo.getDefaultMapDescription();
		
		
	}
	
	public void geometry2GraphicRender(){
		gpOutputRecordSet = (GPFeatureRecordSetLayer)result.getValues()[0];
		recordSet = gpOutputRecordSet.getRecordSet();
		System.out.println(result.toString());
		
		FeatureLayerDrawingDescription featureLyrDrawingDesc = (FeatureLayerDrawingDescription) gpOutputRecordSet.getLayerDrawingDescription();
		FeatureRenderer featureRender = featureLyrDrawingDesc.getFeatureRenderer();
		
		for(int i = 0; i < recordSet.getFields().getFieldArray().length; i++){
			Field recordsField = recordSet.getFields().getFieldArray()[i];
			System.out.println(gpOutputRecordSet.getShapeFieldName() + " :  " + recordsField.getName());
			if(recordsField.getName().equalsIgnoreCase("Shape")){
				System.out.println(gpOutputRecordSet.getShapeFieldName());
				shapeField = i;
				System.out.println(shapeField);
			}
		}
		
		List<WebGeometry> webGeometryList = new ArrayList<WebGeometry>();
		for(int i = 0; i < recordSet.getRecords().length; i++){
			Record record = recordSet.getRecords()[i];
			Geometry geom = (Geometry) record.getValues()[shapeField];
			//webGeometryList.add(AGSUtil.fromAGSGeometry((Geometry)record.getValues()[shapeField]));
			//String data = AGSUtil.serializeStub(geom);
			webGeometryList.add(AGSUtil.fromAGSGeometry(geom));
		}
		
		
		WebSimpleLineSymbol symbol = new WebSimpleLineSymbol();
		symbol.setLineType(WebSimpleLineSymbol.SOLID);
		symbol.setColor("255,0,0");
		symbol.setWidth(2);
		WebRenderer webRender = AGSUtil.fromAGSRenderer(featureRender, symbol);
		
		graphicRenderer = new GraphicRenderer();
		graphicRenderer.setRenderer(webRender);
		graphicRenderer.setGeometries(webGeometryList);
	}

	public GPResult getResult() {
		return result;
	}

	public void setResult(GPResult result) {
		this.result = result;
	}

	public GraphicRenderer getGraphicRenderer() {
		return graphicRenderer;
	}

	public void setGraphicRenderer(GraphicRenderer graphicRenderer) {
		this.graphicRenderer = graphicRenderer;
	}

	public String getMapEndpoint() {
		return mapEndpoint;
	}

	public void setMapEndpoint(String mapEndpoint) {
		this.mapEndpoint = mapEndpoint;
	}
}
