package wiiGestureX.recorders;

import wiiGestureX.Gesture;
import wiiGestureX.events.GestureAccelerationListener;
import wiiGestureX.events.GestureEvent;
import wiiGestureX.events.GestureListener;
import motej.Mote;
import motej.demos.common.SimpleMoteFinder;
import motej.event.CoreButtonEvent;
import motej.event.CoreButtonListener;
import motej.request.ReportModeRequest;

/**
 * @author Anton Schubert
 * 
 * Ermöglicht die Aufnhame von Gesten
 *
 */
public class SimpleGestureRecorder {
	private static Mote mote;
	private Gesture gesture;
	
    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
	
	public SimpleGestureRecorder(){
		SimpleMoteFinder simpleMoteFinder = new SimpleMoteFinder();
		mote = simpleMoteFinder.findMote();
		System.out.println("- Press 1 and 2 simultaneously to connect your Wiimote -");
		
		mote.setPlayerLeds(new boolean[] {true,false,false,false});
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x33);
		
		gesture=new Gesture();
		
		final GestureAccelerationListener listener=new GestureAccelerationListener(gesture);
		
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
						mote.setReportMode(ReportModeRequest.DATA_REPORT_0x30);
						mote.disconnect();
					}
				}
			}
		});
	}
	
	//returns WiiMote Object
	public static Mote getMote() {
		return mote;
	}
	
    // register for GestureEvent
    public void addGestureListener(GestureListener listener) {
        listenerList.add(GestureListener.class, listener);
    }

    // unregister for GestureEvent
    public void removeGestureListener(GestureListener listener) {
        listenerList.remove(GestureListener.class, listener);
    }
    
    protected void fireGestureEvent() {
        GestureListener[] listeners = listenerList.getListeners(GestureListener.class);
        GestureEvent evt = new GestureEvent(this,gesture);
        for (GestureListener l : listeners) {
            l.GestureRecorded(evt);
        }
    }

}
