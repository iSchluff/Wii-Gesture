package wiiGestureX.events;
import java.util.Vector;

import motej.Mote;
import motej.event.AccelerometerEvent;
import motej.event.AccelerometerListener;
import wiiGestureX.Gesture;
import wiiGestureX.filters.Filter;
import wiiGestureX.gui.GestureGraph;

/**
 * Listener für AccelerometerEvents
 * Nimmt kontinuierlich auf und schreibt Werte in die Anzeige
 * @author Anton Schubert
 *
 */
public class DataTraceAccelerationListener implements AccelerometerListener<Mote> {
	private Gesture gesture;
	private Filter filter;
	private GestureGraph graph;
	private double xAccel=0;
	private double yAccel=0;
	private double zAccel=0;
	
	public DataTraceAccelerationListener(GestureGraph graph){
		this(graph,null);
	}
	
	public DataTraceAccelerationListener(GestureGraph graph, Filter filter){
		/* Initialisieren */
		this.graph=graph;
		this.filter=filter;
	}

	
	/**
	 * Beginnt Daten in eine Geste zu schreiben
	 */
	public void startRecord(Gesture gesture){
		this.gesture=gesture;
	}
	
	/**
	 * Hört auf Daten in eine Geste zu schreiben
	 */
	public void stopRecord(){
		gesture=null;
	}

	@Override
	public void accelerometerChanged(AccelerometerEvent<Mote> evt) {
		/* Parameter auslesen */
		xAccel=evt.getX();
		yAccel=evt.getY();
		zAccel=evt.getZ();
		
		Vector<Double> accelStep=new Vector<Double>();
		//Log.write(xAccel+" "+yAccel+" "+zAccel,this);
		accelStep.add(xAccel);
		accelStep.add(yAccel);
		accelStep.add(zAccel);
		
		/* Werte filtern */
		if(filter!=null){
			Vector<Double>filtered=filter.filter(xAccel, yAccel, zAccel);
			
			/* Werte in Geste schreiben */
			if(gesture!=null){
				gesture.setAccelStep(filtered);
			}
			graph.setAccel(filtered);
		}else{
			if(gesture!=null){
				gesture.setAccelStep(accelStep);
			}
			graph.setAccel(accelStep);
		}
	}
}
