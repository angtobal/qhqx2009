/**
 * 
 */
package qhqx.ags;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.esri.arcgisws.EsriJobStatus;
import com.esri.arcgisws.GPDouble;
import com.esri.arcgisws.GPResultOptions;
import com.esri.arcgisws.GPServerBindingStub;
import com.esri.arcgisws.GPString;
import com.esri.arcgisws.GPToolInfo;
import com.esri.arcgisws.GPValue;

/**
 * @author yan
 *
 */
public class GrahpicUpdateTimerTask extends TimerTask {
	
	private ServletContext servletContext;
	private boolean isRunning = false;
	private static final int UPDATE_SCHEDULE_HOUR = 0;
	
	public GrahpicUpdateTimerTask(ServletContext servletContext){
		this.servletContext = servletContext;
	}

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		Calendar calendar = Calendar.getInstance();
		//System.out.println("timer task run");
		/*try {
			executeTask();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if(!isRunning){
			if(UPDATE_SCHEDULE_HOUR == calendar.get(Calendar.HOUR_OF_DAY)){
				isRunning = true;
				this.servletContext.log("Graphic update task start");
				
				//executeTask();
				
				isRunning = false;
				
				this.servletContext.log("task finished");
			}
		}else{
			this.servletContext.log("the pretask is not finished");
		}
	}
	
	public void executeTask() throws IOException, MalformedURLException{
		System.out.println("timer task");
		/*//AGSLocalGPResource res = (AGSLocalGPResource)ctx.getResourceById("ags1");
		if(res != null){
			System.out.println(res);
		}*/
		//GPServerBindingStub gp = (GPServerBindingStub)res.getGPServer();//new GPServerBindingStub(new java.net.URL(url),null);
		GPServerBindingStub gp = new GPServerBindingStub(new java.net.URL("http://YAN:8399/arcgis/services/line/GPServer?"),null);
		if(gp != null){
			System.out.println(gp);
		}
		GPToolInfo toolInfo = gp.getToolInfo("contour");
		if(toolInfo != null){
			System.out.println(toolInfo.toString());
		}
		
		System.out.println("从gp工具执行结果中读取等值线信息");
		//Calendar cal = Calendar.getInstance();
		//GPResult rs = null;
		GPResultOptions options = new GPResultOptions();
		options.setDensifyFeatures(true);
		
		System.out.println("contour2");
		GPString QiYa = new GPString();			
		QiYa.setValue("height");
		
		GPDouble Base_contour = new GPDouble();
		Base_contour.setValue(0);
		
		GPDouble Contour_interval= new GPDouble();
		Contour_interval.setValue(60);
		
		GPValue[] values = new GPValue[3];
		values[0] = QiYa;
		values[1] = Base_contour;
		values[2] = Contour_interval;
		String jobID = gp.submitJob(toolInfo.getName(),values, options, null);
		while(gp.getJobStatus(jobID) !=  EsriJobStatus.esriJobSucceeded){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("success");
		/*FileControl fileControl = new FileControl();
		fileControl.inputFileList("d:\\arcgisserver\\arcgisjobs\\line_gpserver\\");
		for(int i = 0; i < fileControl.getTargetDir().size(); i++){
			fileControl.deleteDir(fileControl.getTargetDir().get(i).getAbsolutePath());
		}*/
		System.out.println("ok");
	}
}
