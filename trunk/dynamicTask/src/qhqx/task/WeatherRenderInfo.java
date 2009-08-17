/**
 * 
 */
package qhqx.task;

/**
 * @author yan
 * 
 */
public class WeatherRenderInfo {
	// 温度
	/*
	 * final static public int[] TEMPRATURE_RGB = {0x68b823, 0x99cf17, 0xb4da11,
	 * 0xcde700, 0xe5f437, 0xffe47b, 0xfecc5d, 0xfeb46d, 0xfd9a7b, 0xfb8172,
	 * 0xfa6648, 0xfc4e43, 0xfb0035, 0xf40058, 0xf30074, 0xf20080, 0xf100aa,
	 * 0xdb0080, 0xdd188b, 0xe04da5, 0xdf65b0, 0xe07dbd, 0xe295c9, 0xe2aed5,
	 * 0xe3c5e1, 0xb3cde4, 0x81bcda, 0x68b3d3, 0x4eaacf, 0x0290c0, 0x027fb9,
	 * 0x006fb1};
	 */
	final static public int[] TEMPRATURE_RGB = { 0xeb0000, 0xe80000, 0xf1140c,
			0xfb3f34, 0xff4a56, 0xfe3b9b, 0xef00f6, 0x9406f2, 0x3f05ff,
			0x0208fa, 0x0257f2, 0x00aaf4, 0x0bf8f0, 0x03febb, 0x04ee2d,
			0x09e000, 0x2ee636, 0x94fb62, 0xb6ff4e, 0xe6fa27, 0xfefe00,
			0xfefe00, 0xf6f867, 0xf8ff2c, 0xf8f570, 0xfafc9b, 0xfffac6,
			0xf1ded0, 0xd3c0b2, 0xdfe6d6, 0xe6fff9, 0xdafaf9, 0xd9ffff,
			0xecfcfc, 0xf2ffff, };
	final static public double[] TEMPRATURE_BREAK = { -35, -33, -31, -29, -27,
			-25, -23, -21, -19, -17, -15, -13, -11, -9, -7, -5, -3, -1, 1, 3,
			5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35 };

	// 湿度
	final static public int[] HUMIDITY_RGB = { 0x35a02c, 0x008637, 0x019271,
			0x03968c, 0x029ba9, 0x80ced0, 0xb4e2d8, 0xb3cde4, 0x81bcda,
			0x68b3d3, 0x4eaacf, 0x0290c0, 0x027fb9, 0x006fb1, 0x0560a7,
			0x0e419c, 0x053192, 0x0f208a, 0x00007c };
	final static public double[] HUMIDITY_BREAK = { 5, 10, 15, 20, 25, 30, 35,
			40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95 };

	// 气压
	final static public int[] ATMOSPHERIC_RGB = { 0x00007c, 0x0f208a, 0x053192,
			0x0e419c,
			
			0x0560a7, 0x68b823, 0x99cf17, 0xcde700, 0xe5f437, 0xfecc5d,
			0xfd9a7b, 0xfc4e43, 0xf30074, 0xd60838, 0xba042d, 0x990225,
			
			0xe5f437, 0xfecc5d, 0xfd9a7b, 0xfc4e43, 0xf30074, 0xd60838,
			0xba042d, 0x990225, 0xe5f437, 0xfecc5d, 0xfd9a7b, 0xfc4e43,
			0xf30074, 0xd60838, 0xba042d, 0x990225,

			0xe5f437, 0xfecc5d, 0xfd9a7b, 0xfc4e43, 0xf30074, 0xd60838,
			0xba042d, 0x990225

	};
	final static public double[] ATMOSPHERIC_BREAK = { 700, 710, 720, 730, 740,
			750, 760, 770, 780, 790, 800, 810, 820, 830, 840, 850, 860, 870,
			880, 890, 900, 910, 920, 930, 940, 950, 960, 970, 980, 990, 1000,
			1010, 1020, 1030, 1040, 1050, 1060 };

	// 降水
	final static public int[] PRECIPITATION_RGB = { 0xebd3e9, 0xe2aed5,
			0xe04da5, 0xdb0080, 0xf30074, 0xfc4e43, 0xfd9a7b, 0xfecc5d,
			0xe5f437, 0xcde700, 0x99cf17, 0x68b823, 0x0560a7, 0x027fb9,
			0x36b1c1, 0xb4e2d8 };
	final static public double[] PRECIPITATION_BREAK = { 0.1, 1, 2, 5, 10, 15,
			20, 25, 30, 40, 50, 70, 100, 150, 200 };

	// 风速
	final static public int[] WIND_SPEED_RGB = { 2, 4, 6, 8, 10, 12, 14, 16,
			18, 20, 22, 24, 26 };
	final static public double[] WIND_SPEED_BREAK = { 0x0560a7, 0x68b823,
			0x99cf17, 0xcde700, 0xe5f437, 0xfecc5d, 0xfd9a7b, 0xebd3e9,
			0xe2aed5, 0xe04da5, 0xdb0080, 0xf30074, 0xfc4e43 };

	// 风向
	final static public int[] WIND_DIRECTION_RGB = {};
	final static public double[] WIND_DIRECTION_BREAK = {};

