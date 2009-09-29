package qhqx.task;

import com.esri.adf.web.data.tasks.SimpleTaskInfo;
import com.esri.adf.web.data.tasks.TaskDescriptor;
import com.esri.adf.web.data.tasks.TaskActionDescriptor;
import com.esri.adf.web.data.tasks.TaskActionDescriptorModel;

public class MapLegendViewTaskInfo extends SimpleTaskInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4332539376596275644L;
	public String imgURL;

	public TaskDescriptor getTaskDescriptor() {
		TaskDescriptor descriptor = new TaskDescriptor(MapLegendView.class, "legendView", "Í¼Àý");
		return descriptor;
	}

	public TaskActionDescriptorModel[] getActionDescriptors(){
		TaskActionDescriptor[] descriptors = new TaskActionDescriptor[1];
		descriptors[0] = new TaskActionDescriptor(MapLegendView.class, "legendView", "Í¼Àý");
		descriptors[0].setRendererType(TaskActionDescriptor.IMAGE_RENDERER_TYPE);
		descriptors[0].setDefaultImage("images/fengsu.jpg");
		descriptors[0].setHoverImage(" ");
		descriptors[0].setSelectedImage(" ");
		descriptors[0].setShowLoadingImage(true);
		descriptors[0].setToolTip("Í¼Àý");
		return descriptors;
	}
}
