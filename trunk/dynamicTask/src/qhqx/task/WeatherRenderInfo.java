/**
 * 
 */
package qhqx.task;


/**
 * @author yan
 *
 */
public class WeatherRenderInfo {
	//温度
	final static public int[] TEMPRATURE_RGB = {0x000000,0xA8FF02,0x04FD01,0xFFFD6C,0xFBC2E3,0xFFBFE9,0xFF0000};
	final static public double[] TEMPRATURE_BREAK = {10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42};
	
	//湿度
	final static public int[] HUMIDITY_RGB = {0x04FD01,0xA8FF02,0x000000,0xFFFD6C,0xFBC2E3,0xFFBFE9,0xFF0000,0xFF0000,0xFF0000,0xFF0000,0xFF0000};
	final static public double[] HUMIDITY_BREAK = {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
	
	//气压
	final static public int[] ATMOSPHERIC_RGB = {0xdeecf7, 0xdeecf7, 0xb0dcff, 0x88bdf3, 0x6391e7, 0x3b66d6, 0x0b289e, 0x011a5d};
	final static public double[] ATMOSPHERIC_BREAK = {0, 5, 10, 15, 20, 30, 50, 100};
	
	//降水
	final static public int[] PRECIPITATION_RGB = {};
	final static public double[] PRECIPITATION_BREAK = {0.1, 1, 2, 5, 10, 15, 20, 25, 30, 40, 50, 70, 100, 150, 200};
	
	//风速
	final static public int[] WIND_SPEED_RGB = {};
	final static public double[] WIND_SPEED_BREAK = {};
	
	//风向
	final static public int[] WIND_DIRECTION_RGB = {};
	final static public double[] WIND_DIRECTION_BREAK = {};
	
	static public int[] getRGB(String featureName){
		if(featureName.equals("wendu")){
			return WeatherRenderInfo.TEMPRATURE_RGB;
		}else if(featureName.equals("shidu")){
			return WeatherRenderInfo.HUMIDITY_RGB;
		}else if(featureName == "qiya"){
			return WeatherRenderInfo.ATMOSPHERIC_RGB;
		}else if(featureName == "jiangshui"){
			return WeatherRenderInfo.PRECIPITATION_RGB;
		}else if(featureName == "fengsu"){
			//return WeatherRenderInfo.PRECIPITATION_RGB;
		}else if(featureName == "fengxiang"){
			//return WeatherRenderInfo.PRECIPITATION_RGB;
		}
		return null;
	}
	
	static public double[] getBreak(String featureName){
		if(featureName.equals("wendu")){
			return WeatherRenderInfo.TEMPRATURE_BREAK;
		}else if(featureName.equals("shidu")){
			return WeatherRenderInfo.HUMIDITY_BREAK;
		}else if(featureName == "qiya"){
			return WeatherRenderInfo.ATMOSPHERIC_BREAK;
		}else if(featureName == "jiangshui"){
			return WeatherRenderInfo.PRECIPITATION_BREAK;
		}else if(featureName == "fengsu"){
			//return WeatherRenderInfo.PRECIPITATION_RGB;
		}else if(featureName == "fengxiang"){
			//return WeatherRenderInfo.PRECIPITATION_RGB;
		}
		return null;
	}
}
