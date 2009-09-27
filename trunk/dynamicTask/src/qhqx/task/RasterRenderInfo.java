/**
 * 
 */
package qhqx.task;

import com.esri.arcgis.carto.IRasterLayer;
import com.esri.arcgis.carto.IRasterRenderer;
import com.esri.arcgis.display.IColor;
import com.esri.arcgis.geodatabase.IRaster;

/**
 * @author Administrator
 *
 */
public class RasterRenderInfo {

	protected IRaster raster = null;
	protected IRasterRenderer rasterRenderer = null;
	protected IColor color = null;
	protected IRasterLayer rasterLayer = null;
	protected String featureName;

	/**
	 * 
	 */
	public RasterRenderInfo() {
		super();
	}

	public IRasterLayer getRasterLayer() {
		return rasterLayer;
	}

	public void setRasterLayer(IRasterLayer rasterLayer) {
		this.rasterLayer = rasterLayer;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

}