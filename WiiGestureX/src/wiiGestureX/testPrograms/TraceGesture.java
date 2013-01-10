package wiiGestureX.testPrograms;

import java.util.Vector;

import wiiGestureX.Gesture;
import wiiGestureX.filters.HighPassFilter;
import wiiGestureX.filters.LowPassFilter;

public class TraceGesture extends Gesture {
	
	private DataGraph graph;
	private LowPassFilter lowpass;
	private HighPassFilter highpass;

	public TraceGesture(DataGraph graph){
		this.graph=graph;
		lowpass=new LowPassFilter();
		highpass=new HighPassFilter();
	}
	
	public void setAccelStep(Double xAccel,Double yAccel, Double zAccel){
		Vector<Double> accelStep=new Vector<Double>();
		accelStep.add(xAccel);
		accelStep.add(yAccel);
		accelStep.add(zAccel);
		accelerations.add(accelStep);
		
		//Filter im GestureAccelerationListener müssen deaktiviert sein!
		Vector<Double> filterAccel=new Vector<Double>();
		filterAccel.add(lowpass.filter(xAccel, 0, 0).get(0)); //rot
		filterAccel.add(highpass.filter(xAccel, 0, 0).get(0)); //grün
		filterAccel.add(xAccel); //blau
		
		graph.setAccel(filterAccel);
	}

}
