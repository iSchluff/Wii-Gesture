package wiiGestureX.testPrograms;

import java.io.IOException;
import java.util.Vector;

import wiiGestureX.Gesture;
import wiiGestureX.GestureManager;
import wiiGestureX.GestureType;
import wiiGestureX.events.GestureEvent;
import wiiGestureX.events.GestureListener;
import wiiGestureX.recorders.MultipleGestureRecorder;
import wiiGestureX.util.Log;

public class GestureTypeRecorder {
	static GestureManager manager;
	static Vector<Gesture> gestures;
    static MultipleGestureRecorder recorder;

	
	public static void main(String[] args) throws IOException, InterruptedException{
		Log.level=Log.DEBUG;
		manager=new GestureManager("highPassGestures");
		gestures=new Vector<Gesture>();
		
        GestureListener listener=new GestureListener(){
    		@Override
    		public void GestureRecorded(GestureEvent evt) {
    			
    			Gesture gesture=evt.getGesture();
    			gestures.add(gesture);
    			
    			Log.write("aufgenommene Gesten: "+ gestures.size());
    		}	
        };
        
        recorder=new MultipleGestureRecorder();
        recorder.addGestureListener(listener);
        
		while(gestures.size()<10){
			Thread.sleep(1000L);
		}
		createType("kreislinks");
	}
	
	public static void createType(String name){
		recorder.forceDisconnect();
		Log.write("Erstellung des Gestentyps: "+name);
		GestureType type=new GestureType(name);
		type.train(gestures);
		manager.addGestureType(type);
	}

}
