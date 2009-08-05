/**
 * 
 */
package qhqx.ags;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.esri.adf.web.data.GraphicElement;
import com.esri.adf.web.data.WebGraphics;
import com.esri.adf.web.data.geometry.WebExtent;
import com.esri.adf.web.data.geometry.WebGeometry;
import com.esri.adf.web.data.geometry.WebPoint;
import com.esri.adf.web.data.symbol.WebPictureMarkerSymbol;
import com.esri.adf.web.data.symbol.WebSimpleMarkerSymbol;
import com.esri.adf.web.data.symbol.WebTrueTypeMarkerSymbol;

/**
 * @author yan
 * 
 * 动态添加地标
 */
public class FeatureMarker {
	@SuppressWarnings("unchecked")
	private Map attributeMap = null;
	private double latitude;
	private double longitude;

	@SuppressWarnings("unchecked")
	public void addSimplePointMarker(Hashtable coodsHash,
			WebGraphics webGraphics, WebExtent currentExtent) {
		if (coodsHash.size() > 0) {
			WebSimpleMarkerSymbol simplePiontMarker = new WebSimpleMarkerSymbol();
			simplePiontMarker.setColor("255,255,0");
			simplePiontMarker.setMarkerType(WebSimpleMarkerSymbol.CIRCLE);
			simplePiontMarker.setTransparency(0.5);
			simplePiontMarker.setWidth(5);
			Enumeration locations = coodsHash.keys();
			while (locations.hasMoreElements()) {
				String loc = locations.nextElement().toString();
				String[] strLoc = loc.split(":");
				if (strLoc.length >= 2) {
					WebPoint webPoint = new WebPoint(Double
							.parseDouble(strLoc[0]), Double
							.parseDouble(strLoc[1]));
					if (webPoint != null && currentExtent.contains(webPoint)) {
						WebGeometry webGeometry = (WebGeometry) webPoint;
						this.attributeMap = (Map) coodsHash.get(loc);
						GraphicElement pointElement = new GraphicElement();
						pointElement.setGeometry(webGeometry);
						pointElement.setSymbol(simplePiontMarker);
						webGraphics.addGraphics(pointElement);
					}
				}
			}
		}
	}

