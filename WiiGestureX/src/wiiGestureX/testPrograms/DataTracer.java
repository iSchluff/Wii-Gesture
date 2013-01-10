package wiiGestureX.testPrograms;

import javax.swing.JFrame;
import motej.Mote;
import motej.demos.common.SimpleMoteFinder;
import motej.event.CoreButtonEvent;
import motej.event.CoreButtonListener;
import motej.request.ReportModeRequest;
import wiiGestureX.events.GestureAccelerationListener;
import wiiGestureX.util.Log;

public class DataTracer{
	private static Mote mote;
	private static boolean stopped=false;
	
	public static void main(String[] args) throws InterruptedException{
		Log.level=Log.DEBUG;
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DataGraph graph=new DataGraph();
        f.add(graph);
        f.setSize(1024,400);
        f.setLocation(0,0);
        f.setVisible(true);
        
		TraceGesture gesture=new TraceGesture(graph);
		final GestureAccelerationListener listener=new GestureAccelerationListener(gesture);
		
		SimpleMoteFinder simpleMoteFinder = new SimpleMoteFinder();
		System.out.println("- Press 1 and 2 simultaneously to connect your Wiimote -");
		mote = simpleMoteFinder.findMote();		
		mote.setPlayerLeds(new boolean[] {true,false,false,false});
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x31);
		mote.addAccelerometerListener(listener);
		
		mote.addCoreButtonListener(new CoreButtonListener() {
			public void buttonPressed(CoreButtonEvent evt) {
				if (evt.isButtonHomePressed()) {
						mote.setPlayerLeds(new boolean[] {false,false,false,false});
						mote.removeAccelerometerListener(listener);
						mote.setReportMode(ReportModeRequest.DATA_REPORT_0x30);
						mote.disconnect();
						f.dispose();
						stopped=true;
				}
			}
		});
		
		while(!stopped){
			Thread.sleep(1000L);
		}
		
	}
}
