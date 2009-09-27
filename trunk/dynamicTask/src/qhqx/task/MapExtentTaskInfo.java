package qhqx.task;

import com.esri.adf.web.data.tasks.SimpleTaskInfo;
import com.esri.adf.web.data.tasks.TaskDescriptor;
import com.esri.adf.web.data.tasks.TaskActionDescriptor;
import com.esri.adf.web.data.tasks.TaskActionDescriptorModel;

public class MapExtentTaskInfo extends SimpleTaskInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9192646619162583546L;

	public TaskDescriptor getTaskDescriptor() {
		TaskDescriptor descriptor = new TaskDescriptor(MapExtent.class, "mapExtent", "");
		return descriptor;
	}

	public TaskActionDescriptorModel[] getActionDescriptors(){
		TaskActionDescriptor[] descriptors = new TaskActionDescriptor[1];
		descriptors[0] = new TaskActionDescriptor(MapExtent.class, "concentrateQinghai", "æ”÷–");
		//descriptors[0].setRendererType(TaskActionDescriptor.IMAGEANDTEXT_RENDERER_TYPE);
		descriptors[0].setRendererType(TaskActionDescriptor.IMAGE_RENDERER_TYPE);
		descriptors[0].setDefaultImage("images/maptips/church-24x24.png");
		descriptors[0].setHoverImage("images/maptips/church-32x32.png");
		descriptors[0].setShowLoadingImage(true);
		descriptors[0].setToolTip("Home");
		return descriptors;
	}
}
