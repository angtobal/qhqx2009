package qinghai.qixiang.winddirectionandspeed;

/**
 * author:yang
 * 
 * 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

import qinghai.qixiang.winddirectionandspeed.selectspeedpic.SelectSpeedPic;

import com.esri.adf.web.data.GraphicElement;
import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.geometry.WebGeometry;
import com.esri.adf.web.data.geometry.WebPoint;
import com.esri.adf.web.data.symbol.WebTrueTypeMarkerSymbol;

@SuppressWarnings("serial")
public class WindDirectionSpeed  {

	@SuppressWarnings("unused")
	private WebContext gContext = null;

	@SuppressWarnings("unused")
	private Map<String, String> strLayers = null;
	
	private String pid;

	public void addWindSymbol(){
		addWindDirectionAndSpeed(gContext);
		addTestValues(gContext);
	}
	
	public void afterPhase(PhaseEvent phaseEvent) {
		/*if (phaseEvent.getPhaseId() == PhaseId.RENDER_RESPONSE) {
			WebContext webContext = WebUtil.getWebContext(phaseEvent
					.getFacesContext().getViewRoot());
			addWindDirectionAndSpeed(webContext);
			addTestValues(webContext);
		}*/

	}

	public void beforePhase(PhaseEvent phaseEvent) {

		if (phaseEvent.getPhaseId() == PhaseId.INVOKE_APPLICATION) {
			System.out.println("is new request!");
		}
		System.out.println(phaseEvent.getPhaseId().toString());
	}

	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	Double geolat = 0.0;
	Double geolong = 0.0;
	Double direction = 0.0;
	Double speed = 0.0;

	public void addWindDirectionAndSpeed(WebContext webContext) {

		ResultSet gisData = getDate();

		try {
			while (gisData.next()) {

				geolat = gisData.getDouble("LATITUDE");
				geolong = gisData.getDouble("longitude");
				/*direction = gisData.getDouble("ZVALUE1");
				speed = gisData.getDouble("ZVALUE2");*/
				direction = gisData.getDouble("ZVALUE2");
				speed = gisData.getDouble("ZVALUE1");

				WebTrueTypeMarkerSymbol trueMarkerSymbol = new WebTrueTypeMarkerSymbol();
				trueMarkerSymbol.setFontName("ESRI Weather");
				trueMarkerSymbol.setFontSize(60);
				trueMarkerSymbol.setFontColor("255,0,0");
				/*trueMarkerSymbol
						.setTextPosition(WebTrueTypeMarkerSymbol.CIRCLE);*/
				trueMarkerSymbol.setTextPosition(WebTrueTypeMarkerSymbol.CENTER);
				trueMarkerSymbol.setFontStyle(WebTrueTypeMarkerSymbol.BOLD);
				trueMarkerSymbol.setAngle(direction);
				String Speed = SelectSpeedPic.selectSpeedPic(speed.intValue());
				trueMarkerSymbol.addTextValue(Speed);

				WebPoint webPoint = new WebPoint();
				webPoint.putCoords(geolong, geolat);
				if (webPoint != null) {
					WebGeometry webGeometry = (WebGeometry) webPoint;
					GraphicElement graphicElem = new GraphicElement();
					graphicElem.setGeometry(webGeometry);
					graphicElem.setSymbol(trueMarkerSymbol);
					webContext.getWebGraphics().addGraphics(graphicElem);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.close(rs);
		this.close(stmt);
		this.close(conn);
	}

	public void addTestValues(WebContext webContext) {
		ResultSet gisData = getDate();

		try {
			while (gisData.next()) {

				geolat = gisData.getDouble("LATITUDE");
				geolong = gisData.getDouble("longitude");
				/*direction = gisData.getDouble("ZVALUE1");
				speed = gisData.getDouble("ZVALUE2");*/
				direction = gisData.getDouble("ZVALUE2");
				speed = gisData.getDouble("ZVALUE1");

				WebTrueTypeMarkerSymbol trueMarkerSymbol = new WebTrueTypeMarkerSymbol();
				trueMarkerSymbol.setFontName("新宋体");
				trueMarkerSymbol.setFontSize(10);
				trueMarkerSymbol.setFontColor("0,0,255");
				trueMarkerSymbol
						.setTextPosition(WebTrueTypeMarkerSymbol.RIGHT);
				trueMarkerSymbol.addTextValue(" 风向：" + direction.toString());
				WebPoint webPoint = new WebPoint();
				webPoint.putCoords(geolong, geolat);

				if (webPoint != null) {
					WebGeometry webGeometry = (WebGeometry) webPoint;
					GraphicElement graphicElem = new GraphicElement();
					graphicElem.setGeometry(webGeometry);
					graphicElem.setSymbol(trueMarkerSymbol);
					webContext.getWebGraphics().addGraphics(graphicElem);
				}

				WebTrueTypeMarkerSymbol trueMarkerSymbo2 = new WebTrueTypeMarkerSymbol();
				trueMarkerSymbo2.setFontName("新宋体");
				trueMarkerSymbo2.setFontSize(10);
				trueMarkerSymbo2.setFontColor("0,0,255");
				/*trueMarkerSymbo2.setTextPosition(WebTrueTypeMarkerSymbol.TOP);*/
				trueMarkerSymbo2.setTextPosition(WebTrueTypeMarkerSymbol.LEFT);
				trueMarkerSymbo2.addTextValue("风速：" + speed.toString());
				WebPoint webPoint2 = new WebPoint();
				webPoint2.putCoords(geolong, geolat);

				if (webPoint != null) {
					WebGeometry webGeometry = (WebGeometry) webPoint2;
					GraphicElement graphicElem = new GraphicElement();
					graphicElem.setGeometry(webGeometry);
					graphicElem.setSymbol(trueMarkerSymbo2);
					webContext.getWebGraphics().addGraphics(graphicElem);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.close(rs);
		this.close(stmt);
		this.close(conn);
	}

	private ResultSet getDate() {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			//String url = "jdbc:oracle:thin:@10.181.22.41:1521:sjy";
			String url = "jdbc:oracle:thin:@10.181.22.41:1521:sjyrdb";
			conn = DriverManager.getConnection(url, "gx", "gx");

			stmt = conn.createStatement();

			rs = stmt
					.executeQuery("select * from SJY_GIS_INFO where pid='" + pid.toString() + "'");

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}

	public void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}

	public void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt = null;
		}
	}

	public void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public WebContext getGContext() {
		return gContext;
	}

	public void setGContext(WebContext context) {
		gContext = context;
	}

}
