/**
 * 渲染raster方式
 * .stretch颜色坡度渲染raster
 * .RGB渲染raster
 * .分级渲染raster
 * .唯一值渲染raster
 */
package qhqx.task;

import java.io.IOException;

import com.esri.arcgis.carto.IRasterClassifyColorRampRenderer;
import com.esri.arcgis.carto.IRasterLayer;
import com.esri.arcgis.carto.IRasterRGBRenderer;
import com.esri.arcgis.carto.IRasterRenderer;
import com.esri.arcgis.carto.IRasterStretchColorRampRenderer;
import com.esri.arcgis.carto.IRasterUniqueValueRenderer;
import com.esri.arcgis.carto.RasterClassifyColorRampRenderer;
import com.esri.arcgis.carto.RasterRGBRenderer;
import com.esri.arcgis.carto.RasterStretchColorRampRenderer;
import com.esri.arcgis.carto.RasterUniqueValueRenderer;
import com.esri.arcgis.datasourcesraster.IRasterBand;
import com.esri.arcgis.datasourcesraster.IRasterBandCollection;
import com.esri.arcgis.display.AlgorithmicColorRamp;
import com.esri.arcgis.display.IAlgorithmicColorRamp;
import com.esri.arcgis.display.IColor;
import com.esri.arcgis.display.IFillSymbol;
import com.esri.arcgis.display.IRandomColorRamp;
import com.esri.arcgis.display.ISimpleFillSymbol;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.RandomColorRamp;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleFillSymbol;
import com.esri.arcgis.geodatabase.IRaster;
import com.esri.arcgis.geodatabase.IRow;
import com.esri.arcgis.geodatabase.ITable;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.IServerContext;

/**
 * @author Administrator
 *
 */
public class CustomRasterRender {
	protected IRaster raster = null;
	protected IRasterRenderer rasterRenderer = null;
	protected IColor color = null;
	protected IRasterLayer rasterLayer = null;
	
	protected String featureName;
	
	//stretch颜色坡度渲染raster
	public void renderByRasterStretchColorRamp() throws AutomationException, IOException{
		//得到图层，创建渲染器
		raster = rasterLayer.getRaster();
		IRasterStretchColorRampRenderer stretchRenderer = new RasterStretchColorRampRenderer();
		rasterRenderer = (IRasterRenderer)stretchRenderer;
		
		rasterRenderer.setRasterByRef(raster);
		rasterRenderer.update();
		IColor fromColor = new RgbColor();
		IColor toColor = new RgbColor();
		fromColor.setRGB(0xFF0000);
		toColor.setRGB(0x00FF00);
		
		//创建颜色坡度
		IAlgorithmicColorRamp ramp = new AlgorithmicColorRamp();
		ramp.setSize(255);
		ramp.setFromColor(fromColor);
		ramp.setToColor(toColor);
		//ramp.createRamp(true);
		
		//将颜色坡度插入渲染器并绑定
		stretchRenderer.setBandIndex(0);
		stretchRenderer.setColorRamp(ramp);
		
		//更新到图层
		rasterRenderer.update();
		rasterLayer.setRendererByRef((IRasterRenderer)stretchRenderer);
	}
	
	//RGB渲染raster
	public void changeRGBRender() throws AutomationException, IOException{
		//得到图层，创建渲染器
		raster = rasterLayer.getRaster();
		IRasterBandCollection rasterBandCol = (IRasterBandCollection)raster;
		if(rasterBandCol.getCount() < 3)return;
		
		//创建RGB渲染器
		IRasterRGBRenderer rasterRGBRender = new RasterRGBRenderer();
		rasterRenderer = (IRasterRenderer)rasterRGBRender;
		
		//连接渲染器和栅格图
		rasterRenderer.setRasterByRef(raster);
		rasterRenderer.update();
		
		rasterRGBRender.setRedBandIndex(2);
		rasterRGBRender.setGreenBandIndex(1);
		rasterRGBRender.setBlueBandIndex(0);
		
		//刷新渲染器和图层
		rasterRenderer.update();
		rasterLayer.setRendererByRef((IRasterRenderer)rasterRGBRender);
	}
	
