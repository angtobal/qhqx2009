/**
 * 
 */
package qhqx.task;

/**
 * @author yan
 *
 */
public class RenderBreaker {
	private float max;
	private float min;
	private int numOfClass;
	private float intervalRange;
	
	//增加构造函数，从数据库获得。。
	
	public double[] createRenderBreak(){
		double[] commonBreak = new double[numOfClass];
		intervalRange = (max - min)/(numOfClass - 1);
		System.out.println(max + " " + min + " " + intervalRange); 
		double count = 0;
		for(int i = 0; i < numOfClass - 1; i++){
			commonBreak[i] = min + count;
			count += intervalRange;
		}
		commonBreak[numOfClass -1] = max;
		return commonBreak;
		
	}
	
	public float getMax() {
		return max;
	}
	public void setMax(float max) {
		this.max = max;
	}
	public float getMin() {
		return min;
	}
	public void setMin(float min) {
		this.min = min;
	}
	public int getNumOfClass() {
		return numOfClass;
	}
	public void setNumOfClass(int numOfClass) {
		this.numOfClass = numOfClass;
	}
	public float getIntervalRange() {
		intervalRange = (max - min)/numOfClass;
		return intervalRange;
	}
	public void setIntervalRange(float intervalRange) {
		this.intervalRange = intervalRange;
	}
	
	
}
