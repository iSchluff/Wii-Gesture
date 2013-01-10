package wiiGestureX.events;
import java.util.Vector;

import wiiGestureX.Gesture;
import wiiGestureX.filters.Filter;
import motej.Mote;
import motej.event.*;

/**
 * Listener für AccelerometerEvents
 * @author Anton Schubert
 *
 */
public class GestureAccelerationListener implements AccelerometerListener<Mote> {
	private Gesture gesture;
	private Filter filter;
	private Double xAccel=0.0;
	private Double yAccel=0.0;
	private Double zAccel=0.0;
	
	public GestureAccelerationListener(Gesture gesture){
		this(gesture,null);
	}
	
	public GestureAccelerationListener(Gesture gesture, Filter filter){
		/* Initialisieren */
		this.gesture=gesture;
		this.filter=filter;
	}

	@Override
	public void accelerometerChanged(AccelerometerEvent<Mote> evt) {
		/* Parameter auslesen */
		xAccel=evt.getX();
		yAccel=evt.getY();
		zAccel=evt.getZ();
		
		Vector<Double> accelStep=new Vector<Double>();
		accelStep.add(xAccel);
		accelStep.add(yAccel);
		accelStep.add(zAccel);
		
		/* Werte filtern */
		if(filter!=null){
			Vector<Double>filtered=filter.filter(xAccel, yAccel, zAccel);
			
			/* Werte in Geste schreiben */
			gesture.setAccelStep(filtered);
		}else{
			gesture.setAccelStep(accelStep);
		}
	}
}
