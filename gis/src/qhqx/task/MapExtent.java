package qhqx.task;

import com.esri.adf.web.data.WebContext;
import com.esri.adf.web.data.geometry.WebExtent;
import com.esri.adf.web.faces.event.TaskEvent;

public class MapExtent {

	private MapExtentTaskInfo taskInfo = new MapExtentTaskInfo();

	public void  concentrateQinghai(TaskEvent event) {
		WebContext ctx = event.getWebContext();
		ctx.getWebMap().setCurrentExtent(new WebExtent(89.400228, 31.542524, 104.5, 40.339596));
		ctx.refresh();
	}

	public MapExtentTaskInfo getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(MapExtentTaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

}
