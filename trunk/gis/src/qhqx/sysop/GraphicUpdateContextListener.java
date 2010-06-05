/**
 * 
 */
package qhqx.sysop;

import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import qhqx.ags.GrahpicUpdateTimerTask;
import qhqx.server.GISResourceManager;

/**
 * @author yan
 *
 */
public class GraphicUpdateContextListener implements ServletContextListener {
	
	private Timer timer = null;

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent ServletContextEvent) {
		timer.cancel();
		ServletContextEvent.getServletContext().log("timer is canceled");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		startMapService();
		
		timer = new Timer(true);
		servletContextEvent.getServletContext().log("Timer is started!");
		timer.schedule(new GrahpicUpdateTimerTask(servletContextEvent.getServletContext()), 0, 5 * 60 * 1000);
		//timer.scheduleAtFixedRate(task, firstTime, period);
		
		timer.schedule(new TempFileClean(), 0, 30 * 60 * 1000);
		servletContextEvent.getServletContext().log("Timer task schedule is added!");
	}
	
	//启动地图服务
	private void startMapService(){
		GISResourceManager grm = GISResourceManager.getInstance();
		grm.restartMapService();
		grm.restartGPService();
		try {
			System.out.println("wait arcgis server start");
			Thread.currentThread().sleep(30 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("started!");
	}

}