	//分级渲染raster
	public void renderByRasterClassify(IServerContext serverContext) throws AutomationException, IOException{
		System.out.println(featureName.toString());
		int numOfClass = WeatherRenderInfo.getBreak(featureName.toString()).length;
		
		//得到图层，创建分级渲染器
		raster = rasterLayer.getRaster();
		IRasterClassifyColorRampRenderer classifyRenderer = (IRasterClassifyColorRampRenderer) serverContext.createObject(RasterClassifyColorRampRenderer.getClsid());
		rasterRenderer = (IRasterRenderer)classifyRenderer;
		
		//设置渲染和刷新的栅格图
		rasterRenderer.setRasterByRef(raster);
		classifyRenderer.setClassCount(numOfClass);
		
		rasterRenderer.update();
		
		//创建颜色坡度 
		IAlgorithmicColorRamp ramp = (IAlgorithmicColorRamp) serverContext.createObject(AlgorithmicColorRamp.getClsid());
		ramp.setSize(numOfClass);
		
		//为分级创建symbol
		IFillSymbol fillSymbol = (IFillSymbol) serverContext.createObject(SimpleFillSymbol.getClsid());
		
		IColor color = (IColor)serverContext.createObject(RgbColor.getClsid());
		
		//遍历分级并应用颜色和标签
		
		//int[] a = {0x0000FF,0x00FF00,0xFFFF00,0xFFFFFF,0x00FFFF,0xFF0000,0x000000};
		for(int i = 0; i < classifyRenderer.getClassCount() - 1; i++){
			//fillSymbol.setColor((IColor)ramp.getColor(i));
			//color.setRGB(0xFF0000);
			//color.setRGB(a[i]);
			color.setRGB(WeatherRenderInfo.getRGB(featureName)[i]);
			color.setTransparency((byte)100);
			fillSymbol.setColor(color);
			classifyRenderer.setBreak(i, WeatherRenderInfo.getBreak(featureName)[i]);
			classifyRenderer.setSymbol(i, (ISymbol) fillSymbol);
			//classifyRenderer.setLabel(i, "class" + i + classifyRenderer.getBreak(i));
			//color = null;
		}
		
		//刷新渲染器并插入到图层
		rasterRenderer.update();
		rasterLayer.setRendererByRef((IRasterRenderer) classifyRenderer);
		//rasterLayer.draw(esriDrawPhase.esriDPGeography, arg1, null);
	}
	
	//唯一值渲染raster
	public void changeRenderToUVRenderer() throws AutomationException, IOException{
		
		raster = rasterLayer.getRaster();
		
		//从raster数据表中得到行数
		ITable table;
		IRasterBand band;
		IRasterBandCollection bandCol;
		bandCol = (IRasterBandCollection) raster;
		band = bandCol.item(0);
		boolean[] tableExist = null;
		band.hasTable(tableExist);
		if(!(tableExist.length > 0)){
			return;
		}
		table = band.getAttributeTable();
		int numOfValues = table.rowCount(null);
		
		//指定一个域并得到这个要被渲染的域的索引
		String fieldName = "";
		int fieldIndex = table.findField(fieldName);
		
		//创建随即色彩
		IRandomColorRamp ramp = new RandomColorRamp();
		ramp.setSize(numOfValues);
		ramp.setSeed(100);
		//ramp.createRamp(true);
		ISimpleFillSymbol fillSymbol;
		
		//创建唯一值渲染器
		IRasterUniqueValueRenderer uvRenderer = new RasterUniqueValueRenderer();
		rasterRenderer = (IRasterRenderer)uvRenderer;
		
		//连接渲染器和栅格图
		rasterRenderer.setRasterByRef(raster);
		rasterRenderer.update();
		
		//设置唯一值渲染器
		uvRenderer.setHeadingCount(1);
		uvRenderer.setHeading(0, "All Data Values");
		uvRenderer.setClassCount(0, numOfValues);
		uvRenderer.setField(fieldName);
		for(int i = 0; i < numOfValues - 1; i++){
			IRow row = table.getRow(i);
			Object labelValue = row.getValue(fieldIndex);
			uvRenderer.addValue(0, i, labelValue);
			uvRenderer.setLabel(0, 1, labelValue.toString());
			fillSymbol = new SimpleFillSymbol();
			fillSymbol.setColor(ramp.getColor(i));
			uvRenderer.setSymbol(0, i, (ISymbol) fillSymbol);
		}
		
		//更新渲染、刷新图层
		rasterRenderer.update();
		rasterLayer.setRendererByRef((IRasterRenderer) uvRenderer);
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
