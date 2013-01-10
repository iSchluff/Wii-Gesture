package wiiGestureX.recorders;

import wiiGestureX.Gesture;
import wiiGestureX.events.GestureAccelerationListener;
import wiiGestureX.events.GestureEvent;
import wiiGestureX.events.GestureListener;
import wiiGestureX.filters.Filter;
import motej.Mote;
import motej.demos.common.SimpleMoteFinder;
import motej.event.CoreButtonEvent;
import motej.event.CoreButtonListener;
import motej.request.ReportModeRequest;

/**
 * @author Anton Schubert
 * 
 * Ermöglicht die Aufnahme mehrerer Gesten hintereinander
 *
 */
public class MultipleGestureRecorder {
	private static Mote mote;
	private Gesture gesture;
	private GestureAccelerationListener listener;
    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
	
    public MultipleGestureRecorder(){
    	this(null);
    }
    
	public MultipleGestureRecorder(Filter filter){
		SimpleMoteFinder simpleMoteFinder = new SimpleMoteFinder();
		mote = simpleMoteFinder.findMote();
		//System.out.println("- Press 1 and 2 simultaneously to connect your Wiimote -");
		//System.out.println("- Press the Home Key to close the connection -");
		
		mote.setPlayerLeds(new boolean[] {true,false,false,false});
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x33);
		
		gesture=new Gesture();
		listener=new GestureAccelerationListener(gesture, filter);
		
		
		mote.addCoreButtonListener(new CoreButtonListener() {
			private boolean pressedA=false;
			public void buttonPressed(CoreButtonEvent evt) {
				
				if (evt.isButtonAPressed() && pressedA==false) {
					mote.addAccelerometerListener(listener);
					pressedA=true;
				}
				
				if (evt.isNoButtonPressed()) {
					if(pressedA){
						mote.removeAccelerometerListener(listener);
						System.out.println("- firing GestureEvent -");
						fireGestureEvent();
						gesture=new Gesture();
						listener=new GestureAccelerationListener(gesture);
						pressedA=false;
					}
				}
				
				if(evt.isButtonHomePressed()){
					mote.setPlayerLeds(new boolean[] {false,false,false,false});
					mote.setReportMode(ReportModeRequest.DATA_REPORT_0x30);
					mote.disconnect();
				}
			}
		});
	}
	
    // register for GestureEvent
    public void addGestureListener(GestureListener listener) {
        listenerList.add(GestureListener.class, listener);
    }

    // unregister for GestureEvent
    public void removeGestureListener(GestureListener listener) {
        listenerList.remove(GestureListener.class, listener);
    }
    
    public void forceDisconnect(){
		mote.setPlayerLeds(new boolean[] {false,false,false,false});
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x30);
		mote.disconnect();
    }
    
    protected void fireGestureEvent() {
        GestureListener[] listeners = listenerList.getListeners(GestureListener.class);
        GestureEvent evt = new GestureEvent(this,gesture);
        for (GestureListener l : listeners) {
            l.GestureRecorded(evt);
        }
    }

}