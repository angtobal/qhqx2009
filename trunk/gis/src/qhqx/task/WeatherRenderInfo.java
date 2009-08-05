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
	final static public int[] TEMPRATURE_RGB = {0x68b823,
		0x99cf17,
		0xb4da11,
		0xcde700,
		0xe5f437,
		0xffe47b,
		0xfecc5d,
		0xfeb46d,
		0xfd9a7b,
		0xfb8172,
		0xfa6648,
		0xfc4e43,
		0xfb0035,
		0xf40058,
		0xf30074,
		0xf20080,
		0xf100aa,
		0xdb0080,
		0xdd188b,
		0xe04da5,
		0xdf65b0,
		0xe07dbd,
		0xe295c9,
		0xe2aed5,
		0xe3c5e1,
		0xb3cde4,
		0x81bcda,
		0x68b3d3,
		0x4eaacf,
		0x0290c0,
		0x027fb9,
		0x006fb1};
	final static public double[] TEMPRATURE_BREAK = {-24, -22, -20, -18, -16, -14, -12, -10, -8, -6, -4, -2, 0,
		2,4,6,8,10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 42};
	
	//湿度
	final static public int[] HUMIDITY_RGB = {0x35a02c,
		0x008637,
		0x019271,
		0x03968c,
		0x029ba9,
		0x80ced0,
		0xb4e2d8,
		0xb3cde4,
		0x81bcda,
		0x68b3d3,
		0x4eaacf,
		0x0290c0,
		0x027fb9,
		0x006fb1,
		0x0560a7,
		0x0e419c,
		0x053192,
		0x0f208a,
		0x00007c};
	final static public double[] HUMIDITY_BREAK = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95};
	
	//气压
	final static public int[] ATMOSPHERIC_RGB = {0x00007c,
		0x0f208a,
		0x053192,
		0x0e419c,
		
		0x0560a7,
		0x68b823,
		0x99cf17,
		0xcde700,
		0xe5f437,
		0xfecc5d,
		0xfd9a7b,
		0xfc4e43,
		0xf30074,
		0xd60838,
		0xba042d,
		0x990225,
		
		
		0xe5f437,
		0xfecc5d,
		0xfd9a7b,
		0xfc4e43,
		0xf30074,
		0xd60838,
		0xba042d,
		0x990225,
		0xe5f437,
		0xfecc5d,
		0xfd9a7b,
		0xfc4e43,
		0xf30074,
		0xd60838,
		0xba042d,
		0x990225,

		0xe5f437,
		0xfecc5d,
		0xfd9a7b,
		0xfc4e43,
		0xf30074,
		0xd60838,
		0xba042d,
		0x990225


		};
	final static public double[] ATMOSPHERIC_BREAK = {700, 710, 720, 730, 740, 750, 760, 770, 780, 790, 800, 810, 820, 830, 840, 850, 860, 870,
		880,
		890,
		900,
		910,
		920,
		930,
		940,
		950,
		960,
		970,
		980,
		990,
		1000,
		1010,
		1020,
		1030,
		1040,
		1050,
		1060};
	
	//降水
	final static public int[] PRECIPITATION_RGB = {0xebd3e9,
		0xe2aed5,
		0xe04da5,
		0xdb0080,
		0xf30074,
		0xfc4e43,
		0xfd9a7b,
		0xfecc5d,
		0xe5f437,
		0xcde700,
		0x99cf17,
		0x68b823,
		0x0560a7,
		0x027fb9,
		0x36b1c1,
		0xb4e2d8};
	final static public double[] PRECIPITATION_BREAK = {0.1, 1, 2, 5, 10, 15, 20, 25, 30, 40, 50, 70, 100, 150, 200};
	
	//风速
	final static public int[] WIND_SPEED_RGB = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26};
	final static public double[] WIND_SPEED_BREAK = {0x0560a7, 0x68b823, 0x99cf17, 0xcde700, 0xe5f437, 0xfecc5d,
		0xfd9a7b,
		0xebd3e9,
		0xe2aed5,
		0xe04da5,
		0xdb0080,
		0xf30074,
		0xfc4e43};
	
	//风向
	final static public int[] WIND_DIRECTION_RGB = {};
	final static public double[] WIND_DIRECTION_BREAK = {};
	
	static public int[] getRGB(String featureName){
		if(featureName.equals("wendu") || featureName.equalsIgnoreCase("V12003")){
			return WeatherRenderInfo.TEMPRATURE_RGB;
		}else if(featureName.equals("shidu") || featureName.equalsIgnoreCase("V13003")){
			return WeatherRenderInfo.HUMIDITY_RGB;
		}else if(featureName .equals("qiya") || featureName.equalsIgnoreCase("V13004")){
			return WeatherRenderInfo.ATMOSPHERIC_RGB;
		}else if(featureName.equals("jiangshui") || featureName.equalsIgnoreCase("V13023")){
			return WeatherRenderInfo.PRECIPITATION_RGB;
		}else if(featureName.equals("fengsu") || featureName.equalsIgnoreCase("V11302")){
			return WeatherRenderInfo.WIND_SPEED_RGB;
		}else if(featureName.equals("fengxiang") || featureName.equalsIgnoreCase("V11301")){
			return WeatherRenderInfo.WIND_SPEED_RGB;
		}else{
			return WeatherRenderInfo.TEMPRATURE_RGB;
		}
	}
	
	static public double[] getBreak(String featureName){
		if(featureName.equals("wendu") || featureName.equalsIgnoreCase("V12003")){
			return WeatherRenderInfo.TEMPRATURE_BREAK;
		}else if(featureName.equals("shidu") || featureName.equalsIgnoreCase("V13003")){
			return WeatherRenderInfo.HUMIDITY_BREAK;
		}else if(featureName.equals("qiya") || featureName.equalsIgnoreCase("V13004")){
			return WeatherRenderInfo.ATMOSPHERIC_BREAK;
		}else if(featureName.equals("jiangshui") || featureName.equalsIgnoreCase("V13023")){
			return WeatherRenderInfo.PRECIPITATION_BREAK;
		}else if(featureName.equals("fengsu") || featureName.equalsIgnoreCase("V11302")){
			return WeatherRenderInfo.WIND_SPEED_BREAK;
		}else if(featureName.equals("fengxiang") || featureName.equalsIgnoreCase("V11301")){
			return WeatherRenderInfo.WIND_SPEED_BREAK;
		}else{
			return WeatherRenderInfo.TEMPRATURE_BREAK;
		}
	}
	
	static public String getName(String featureName){
		if(featureName.equals("wendu") || featureName.equalsIgnoreCase("V12003")){
			return new String("温度");
		}else if(featureName.equals("shidu") || featureName.equalsIgnoreCase("V13003")){
			return new String("湿度");
		}else if(featureName.equals("qiya") || featureName.equalsIgnoreCase("V13004")){
			return new String("气压");
		}else if(featureName.equals("jiangshui") || featureName.equalsIgnoreCase("V13023")){
			return new String("降水");
		}else if(featureName.equals("fengsu") || featureName.equalsIgnoreCase("V11302")){
			return new String("风向风速");
		}else if(featureName.equals("fengxiang") || featureName.equalsIgnoreCase("V11301")){
			return new String("风向风速");
		}else{
			return new String(featureName.toString());
		}
		
	}
}
