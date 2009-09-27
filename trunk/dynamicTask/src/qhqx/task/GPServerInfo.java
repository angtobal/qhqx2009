/**
 * 
 */
package qhqx.task;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import com.esri.adf.web.data.WebContext;
import com.esri.arcgisws.EsriJobStatus;
import com.esri.arcgisws.GPDouble;
import com.esri.arcgisws.GPResult;
import com.esri.arcgisws.GPResultOptions;
import com.esri.arcgisws.GPServerBindingStub;
import com.esri.arcgisws.GPString;
import com.esri.arcgisws.GPToolInfo;
import com.esri.arcgisws.GPValue;

/**
 * @author yan
 *
 */
public class GPServerInfo {

	private String gpEndpoint = null;
	protected WebContext webContext = null;//face config
	private GPToolInfo toolInfo = null;
	private GPResult result = null;
	private GPResultOptions resultOpt = null;
	protected String pid = null;
	private String base = null;
	private String interval = null;
	protected String JobID = null;

	/**
	 * 
	 */
	public GPServerInfo() {
		super();
	}

	@SuppressWarnings("static-access")
	public void generateContout(WebContext webContext, String modelName)
			throws MalformedURLException, RemoteException, InterruptedException {
				GPString queryString = new GPString();
				queryString.setValue("PID=\'" + pid + "\'");
				//queryString.setValue("\"F10\"=\'" + pid + "\'");
				
				GPDouble baseContour = new GPDouble();
				baseContour.setValue(Double.parseDouble(base));
				
				GPDouble contourInterval = new GPDouble();
				contourInterval.setValue(Double.parseDouble(interval));
				
				GPValue[] gpValues = new GPValue[3];
				gpValues[0] = contourInterval;
				gpValues[1] = baseContour;
				gpValues[2] = queryString;
				
				
				
				GPServerBindingStub gpServer = new GPServerBindingStub(new java.net.URL(gpEndpoint), null);
				toolInfo = gpServer.getToolInfo(modelName);
				resultOpt = new GPResultOptions();
				resultOpt.setDensifyFeatures(true);
				
				JobID = gpServer.submitJob(toolInfo.getName(), gpValues, resultOpt, null);
				System.out.println(JobID);
				//Check the status of the job; if it's finished and successful, proceed - false delay.
				while (gpServer.getJobStatus(JobID) != EsriJobStatus.esriJobSucceeded && gpServer.getJobStatus(JobID) != EsriJobStatus.esriJobFailed){
				    Thread.currentThread().sleep(3000);
				    System.out.println(gpServer.getJobStatus(JobID).toString());
				}
				
				if(gpServer.getResultMapServerName() != null){
					//System.out.println(gpServer.getResultMapServerName().toString() + "aaa");
					//result = gpServer.getJobResult(JobID, null, resultOpt);
				}
				
			}

	public void generateContout(String modelName) throws MalformedURLException,
			RemoteException, InterruptedException {
				GPString queryString = new GPString();
				queryString.setValue("PID=\'" + pid + "\'");
				//queryString.setValue("\"F10\"=\'" + pid + "\'");
				
				GPDouble baseContour = new GPDouble();
				baseContour.setValue(Double.parseDouble(base));
				
				GPDouble contourInterval = new GPDouble();
				contourInterval.setValue(Double.parseDouble(interval));
				
				GPValue[] gpValues = new GPValue[3];
				gpValues[0] = contourInterval;
				gpValues[1] = baseContour;
				gpValues[2] = queryString;
				
				GPServerBindingStub gpServer = new GPServerBindingStub(new java.net.URL(gpEndpoint), null);
				toolInfo = gpServer.getToolInfo(modelName);
				resultOpt = new GPResultOptions();
				resultOpt.setDensifyFeatures(true);
				
				JobID = gpServer.submitJob(toolInfo.getName(), gpValues, resultOpt, null);
				System.out.println(JobID);
				//Check the status of the job; if it's finished and successful, proceed - false delay.
				while (gpServer.getJobStatus(JobID) != EsriJobStatus.esriJobSucceeded && gpServer.getJobStatus(JobID) != EsriJobStatus.esriJobFailed){
				    Thread.sleep(3000);
				    System.out.println(gpServer.getJobStatus(JobID).toString());
				}
				
				if(gpServer.getResultMapServerName() != null){
					//System.out.println(gpServer.getResultMapServerName().toString() + "aaa");
					//result = gpServer.getJobResult(JobID, null, resultOpt);
				}
				
			}

	public String getEndpoint() {
		return gpEndpoint;
	}

	public void setEndpoint(String endpoint) {
		this.gpEndpoint = endpoint;
	}

	public GPToolInfo getToolInfo() {
		return toolInfo;
	}

	public void setToolInfo(GPToolInfo toolInfo) {
		this.toolInfo = toolInfo;
	}

	public GPResult getResult() {
		return result;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public WebContext getWebContext() {
		return webContext;
	}

	public void setWebContext(WebContext webContext) {
		this.webContext = webContext;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

}