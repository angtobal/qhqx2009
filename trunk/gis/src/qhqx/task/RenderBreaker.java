/**
 * 
 */
package qhqx.task;

/**
 * @author yan
 *
 */
public class RenderBreaker {
	private double max;
	private double min;
	private int numOfClass;
	private double intervalRange;
	
	//���ӹ��캯���������ݿ��á���
	
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
	
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	public int getNumOfClass() {
		return numOfClass;
	}
	public void setNumOfClass(int numOfClass) {
		this.numOfClass = numOfClass;
	}
	public double getIntervalRange() {
		intervalRange = (max - min)/numOfClass;
		return intervalRange;
	}
	public void setIntervalRange(float intervalRange) {
		this.intervalRange = intervalRange;
	}
	
	
}