	public void addLabelMarker(String labelText, WebGraphics webGraphics,
			WebPoint webPoint) {

		WebTrueTypeMarkerSymbol labelSymbol = new WebTrueTypeMarkerSymbol();
		byte[] temp;
		try {
			temp = labelText.toString().getBytes("UTF-8");
			labelText = new String(temp, "GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		labelSymbol.addTextValue(labelText);
		labelSymbol.setTextPosition(WebTrueTypeMarkerSymbol.RIGHT);

		GraphicElement graphicElement = new GraphicElement();
		graphicElement.setGeometry(webPoint);
		graphicElement.setSymbol(labelSymbol);

		webGraphics.addGraphics(graphicElement);
	}

	@SuppressWarnings("unchecked")
	public void addLabelMarker(Hashtable coodsHash, WebGraphics webGraphics,
			WebExtent currentExtent) {
		if (coodsHash.size() > 0) {
			Enumeration<String> locations = coodsHash.keys();
			while (locations.hasMoreElements()) {
				String loc = locations.nextElement().toString();
				String[] strLoc = loc.split(":");
				if (strLoc.length >= 2) {
					WebPoint webPoint = new WebPoint(Double
							.parseDouble(strLoc[0]), Double
							.parseDouble(strLoc[1]));
					if (webPoint != null && currentExtent.contains(webPoint)) {
						WebGeometry webGeometry = (WebGeometry) webPoint;
						this.attributeMap = (Map) coodsHash.get(loc);

						WebTrueTypeMarkerSymbol labelMarker = new WebTrueTypeMarkerSymbol();
						labelMarker.addTextValue("stnno:"
								+ this.attributeMap.get("V01000").toString());
						labelMarker.setFontColor("255,0,0");
						labelMarker.setFontSize(10);
						labelMarker
								.setTextPosition(WebTrueTypeMarkerSymbol.RIGHT);

						GraphicElement labelElement = new GraphicElement();
						labelElement.setGeometry(webGeometry);
						labelElement.setSymbol(labelMarker);

						webGraphics.addGraphics(labelElement);
					}
				}
			}
		}
	}

	public void addLabelMarker(
			Hashtable<String, Map<String, String>> coordsHash,
			String featureName, WebGraphics webGraphics, WebExtent currentExtent) {
		if (coordsHash.size() <= 0) {
			return;
		}
		Enumeration<String> locations = coordsHash.keys();
		while (locations.hasMoreElements()) {
			String loc = locations.nextElement().toString();
			String[] locStr = loc.split(":");
			longitude = Double.parseDouble(locStr[0]);
			latitude = Double.parseDouble(locStr[1]);

			WebPoint webPoint = new WebPoint(longitude, latitude);
			System.out.println(coordsHash.get(locations));
			this.attributeMap = (Map<String, String>) coordsHash.get(loc);

			if (webPoint != null && currentExtent.contains(webPoint)
					&& this.attributeMap != null) {
				WebTrueTypeMarkerSymbol labelSymbol = new WebTrueTypeMarkerSymbol();
				labelSymbol.addTextValue(featureName + ":"
						+ attributeMap.get(featureName).toString());
				labelSymbol.setTextPosition(WebTrueTypeMarkerSymbol.BOTTOM);
				labelSymbol.setFontName("宋体");

				GraphicElement graphicElement = new GraphicElement();
				graphicElement.setGeometry(webPoint);
				graphicElement.setSymbol(labelSymbol);

				webGraphics.addGraphics(graphicElement);
			}
		}
	}

	public void addPictureMarker(WebGeometry webPoint, WebGraphics webGraphics,
			String picurl) {
		WebPictureMarkerSymbol pictureMarker = new WebPictureMarkerSymbol();
		try {
			pictureMarker.setURL(new java.net.URL(picurl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		GraphicElement graphicElement = new GraphicElement();
		graphicElement.setGeometry(webPoint);
		graphicElement.setSymbol(pictureMarker);

		webGraphics.addGraphics(graphicElement);
		/*
		 * WebPictureMarkerSymbol picMarker = new WebPictureMarkerSymbol(); URL
		 * url; try { url = new
		 * URL("http://localhost:8080/qhqx/images/tasks/search/search.gif");
		 * picMarker.setURL(url); } catch (MalformedURLException e) {
		 * e.printStackTrace(); } GraphicElement ge = new GraphicElement();
		 * ge.setGeometry(webPoint); ge.setSymbol(picMarker);
		 * 
		 * webGraphics.addGraphics(ge);
		 */
	}

	@SuppressWarnings("unchecked")
	public void addPictureMarker(Hashtable coodsHash, WebGraphics webGraphics,
			WebExtent currentExtent) {
		if (coodsHash.size() <= 0) {
			System.out.println("feature marker coodsHash size <= 0");
			return;
		}
		Enumeration locations = coodsHash.keys();
		while (locations.hasMoreElements()) {
			String loc = locations.nextElement().toString();
			String[] strLoc = loc.split(":");
			longitude = Double.parseDouble(strLoc[0]);
			latitude = Double.parseDouble(strLoc[1]);
			WebPoint webPoint = new WebPoint(longitude, latitude);
			// WebMultiPoint webMultiPoint = new WebMultiPoint();
			// webMultiPoint.addPoint(new WebPoint(longitude - 1, latitude -
			// 1));
			// webMultiPoint.addPoint(new WebPoint(longitude + 1, latitude -
			// 1));
			// webMultiPoint.addPoint(new WebPoint(longitude + 1, latitude +
			// 1));
			// webMultiPoint.addPoint(new WebPoint(longitude - 1, latitude +
			// 1));
			// WebElementConverter
			// webMultiPoint.addPoint(Double.parseDouble(strLoc[0]),Double.parseDouble(strLoc[1]));
			if (webPoint != null && currentExtent.contains(webPoint)) {
				// WebGeometry webGeometry = (WebGeometry)webPoint;
				WebPictureMarkerSymbol pictureMarker = new WebPictureMarkerSymbol();
				try {
					pictureMarker
							.setURL(new java.net.URL(
									"http://localhost:8080/qhqxgis/images/tasks/search/search.gif"));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

				GraphicElement graphicElement = new GraphicElement();
				graphicElement.setGeometry(webPoint);
				graphicElement.setSymbol(pictureMarker);

				webGraphics.addGraphics(graphicElement);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void addPictureMarker(Hashtable coordsHash, String pictureUrl,
			WebGraphics webGraphics, WebExtent currentExtent) {
		if (coordsHash.size() <= 0) {
			return;
		}
		Iterator locations = (Iterator) coordsHash.keys();
		while (locations.hasNext()) {
			String loc = locations.next().toString();
			String[] locArr = loc.split(":");
			longitude = Double.parseDouble(locArr[0]);
			latitude = Double.parseDouble(locArr[1]);

			Map attributeMap = (Map) coordsHash.get(locations);
			WebPoint webPoint = new WebPoint(longitude, latitude);
			if (webPoint != null && currentExtent.contains(webPoint)
					&& attributeMap != null) {
				WebPictureMarkerSymbol pictureMarker = new WebPictureMarkerSymbol();

				URL url;
				try {
					url = new URL(pictureUrl);
					pictureMarker.setURL(url);

					GraphicElement graphicElement = new GraphicElement();
					graphicElement.setGeometry(webPoint);
					graphicElement.setSymbol(pictureMarker);

					webGraphics.addGraphics(graphicElement);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
