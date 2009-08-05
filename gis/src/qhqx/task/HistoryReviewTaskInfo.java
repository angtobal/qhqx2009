package qhqx.task;

import com.esri.adf.web.data.tasks.AbsoluteLayout;
import com.esri.adf.web.data.tasks.AbsolutePosition;
import com.esri.adf.web.data.tasks.SimpleTaskInfo;
import com.esri.adf.web.data.tasks.TaskDescriptor;
import com.esri.adf.web.data.tasks.TaskLayout;
import com.esri.adf.web.data.tasks.TaskParamDescriptorModel;
import com.esri.adf.web.data.tasks.TaskParamDescriptor;
import com.esri.adf.web.data.tasks.TaskActionDescriptor;
import com.esri.adf.web.data.tasks.TaskActionDescriptorModel;

public class HistoryReviewTaskInfo extends SimpleTaskInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3078709243030682666L;
	private TaskDescriptor taskDescriptor = new TaskDescriptor(HistoryReviewTask.class, "HistoryReview", "历史查询");
	private TaskParamDescriptor[] paramDescriptors = new TaskParamDescriptor[4];
	private TaskActionDescriptor[] actionDescriptors = new TaskActionDescriptor[2];
	private TaskLayout layout[] = new TaskLayout[1];

	public HistoryReviewTaskInfo(){
		paramDescriptors[0] = new TaskParamDescriptor(HistoryReviewTask.class, "year", "年", "getYear", "setYear");
		paramDescriptors[1] = new TaskParamDescriptor(HistoryReviewTask.class, "month", "月", "getMonth", "setMonth");
		paramDescriptors[2] = new TaskParamDescriptor(HistoryReviewTask.class, "day", "日", "getDay", "setDay");
		paramDescriptors[3] = new TaskParamDescriptor(HistoryReviewTask.class, "time", "时", "getTime", "setTime");
		
		actionDescriptors[0] = new TaskActionDescriptor(HistoryReviewTask.class, "search", "查询");
		actionDescriptors[1] = new TaskActionDescriptor(HistoryReviewTask.class, "remove", "delete");

		AbsoluteLayout absoluteLayout = new AbsoluteLayout();
		System.out.println(absoluteLayout.getLayoutType() + ":" + absoluteLayout.getStyleClass());
		absoluteLayout.setStyle("width:800px; height:45px; padding:0px; margin:0px;");
		System.out.println("    " + absoluteLayout.getStyle());
		absoluteLayout.setId("historyReview");
		absoluteLayout.addComponent(null, new AbsolutePosition(0, 0));
		absoluteLayout.addComponent(paramDescriptors[0], new AbsolutePosition(5, 5));
		absoluteLayout.addComponent(paramDescriptors[1], new AbsolutePosition(195, 5));
		absoluteLayout.addComponent(paramDescriptors[2], new AbsolutePosition(385, 5));
		absoluteLayout.addComponent(paramDescriptors[3], new AbsolutePosition(575, 5));
		absoluteLayout.addComponent(actionDescriptors[0], new AbsolutePosition(760, 5));
		//absoluteLayout.addComponent(actionDescriptors[1], new AbsolutePosition(800,5));
		
		layout[0] = absoluteLayout;
	}
	public TaskDescriptor getTaskDescriptor() {
		return taskDescriptor;
	}

	public TaskParamDescriptorModel[] getParamDescriptors(){
		return paramDescriptors;
	}

	public TaskActionDescriptorModel[] getActionDescriptors(){
		return actionDescriptors;
	}
	
	public TaskLayout[] getTaskLayout(){
		return layout;
	}
}
