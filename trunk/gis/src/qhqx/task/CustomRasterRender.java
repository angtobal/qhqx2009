/**
 * ��Ⱦraster��ʽ
 * .stretch��ɫ�¶���Ⱦraster
 * .RGB��Ⱦraster
 * .�ּ���Ⱦraster
 * .Ψһֵ��Ⱦraster
 */
package qhqx.task;

import java.io.IOException;
import java.util.Date;

import com.esri.arcgis.carto.IRasterClassifyColorRampRenderer;
import com.esri.arcgis.carto.IRasterRenderer;
import com.esri.arcgis.carto.RasterClassifyColorRampRenderer;
import com.esri.arcgis.display.IColor;
import com.esri.arcgis.display.IFillSymbol;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleFillSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.server.IServerContext;

/**
 * @author Administrator
 *
 */
public class CustomRasterRender extends RasterRenderInfo {
	private String pid;
	private int numOfClass;
	
	//Start====================================================================
	public void changeRasterClassifyRenderer(IRasterClassifyColorRampRenderer rClassifyRenderer, IServerContext serverContext){
		countNumOfClass();
		IFillSymbol fillSymbol = null;
		IColor color = null;
		try {
			rClassifyRenderer.setClassCount(numOfClass);
			fillSymbol = (IFillSymbol)serverContext.createObject(SimpleFillSymbol.getClsid());
			color = (IColor)serverContext.createObject(RgbColor.getClsid());
		} catch (AutomationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double tmp = 0;
		if(WeatherRenderInfo.getEnName(featureName).equals("common")
				|| WeatherRenderInfo.getEnName(featureName).equals("fengsu")
				|| WeatherRenderInfo.getEnName(featureName).equals("qiya")){
			double rgbSelectInterval = 0;
			int numOfColor = WeatherRenderInfo.getRGB("wendu").length;
			RenderBreaker rb = new RenderBreaker(pid);
			rgbSelectInterval = (rb.getMax() - rb.getMin())/numOfColor;
			tmp = rb.getMax();
			for(int i = 0; i < numOfColor; i++){
				try {
					color.setRGB(WeatherRenderInfo.getRGB("wendu")[i]);
					color.setTransparency((byte)128);
					fillSymbol.setColor(color);
					rClassifyRenderer.setBreak(i, tmp);
					tmp -= rgbSelectInterval; 
					rClassifyRenderer.setSymbol(i, (ISymbol) fillSymbol);
					rClassifyRenderer.setLabel(i, "  " + (double)((int)(rClassifyRenderer.getBreak(i)*10))/10);
				} catch (AutomationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else if(WeatherRenderInfo.getEnName(featureName).equals("wendu")){
			for(int i = 0; i < numOfClass - 1; i++){
				try {
					color.setRGB(WeatherRenderInfo.getRGB(featureName)[i]);
					color.setTransparency((byte)128);
					fillSymbol.setColor(color);
				
					//double���������⣺0��Ϊ��ʼֵ�����ֵ������
					if(WeatherRenderInfo.getBreak(featureName)[i] == 0 && WeatherRenderInfo.getBreak(featureName)[i - 1] > 0){
						rClassifyRenderer.setBreak(i, WeatherRenderInfo.getBreak(featureName)[i] + 0.000000000000011);
						rClassifyRenderer.setSymbol(i, (ISymbol) fillSymbol);
						//classifyRenderer.setLabel(i, "  " + classifyRenderer.getBreak(i));
						rClassifyRenderer.setLabel(i, "  " + WeatherRenderInfo.getBreak(featureName)[i]);
						continue;
					}
					if(WeatherRenderInfo.getBreak(featureName)[i] < 0 && WeatherRenderInfo.getBreak(featureName)[i - 1] >= 0){
						rClassifyRenderer.setBreak(i, -0.000000000000011);
						rClassifyRenderer.setSymbol(i, (ISymbol) fillSymbol);
						//classifyRenderer.setLabel(i, "  " + classifyRenderer.getBreak(i));
						rClassifyRenderer.setLabel(i, "  " + WeatherRenderInfo.getBreak(featureName)[i]);
						continue;
					}
					
					rClassifyRenderer.setBreak(i, WeatherRenderInfo.getBreak(featureName)[i]);
					rClassifyRenderer.setSymbol(i, (ISymbol) fillSymbol);
					rClassifyRenderer.setLabel(i, "  " + WeatherRenderInfo.getBreak(featureName)[i]);
				} catch (AutomationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			try {
				rClassifyRenderer.setBreak(rClassifyRenderer.getClassCount() - 1, tmp);
				rClassifyRenderer.setLabel(rClassifyRenderer.getClassCount() - 1, "  " + Double.toString(WeatherRenderInfo.getBreak(featureName)[WeatherRenderInfo.getBreak(featureName).length - 1]));
				
				color.setRGB(WeatherRenderInfo.getRGB("wendu")[WeatherRenderInfo.getRGB("wendu").length -1]);
				color.setTransparency((byte)128);
				fillSymbol.setColor(color);
				rClassifyRenderer.setSymbol(rClassifyRenderer.getClassCount() - 1, (ISymbol) fillSymbol);
				
				rClassifyRenderer.setLabel(0, "  >" + rClassifyRenderer.getLabel(1).trim());
			} catch (AutomationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			
			for(int i = 0; i < numOfClass - 1; i++){
				try {
					color.setRGB(WeatherRenderInfo.getRGB(featureName)[i]);
					color.setTransparency((byte)128);
					fillSymbol.setColor(color);
					
					rClassifyRenderer.setBreak(i, WeatherRenderInfo.getBreak(featureName)[i]);
					rClassifyRenderer.setSymbol(i, (ISymbol) fillSymbol);
					rClassifyRenderer.setLabel(i, "  " + WeatherRenderInfo.getBreak(featureName)[i]);
				} catch (AutomationException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				rClassifyRenderer.setBreak(rClassifyRenderer.getClassCount() - 1, tmp);
				rClassifyRenderer.setLabel(rClassifyRenderer.getClassCount() - 1, "  " + Double.toString(WeatherRenderInfo.getBreak(featureName)[WeatherRenderInfo.getBreak(featureName).length - 1]));
				
				color.setRGB(WeatherRenderInfo.getRGB(featureName)[WeatherRenderInfo.getRGB(featureName).length -1]);
				color.setTransparency((byte)128);
				fillSymbol.setColor(color);
				rClassifyRenderer.setSymbol(rClassifyRenderer.getClassCount() - 1, (ISymbol) fillSymbol);
				
				//rClassifyRenderer.setLabel(0, "  " + rClassifyRenderer.getLabel(1).trim());
			} catch (AutomationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		System.out.println(new Date() + " rendered");
	}
	//End====================================================================
	
	//�ּ���Ⱦraster
	public void renderByRasterClassify(IServerContext serverContext) throws AutomationException, IOException{
		
		//�õ�ͼ�㣬�����ּ���Ⱦ��
		raster = rasterLayer.getRaster();
		IRasterClassifyColorRampRenderer classifyRenderer = (IRasterClassifyColorRampRenderer) serverContext.createObject(RasterClassifyColorRampRenderer.getClsid());
		rasterRenderer = (IRasterRenderer)classifyRenderer;
		
		//������Ⱦ��ˢ�µ�դ��ͼ
		rasterRenderer.setRasterByRef(raster);
		countNumOfClass();
		classifyRenderer.setClassCount(numOfClass);
		rasterRenderer.update();//���룬����label��ֵλ�������⣬secClassCount��fillSymbol��color֮ǰ����
		
		//Ϊ�ּ�����symbol
		IFillSymbol fillSymbol = (IFillSymbol) serverContext.createObject(SimpleFillSymbol.getClsid());
		
		IColor color = (IColor)serverContext.createObject(RgbColor.getClsid());
		
		double tmp = 0;
		//�����ּ���Ӧ����ɫ�ͱ�ǩ
		if(WeatherRenderInfo.getEnName(featureName).equals("common") || WeatherRenderInfo.getEnName(featureName).equals("fengsu") || WeatherRenderInfo.getEnName(featureName).equals("qiya")){
			
			RenderBreaker rb = new RenderBreaker(pid);
			/*double[] tmpBreak = rb.createRenderBreak();*///ascending
			
			//double[] tmpBreak = rb.createRenderBreakDescending();
			numOfClass = rb.getNumOfClass();
			
			double rgbSelectInterval = 0;
			int numOfColor = WeatherRenderInfo.getRGB("wendu").length;
			rgbSelectInterval = (rb.getMax() - rb.getMin())/numOfColor;
			tmp = rb.getMax();
			
			for(int i = 0; i < numOfColor; i++){
				//color.setRGB(WeatherRenderInfo.getRGB("wendu")[Math.abs(numOfColor - Math.abs((int)(i * rgbSelectInterval) + 1))]);
				color.setRGB(WeatherRenderInfo.getRGB("wendu")[i]);
				color.setTransparency((byte)80);
				fillSymbol.setColor(color);
				/*����˳��*/
				//int j = classifyRenderer.getClassCount() - i - 1;
				
				/*classifyRenderer.setBreak(i, tmpBreak[i]);*/
				classifyRenderer.setBreak(i, tmp);
				tmp -= rgbSelectInterval; 
				classifyRenderer.setSymbol(i, (ISymbol) fillSymbol);
				classifyRenderer.setLabel(i, "  " + (double)((int)(classifyRenderer.getBreak(i)*10))/10);
			}
		}else if(WeatherRenderInfo.getEnName(featureName).equals("wendu")){
			for(int i = 0; i < numOfClass - 1; i++){
				color.setRGB(WeatherRenderInfo.getRGB(featureName)[i]);
				color.setTransparency((byte)80);
				fillSymbol.setColor(color);
			
				//double���������⣺0��Ϊ��ʼֵ�����ֵ������
				if(WeatherRenderInfo.getBreak(featureName)[i] == 0 && WeatherRenderInfo.getBreak(featureName)[i - 1] > 0){
					classifyRenderer.setBreak(i, WeatherRenderInfo.getBreak(featureName)[i] + 0.000000000000011);
					classifyRenderer.setSymbol(i, (ISymbol) fillSymbol);
					//classifyRenderer.setLabel(i, "  " + classifyRenderer.getBreak(i));
					classifyRenderer.setLabel(i, "  " + WeatherRenderInfo.getBreak(featureName)[i]);
					continue;
				}
				if(WeatherRenderInfo.getBreak(featureName)[i] < 0 && WeatherRenderInfo.getBreak(featureName)[i - 1] >= 0){
					classifyRenderer.setBreak(i, -0.000000000000011);
					classifyRenderer.setSymbol(i, (ISymbol) fillSymbol);
					//classifyRenderer.setLabel(i, "  " + classifyRenderer.getBreak(i));
					classifyRenderer.setLabel(i, "  " + WeatherRenderInfo.getBreak(featureName)[i]);
					continue;
				}
				
				classifyRenderer.setBreak(i, WeatherRenderInfo.getBreak(featureName)[i]);
				classifyRenderer.setSymbol(i, (ISymbol) fillSymbol);
				//classifyRenderer.setLabel(i, "  " + classifyRenderer.getBreak(i));
				classifyRenderer.setLabel(i, "  " + WeatherRenderInfo.getBreak(featureName)[i]);
				//color = null;
			}
			classifyRenderer.setBreak(classifyRenderer.getClassCount() - 1, tmp);
			classifyRenderer.setLabel(classifyRenderer.getClassCount() - 1, "  " + Double.toString(WeatherRenderInfo.getBreak(featureName)[WeatherRenderInfo.getBreak(featureName).length - 1]));
			
			color.setRGB(WeatherRenderInfo.getRGB("wendu")[WeatherRenderInfo.getRGB("wendu").length -1]);
			color.setTransparency((byte)50);
			fillSymbol.setColor(color);
			classifyRenderer.setSymbol(classifyRenderer.getClassCount() - 1, (ISymbol) fillSymbol);
			
			classifyRenderer.setLabel(0, "  >" + classifyRenderer.getLabel(1).trim());
			
		}else{//��Ⱦ�ѹ̶��Ĵ���
			//�������ֵ��Χ��i=0
			/*color.setRGB(WeatherRenderInfo.getRGB(featureName)[0]);
			color.setTransparency((byte)50);
			fillSymbol.setColor(color);
			classifyRenderer.setBreak(0, WeatherRenderInfo.getBreak(featureName)[0]);
			classifyRenderer.setSymbol(0, (ISymbol) fillSymbol);
			classifyRenderer.setLabel(0, "  ");*/
			for(int i = 0; i < numOfClass - 1; i++){
				//fillSymbol.setColor((IColor)ramp.getColor(i));
				//color.setRGB(0xFF0000);
				//color.setRGB(a[i]);
				
				color.setRGB(WeatherRenderInfo.getRGB(featureName)[i]);
				color.setTransparency((byte)50);
				fillSymbol.setColor(color);
				/*����˳��*/
				//int j = classifyRenderer.getClassCount() - i;
				
				
				//���������⣺0��Ϊ��ʼֵ�����ֵ������
				
				classifyRenderer.setBreak(i, WeatherRenderInfo.getBreak(featureName)[i] + 0.0000000001);
				classifyRenderer.setSymbol(i, (ISymbol) fillSymbol);
				//classifyRenderer.setLabel(i, "  " + classifyRenderer.getBreak(i));
				classifyRenderer.setLabel(i, "  " + WeatherRenderInfo.getBreak(featureName)[i]);
				//color = null;
			}
			classifyRenderer.setBreak(classifyRenderer.getClassCount() - 1, tmp);
			classifyRenderer.setLabel(classifyRenderer.getClassCount() - 1, "  " + Double.toString(WeatherRenderInfo.getBreak(featureName)[WeatherRenderInfo.getBreak(featureName).length - 1]));
			
			color.setRGB(WeatherRenderInfo.getRGB("wendu")[WeatherRenderInfo.getRGB("wendu").length -1]);
			color.setTransparency((byte)50);
			fillSymbol.setColor(color);
			classifyRenderer.setSymbol(classifyRenderer.getClassCount() - 1, (ISymbol) fillSymbol);
			
			classifyRenderer.setLabel(0, "  >" + classifyRenderer.getLabel(1).trim());
		}
		//classifyRenderer.setSortClassesAscending(false);
		
		System.out.println(new Date() + "rendered");
		
		//ˢ����Ⱦ�������뵽ͼ��
		rasterRenderer.update();
		rasterLayer.setRendererByRef((IRasterRenderer) classifyRenderer);
		
		//rasterLayer.draw(esriDrawPhase.esriDPGeography, arg1, null);
	}
	
	private int countNumOfClass(){
		if(WeatherRenderInfo.getEnName(featureName).equals("common") || WeatherRenderInfo.getEnName(featureName).equals("fengsu") || WeatherRenderInfo.getEnName(featureName).equals("qiya")){
			/*if(){
				
			}else{*/
				//numOfClass = 32;
			numOfClass = WeatherRenderInfo.getRGB("wendu").length;
			/*}*/
			
		}/*else if(WeatherRenderInfo.getEnName(featureName).equals("wendu")){
			numOfClass = WeatherRenderInfo.getBreak(WeatherRenderInfo.getEnName(featureName)).length + 1;
		}*/else{
			numOfClass = WeatherRenderInfo.getBreak(WeatherRenderInfo.getEnName(featureName)).length;
		}
		return 0;	
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
}
