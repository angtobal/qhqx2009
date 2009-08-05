/**
 * 
 */
package qhqx.sysop;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import qhqx.ags.GrahpicUpdateTimerTask;

/**
 * @author yan
 *
 */
public class GraphicUpdateContextListener implements ServletContextListener {
	
	private Timer timer = null;

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent ServletContextEvent) {
		timer.cancel();
		ServletContextEvent.getServletContext().log("timer is canceled");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		timer = new Timer(true);
		servletContextEvent.getServletContext().log("Timer is started!");
		timer.schedule(new GrahpicUpdateTimerTask(servletContextEvent.getServletContext()), 0, 5 * 60 * 1000);
		//timer.scheduleAtFixedRate(task, firstTime, period);
		servletContextEvent.getServletContext().log("Timer task schedule is added!");
	}

}
