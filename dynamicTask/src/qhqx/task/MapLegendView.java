package qhqx.task;

import com.esri.adf.web.faces.event.TaskEvent;

public class MapLegendView {

	private MapLegendViewTaskInfo taskInfo = new MapLegendViewTaskInfo();

	public void  legendView(TaskEvent event) {
		System.out.println(this.getClass().getName() + ":legendView()");
	}

	public MapLegendViewTaskInfo getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(MapLegendViewTaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

}
