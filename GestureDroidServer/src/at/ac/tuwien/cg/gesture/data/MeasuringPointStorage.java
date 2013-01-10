package at.ac.tuwien.cg.gesture.data;

import java.util.ArrayList;

public class MeasuringPointStorage {
	
	private ArrayList<Object> objectQueue;
	private int measurementID = 0;
	
	public MeasuringPointStorage(){
		objectQueue = new ArrayList<Object>();
	}
	
	public void setMeasurementID(int id){
		this.measurementID=id;
	}
	
	public int getMeasurementID(){
		return measurementID;
	}
	
	public synchronized Object getObject(int i){
		return objectQueue.get(i);
	}
	
	public synchronized void removeObject(int i){
		if(objectQueue.size()>0)
		objectQueue.remove(i);
	}
	
	public synchronized void addObject(Object o){
		objectQueue.add(o);
	}
	
	public synchronized void removeAll(){
		objectQueue.removeAll(objectQueue);
	}
	
	public synchronized int getSize(){
		return objectQueue.size();
	}

}
