package wiiGestureX;

import java.util.Vector;

/**
 * @author Anton Schubert
 *
 * Speichert Beschleunigungsdaten 
 */
public class Gesture {	
	protected Vector<Vector<Double>> accelerations;
	protected Vector<Vector<Double>> rotations;
	
	public Gesture(){		
		accelerations=new Vector<Vector<Double>>();
		rotations=new Vector<Vector<Double>>();
	}
	
	public void setAccelStep(Vector<Double> accelStep){
		accelerations.add(accelStep);
	}
	
	public void setRotStep(Double rollSpeed,Double pitchSpeed, Double yawSpeed){
		Vector<Double> rotStep=new Vector<Double>();
		rotStep.add(rollSpeed);
		rotStep.add(pitchSpeed);
		rotStep.add(yawSpeed);
		accelerations.add(rotStep);
	}
	
	public Vector<Vector<Double>> returnData(){
		return accelerations;
	}
	
	public int getSize(){
		return accelerations.size();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@SuppressWarnings("unchecked")
	public Gesture clone(){
		Gesture gesture=new Gesture();
		for(int i=0;i<accelerations.size();i++){
			Vector<Double>accelStep=accelerations.get(i);
			gesture.setAccelStep((Vector<Double>) accelStep.clone());
		}
		return gesture;
	}
}
