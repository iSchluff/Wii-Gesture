package wiiGestureX.recorders;

import motej.Mote;
import motej.event.CoreButtonEvent;
import motej.event.CoreButtonListener;
import motej.request.ReportModeRequest;
import wiiGestureX.Gesture;
import wiiGestureX.MoteConnector;
import wiiGestureX.events.DataTraceAccelerationListener;
import wiiGestureX.events.GestureEvent;
import wiiGestureX.events.GestureListener;
import wiiGestureX.filters.Filter;
import wiiGestureX.gui.GestureGraph;
import wiiGestureX.util.Log;

/**
 * @author Anton Schubert
 *
 * Ermöglicht die Ausgabe in der grafischen Oberfläche
 *
 */
public class DataTraceRecorder {
	private static Mote mote;
	private Gesture gesture;
	private DataTraceAccelerationListener listener;
	private GestureGraph graph;
	private Filter filter;
    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();
    
    public DataTraceRecorder(GestureGraph graph){
    	this(graph,null);
    }
    
	public DataTraceRecorder(GestureGraph graph, Filter filter){
		this.graph=graph;
		this.filter=filter;
	}
	
	/**
	 * Stellt Verbindung zu einer Mote her
	 * 
	 * @return true wenn die Verbindung erfolgt ist, sonst false
	 */
	public boolean connectMote(){
		MoteConnector connector=new MoteConnector();
		mote = connector.findMote();
		if(mote==null){
			return false;
		}
		mote.setPlayerLeds(new boolean[] {true,false,false,false});
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x33);
		
		listener=new DataTraceAccelerationListener(graph, filter);
		mote.addAccelerometerListener(listener);
		
		mote.addCoreButtonListener(new CoreButtonListener() {
			private boolean pressedA=false;
			public void buttonPressed(CoreButtonEvent evt) {
				
				if ((evt.isButtonAPressed() || evt.isButtonBPressed()) && pressedA==false) {
					pressedA=true;
					gesture=new Gesture();
					Log.write("starting to record",this);
					listener.startRecord(gesture);
					graph.recording=true;
				}
				
				if (evt.isNoButtonPressed()) {
					if(pressedA){
						listener.stopRecord();
						Log.write("firing GestureEvent",this);
						fireGestureEvent();
						pressedA=false;
						graph.recording=false;
					}
				}
				
				if(evt.isButtonHomePressed()){
					mote.setPlayerLeds(new boolean[] {false,false,false,false});
					mote.removeAccelerometerListener(listener);
					mote.setReportMode(ReportModeRequest.DATA_REPORT_0x30);
					mote.disconnect();
				}
			}
		});
		return true;
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
		mote.removeAccelerometerListener(listener);
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
