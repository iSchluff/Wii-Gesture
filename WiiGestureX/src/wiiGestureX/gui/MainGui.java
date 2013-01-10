package wiiGestureX.gui;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import wiiGestureX.Gesture;
import wiiGestureX.GestureManager;
import wiiGestureX.events.GestureEvent;
import wiiGestureX.events.GestureListener;
import wiiGestureX.recorders.DataTraceRecorder;
import wiiGestureX.util.Log;

public class MainGui {
	//gui
	public static JFrame frame;
	public static JPanel actionBar;
	public static PropertyPanel propertyPanel;
	public static JList gestureList;
	public static JScrollPane gesturePanel;
	private static GestureGraph graphPanel=new GestureGraph();
	private static Box centerPanel;
	private static StatusBar statusBar;
	
	//logic
	public static DataTraceRecorder recorder;
	public static GestureManager manager;
	public static GestureListener mainListener;
	
	public static void main(String[] args) throws IOException {
		Log.level=Log.DEBUG;
		//System Look And Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		//Erstellt den Frame
		frame = new JFrame("WiiGestureX");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		WindowListener windowListener=new WindowListener(){
			public void windowActivated(WindowEvent event) {}
			public void windowClosed(WindowEvent event) {}
			public void windowDeactivated(WindowEvent event) {}
			public void windowDeiconified(WindowEvent event) {}
			public void windowIconified(WindowEvent event) {}
			public void windowOpened(WindowEvent event) {}

			@Override
			public void windowClosing(WindowEvent event) {
				manager.writeAllChanges();
				System.exit(0);
			}
		};

		frame.addWindowListener(windowListener);

		manager=new GestureManager("gestures");
		
		createGestureRecorder();
		createPanels();
		
		StatusController.setStatusBar(statusBar);
		StatusController.setMessage("Ready", StatusController.SUCCESS);
		
		frame.pack();
		frame.setSize(800, 600);
		//maximiert das Fenster
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		//com.sun.awt.AWTUtilities.setWindowOpacity(frame, (float) .5);
		
		//Macht die GUI sichtbar
		frame.setVisible(true);
	}
	
	private static void createPanels(){
		actionBar=new ActionBar();
		
		graphPanel.setBackground(Color.white);
		statusBar=new StatusBar();
		propertyPanel=new PropertyPanel();
		centerPanel=new Box(BoxLayout.Y_AXIS);
		centerPanel.add(graphPanel); centerPanel.add(statusBar); centerPanel.add(propertyPanel);
		//propertyPanel.setMaximumSize(new Dimension(propertyPanel.getWidth(),500));
		
		
		//GesturePanel
		gestureList=new GestureList();
		gestureList.setSelectedIndex(0);
		gesturePanel=new JScrollPane(gestureList,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//gesturePanel.add(gestureList);
		//gesturePanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//gesturePanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//gesturePanel.setBorder(BorderFactory.createEmptyBorder());
		
		//Borders
		graphPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		gesturePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		centerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		//Add to the Frame
		frame.add(actionBar, BorderLayout.NORTH);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.add(gesturePanel, BorderLayout.WEST);
		
		//statusBar.setPreferredSize(new Dimension(statusBar.getWidth(),30));
		//statusBar.setPreferredSize(statusBar.getMinimumSize());
		graphPanel.setPreferredSize(new Dimension(graphPanel.getWidth(),700));
		propertyPanel.setPreferredSize(new Dimension(propertyPanel.getWidth(),150));
	}
	
	private static void createGestureRecorder(){
        mainListener=new GestureListener(){
    		@Override
    		public void GestureRecorded(GestureEvent evt) {
    			Gesture gesture=evt.getGesture();
    			int index=manager.classify(gesture);
    			if(index!=-1){
    				Log.write("Matching: "+manager.getGestureType(index).getName(),this);
    				StatusController.setMessage("Geste "+manager.getGestureType(index).getName()+" erkannt!", StatusController.SUCCESS);
    				manager.getGestureType(index).getExecutor().execute();
    			}else{
    				StatusController.setMessage("Keine passende Geste gefunden!", StatusController.ERROR);
    				Log.write("No Matching Gesture found!",this);
    			}
    		}	
        };
        
        recorder=new DataTraceRecorder(graphPanel);
        recorder.addGestureListener(mainListener);
	}
}

	

