package wiiGestureX.testPrograms;

import java.io.IOException;

import wiiGestureX.Gesture;
import wiiGestureX.GestureManager;
import wiiGestureX.events.GestureEvent;
import wiiGestureX.events.GestureListener;
import wiiGestureX.filters.HighPassFilter;
import wiiGestureX.recorders.MultipleGestureRecorder;
import wiiGestureX.util.Log;

public class SimpleTest {

	/**
	 * Nimmt eine Geste auf und gibt den gefundenen Gestentyp aus
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		Log.level=Log.DEBUG;
/*	 	JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1024,400);
        f.setLocation(0,0);
        f.setVisible(true);
        final GestureGraph graph=new GestureGraph(80);
        f.add(graph);*/
        final GestureManager manager=new GestureManager("highPassGestures");
        
        GestureListener listener=new GestureListener(){
    		@Override
    		public void GestureRecorded(GestureEvent evt) {
    			//graph.clear();
    			Gesture gesture=evt.getGesture();
    			int index=manager.classify(gesture);
    			if(index!=-1){
    				System.out.println("Matching: "+manager.getGestureType(index).getName());
    			}else{
    				System.out.println("No Matching Gesture found!");
    			}
    			//graph.addGesture(gesture);
    		}	
        };
        
        MultipleGestureRecorder recorder=new MultipleGestureRecorder(new HighPassFilter());
        recorder.addGestureListener(listener);
        
		while(true){
			Thread.sleep(1000L);
		}
	}

}
