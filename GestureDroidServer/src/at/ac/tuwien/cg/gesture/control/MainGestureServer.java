package at.ac.tuwien.cg.gesture.control;

import at.ac.tuwien.cg.gesture.data.MeasuringPointStorage;
import at.ac.tuwien.cg.gesture.visualization.MainWindow;
import at.ac.tuwien.cg.gesture.visualization.RenderLoop;

public class MainGestureServer {
	
	public static void main (String args[]) {
		
		//MeasuringPointStorage
		MeasuringPointStorage mpStorage = new MeasuringPointStorage(); 
		
		InputHandler ih = new InputHandler(mpStorage);

		//Window
		MainWindow mainWin = new MainWindow();
		mainWin.setVisible(true);
	
		
		//RenderLoop
		Thread rl = new RenderLoop(mainWin,mpStorage);
		rl.start();
		
		DataInputHandler dataInputHandler = new DataInputHandler(ih);
		
		dataInputHandler.start();
				
	}

}