	final static public int[] COMMON_RGB = { 0xff3e3c, 0xfd7695, 0xfd76c1,
			0xfa53d7, 0xeb02e1, 0xd200f1, 0xb500fe, 0xad00ff, 0x8b00cb,
			0x8000bc, 0x6f00a3, 0x56017b, 0x560065, 0x5b0358, 0x6f0d62,
			0x6f0d88, 0x68176e, 0x0c2274, 0x193058, 0x854204, 0x89521b,
			0x895d31, 0xbb5d03, 0xdc7516, 0xffc77c, 0xfddb2a, 0xfff626,
			0xfff684, 0x88a0a3, 0x003fbb, 0x0042e1, 0x0058ff, 0x0050ec,
			0x528dff, 0x2a7872, 0x0e6406, 0x929530, 0xb9b14a, 0xcccd67,
			0xccd784, 0xbad489, 0x54a937, 0x149500, 0x15b100, 0x1cd800,
			0x26ff02, 0x53ff34, 0x97ff91, 0x83f1b2, 0x34cdcb, 0x3acccc,
			0x76d2d2, 0xcaeaea, 0xadd6d6, 0x68dedd, 0x00fffe, 0x35f9f9,
			0xa8e7e8, 0xccefef, 0xccfefe, };
	final static public double[] COMMON_BREAK = { -200, -180, -160, -140, -120,
			-100, -80, -60, -40, -20, 0, 20, 40, 60, 80, 100, 120, 140, 160,
			180, 200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400, 420,
			440, 460, 480, 500, 520, 540, 560, 580, 600, 620, 640, 660, 680,
			700, 720, 740, 760, 780, 800, 820, 840, 860, 880, 900, 920, 940,
			960, 980, 1000 };

	static public int[] getRGB(String featureName) {
		if (featureName.equals("wendu")
				|| featureName.equalsIgnoreCase("V12003")) {
			return WeatherRenderInfo.TEMPRATURE_RGB;
		} else if (featureName.equals("shidu")
				|| featureName.equalsIgnoreCase("V13003")) {
			return WeatherRenderInfo.HUMIDITY_RGB;
		} else if (featureName.equals("qiya")
				|| featureName.equalsIgnoreCase("V13004")) {
			return WeatherRenderInfo.ATMOSPHERIC_RGB;
		} else if (featureName.equals("jiangshui")
				|| featureName.equalsIgnoreCase("V13023")) {
			return WeatherRenderInfo.PRECIPITATION_RGB;
		} else if (featureName.equals("fengsu")
				|| featureName.equalsIgnoreCase("V11302")) {
			return WeatherRenderInfo.WIND_SPEED_RGB;
		} else if (featureName.equals("fengxiang")
				|| featureName.equalsIgnoreCase("V11301")) {
			return WeatherRenderInfo.WIND_SPEED_RGB;
		} else {
			//return WeatherRenderInfo.TEMPRATURE_RGB;
			return WeatherRenderInfo.COMMON_RGB;
		}
	}

	static public double[] getBreak(String featureName) {
		if (featureName.equals("wendu")
				|| featureName.equalsIgnoreCase("V12003")) {
			return WeatherRenderInfo.TEMPRATURE_BREAK;
		} else if (featureName.equals("shidu")
				|| featureName.equalsIgnoreCase("V13003")) {
			return WeatherRenderInfo.HUMIDITY_BREAK;
		} else if (featureName.equals("qiya")
				|| featureName.equalsIgnoreCase("V13004")) {
			return WeatherRenderInfo.ATMOSPHERIC_BREAK;
		} else if (featureName.equals("jiangshui")
				|| featureName.equalsIgnoreCase("V13023")) {
			return WeatherRenderInfo.PRECIPITATION_BREAK;
		} else if (featureName.equals("fengsu")
				|| featureName.equalsIgnoreCase("V11302")) {
			return WeatherRenderInfo.WIND_SPEED_BREAK;
		} else if (featureName.equals("fengxiang")
				|| featureName.equalsIgnoreCase("V11301")) {
			return WeatherRenderInfo.WIND_SPEED_BREAK;
		} else {
			return WeatherRenderInfo.COMMON_BREAK;
		}
	}

	static public String getName(String featureName) {
		if (featureName.equals("wendu")
				|| featureName.equalsIgnoreCase("V12003")) {
			return new String("温度");
		} else if (featureName.equals("shidu")
				|| featureName.equalsIgnoreCase("V13003")) {
			return new String("湿度");
		} else if (featureName.equals("qiya")
				|| featureName.equalsIgnoreCase("V13004")) {
			return new String("气压");
		} else if (featureName.equals("jiangshui")
				|| featureName.equalsIgnoreCase("V13023")) {
			return new String("降水");
		} else if (featureName.equals("fengsu")
				|| featureName.equalsIgnoreCase("V11302")) {
			return new String("风向风速");
		} else if (featureName.equals("fengxiang")
				|| featureName.equalsIgnoreCase("V11301")) {
			return new String("风向风速");
		} else {
			//return new String(featureName.toString());
			return new String("要素");
		}

	}
}
