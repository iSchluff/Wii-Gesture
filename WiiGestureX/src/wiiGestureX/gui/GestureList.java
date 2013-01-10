package wiiGestureX.gui;

import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GestureList extends JList {
	private static final long serialVersionUID = -1658694617543364629L;
	
	public GestureList(){
		//super(new String[]{"abc","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35"});
		super(MainGui.manager.getGestureNames());
		
		setPreferredSize(new Dimension(200,this.getPreferredSize().height));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//this.setVisibleRowCount(10);
		setCellRenderer(new GestureComponent());
		
		ListSelectionListener listener=new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				MainGui.propertyPanel.gestureSelected(((JList) evt.getSource()).getSelectedIndex());
			}
			
		};
		
		this.addListSelectionListener(listener);
	}
}