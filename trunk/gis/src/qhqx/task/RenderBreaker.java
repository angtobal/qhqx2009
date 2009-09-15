/**
 * 
 */
package qhqx.task;

import qhqx.db.MaxMinValue;

/**
 * @author yan
 *
 */
public class RenderBreaker {
	private double max;
	private double min;
	private int numOfClass;
	private double intervalRange;
	private String pid;
	
	//增加构造函数，从数据库获得。。
	public RenderBreaker(String pid){
		MaxMinValue maxmin = new MaxMinValue();
		maxmin.setPid(pid);
		double[] temp = maxmin.selectMaxMinValue();
		max = temp[0];
		min = temp[1];
		this.pid = pid;
		numOfClass = 32;
	}
	
	public double[] createRenderBreak(){
		double[] commonBreak = new double[numOfClass];
		countIntervalRange();
		countNumOfClass();
		
		double count = 0;
		for(int i = 0; i < numOfClass - 1; i++){
			commonBreak[i] = min + count;
			count += intervalRange;
		}
		if(numOfClass > 1){
			commonBreak[numOfClass -1] = max;
		}
		
		return commonBreak;
		
	}
	
	public int countNumOfClass(){
		if(intervalRange <= 2 && intervalRange <= 0){
			intervalRange = 2;
			countNumOfClass();
			System.out.println("<2");
		}else{
			numOfClass = 32;
		}
		return numOfClass;
		
	}
	
	public double countIntervalRange(){
		intervalRange = (max - min)/(numOfClass - 1);
		System.out.println(max + " " + min + " " + intervalRange); 
		return intervalRange;
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

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	
}
