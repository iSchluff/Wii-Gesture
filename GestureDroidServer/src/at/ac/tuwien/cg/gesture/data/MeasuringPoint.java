package at.ac.tuwien.cg.gesture.data;

import java.io.Serializable;


import at.ac.tuwien.cg.gesture.math.Matrix4f;
import at.ac.tuwien.cg.gesture.math.Vector3f;

public class MeasuringPoint implements Serializable {


	/**
	 * This class represent a Measuring Point at a specific time
	 */
	private static final long serialVersionUID = 3193157791027574438L;
	private long time;
	private long deltaTime;
	private Vector3f acceleration;
	private float orientationMatrix[] = new float[16];
	private String id;
	private boolean isRealMeasurement;
	private Vector3f distance;
	private Vector3f velocity;
	private int measurementID;
	private Vector3f [] rowData = null ;

	public MeasuringPoint(Vector3f acceleration, Matrix4f orientationMatrix, long lastMeasurePointTime) {
		set(acceleration, orientationMatrix,lastMeasurePointTime);
	}
	
	public MeasuringPoint(Vector3f acceleration, Matrix4f orientationMatrix, long lastMeasurePointTime,
						  boolean isRealMeasurement, Vector3f distance, Vector3f velocity, int mID) {
		set(acceleration, orientationMatrix,lastMeasurePointTime,isRealMeasurement,distance,velocity, mID);
	}
	
	private void set(Vector3f acceleration, Matrix4f orientationMatrix, long lastMeasurePointTime){
		
		this.time=System.nanoTime();
		this.deltaTime=this.time-lastMeasurePointTime;
		
		this.acceleration=acceleration;
		this.orientationMatrix=orientationMatrix.getMatrix();
	}
	
	private void set(Vector3f acceleration, Matrix4f orientationMatrix, long lastMeasurePointTime,
					 boolean isRealMeasurement, Vector3f distance, Vector3f velocity, int mID){
		
		this.time=System.nanoTime();
		
		this.deltaTime=this.time-lastMeasurePointTime;
		
		this.acceleration=acceleration;
		this.orientationMatrix=orientationMatrix.getMatrix();
		
		this.isRealMeasurement=isRealMeasurement;
		this.distance=distance;
		this.velocity=velocity;
		this.measurementID=mID;
	}

	public void setID(String id) {
		this.id=id;
	}

	public void setMatrix(float[] matrix)
	{
		this.orientationMatrix = matrix;
	}

	public long getTime() {
		return time;
	}

	public Vector3f getAcceleration() {
		return acceleration;
	}

	public String getId() {
		return id;
	}
	
	public float[] getOrientationMatrix()
	{
		return orientationMatrix;
	}

	@Override
	public String toString() {
		return "time="+time+" "+acceleration+" "+orientationMatrix+ " id="+id;
	}

	public long getDeltaTime() {
		return deltaTime;
	}
	
	public Vector3f getDistance() {
		return distance;
	}
	
	public Vector3f getVelocity() {
		return velocity;
	}
	
	public boolean getIsRealMeasurement() {
		return isRealMeasurement;
	}
	
	public int getMeasurementID(){
		return measurementID;
	}

	public Vector3f[] getRowData() {
		return rowData;
	}

	public void setRowData(Vector3f[] rowData) {
		this.rowData = rowData;
	}
}