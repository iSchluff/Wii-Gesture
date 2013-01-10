package wiiGestureX.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import wiiGestureX.Gesture;
import wiiGestureX.GestureType;
import wiiGestureX.events.GestureEvent;
import wiiGestureX.events.GestureListener;
import wiiGestureX.util.Log;

public class ActionBar extends JPanel {
	private static final long serialVersionUID = 7082953353733940886L;
	private Vector<Gesture> gestures=new Vector<Gesture>();
	
	private JButton recordButton, deleteButton, connectButton;
	
	public ActionBar(){
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		//layout
		FlowLayout flowlayout=new FlowLayout();
		flowlayout.setAlignment(FlowLayout.LEFT);
		flowlayout.setHgap(25);
		setLayout(flowlayout);
		
		//listeners
		ActionListener connectListener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				Log.write("Searching Mote ...",this);
				StatusController.setMessage("Press 1 and 2 to connect the Wiimote", StatusController.PROGRESS);
				if(MainGui.recorder.connectMote()){
					Log.write("Mote successfully connected!", this);
					StatusController.setMessage("Mote successfully connected", StatusController.SUCCESS);
					recordButton.setEnabled(true);
					connectButton.setEnabled(false);
				}else{
					Log.write("Connection to Mote failed!", this);
					StatusController.setMessage("Connection to Mote failed", StatusController.ERROR);
				}
				
			}
		};
		
		final JPanel panel=this;
		
		final GestureListener gRecordListener=new GestureListener(){
			@Override
			public void GestureRecorded(GestureEvent event) {
				gestures.add(event.getGesture());
				if(gestures.size()<10){
					StatusController.setMessage(gestures.size()+"/10 gestures trained", StatusController.PROGRESS);
				}else{
					StatusController.setMessage("10/10 gestures trained", StatusController.SUCCESS);
				       Object response = JOptionPane.showInputDialog(panel, "Enter a Name for the Gesture!", "New GestureType");
				       System.out.println(response);
				       createType(String.valueOf(response));
				       MainGui.recorder.addGestureListener(MainGui.mainListener);
				       MainGui.recorder.removeGestureListener(this); 
				}
			}
		};
		
		ActionListener recordListener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				MainGui.recorder.removeGestureListener(MainGui.mainListener);
				MainGui.recorder.addGestureListener(gRecordListener);
				StatusController.setMessage("0/10 gestures trained", StatusController.PROGRESS);
			}
		};
		
		ActionListener deleteListener=new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				int index=MainGui.gestureList.getSelectedIndex();
				MainGui.manager.deleteGestureType(index);
			}
			
		};
		
		
		//buttons
		connectButton=new JButton("Connect Wiimote");
		connectButton.addActionListener(connectListener);
		add(connectButton);
		
		recordButton=new JButton("Record Gesture");
		recordButton.addActionListener(recordListener);
		recordButton.setEnabled(false);
		add(recordButton);
		
		deleteButton=new JButton("Delete Gesture");
		deleteButton.addActionListener(deleteListener);
		add(deleteButton);
	}
	
	public void createType(String name){
		name=name.toLowerCase();
		Log.write("Erstellung des Gestentyps: "+name,this);
		GestureType type=new GestureType(name);
		type.train(gestures);
		MainGui.manager.addGestureType(type);
		gestures=new Vector<Gesture>();
	}
}
