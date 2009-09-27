/**
 * 
 */
package qhqx.ags;

import java.io.IOException;

import com.esri.adf.web.ags.data.AGSLocalMapResource;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.ILegend;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.carto.Legend;
import com.esri.arcgis.carto.MapServer;
import com.esri.arcgis.carto.VerticalLegendItem;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.IServerContext;

/**
 * @author yan
 *
 */
public class LegendInfo {

	/**
	 * 
	 */
	protected IServerContext serverContext;
	protected MapServer mapServer;
	protected IMap focusMap;
	private ILayer layer;
	protected ILegend legend;

	/**
	 * @throws IOException 
	 * @throws AutomationException 
	 * 
	 */
	public LegendInfo(AGSLocalMapResource localResource) throws AutomationException, IOException {
		super();
		serverContext = localResource.getServerContext();
		mapServer = localResource.getLocalMapServer();
		focusMap = mapServer.getMap(mapServer.getDefaultMapName());
		System.out.println(mapServer.getDefaultMapName());
		legend = (ILegend) serverContext.createObject(Legend.getClsid());
	}

	public void generateLegendFromLayer() throws AutomationException,
			IOException {
				legend.clearItems();
			
				VerticalLegendItem vLegendItem = (VerticalLegendItem) serverContext.createObject(VerticalLegendItem.getClsid());
				/*HorizontalLegendItem hLegendItem = (HorizontalLegendItem) serverContext
						.createObject(HorizontalLegendItem.getClsid());
				layer = mapServer.getLayer(mapServer.getDefaultMapName(), 2);
				hLegendItem.setLayerByRef(layer);*/
				layer = mapServer.getLayer(mapServer.getDefaultMapName(), 3);
				vLegendItem.setLayerByRef(layer);
				legend.addItem(vLegendItem);
				
				legend.setName("legend");
				legend.setTitle("Í¼Àý");
				//customLegendStyle();
			
			}

	public void generateLegendFromMap() throws AutomationException,
			IOException {
				legend.clearItems();
				for (int i = 0; i < focusMap.getMapSurroundCount(); i++) {
					if (focusMap.getMapSurround(i) instanceof com.esri.arcgis.carto.Legend) {
						legend = (ILegend) focusMap.getMapSurround(i);
					}
				}
				
				legend.setName("legend");
				legend.setTitle("Í¼Àý");
				// legend.removeItem(arg0);
			}

}