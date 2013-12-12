package at.ac.tuwien.cg.gesture.visualization.menu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.border.Border;


import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

import at.ac.tuwien.cg.gesture.control.InputHandler;
import at.ac.tuwien.cg.gesture.math.Vector3f;


import pagelayout.CellGrid;
import pagelayout.GridRows;

public class GForceMenuBar implements ItemListener,ActionListener{
	
	private JXPanel menuBar = null;
	
	//Device
	JRadioButton rDSmartphone;
	JRadioButton rDWii;
	JRadioButton rDAxis;
	public static boolean isDeviceSmartPhone =false;
	public static boolean isNoDevice =true;
	
	//Orientation
	JXLabel lOrientationX;
	JXLabel lOrientationY;
	JXLabel lOrientationZ;
	public static JXLabel lMOrientationX;
	public static JXLabel lMOrientationY;
	public static JXLabel lMOrientationZ;
	String orientationXText = "";
	String orientationYText = "";
	String orientationZText = "";
	Vector3f orientationVec = new Vector3f(0.0f, 0.0f, 0.0f);
	
	//Measurement 
	JXLabel lMeasurement;
	JRadioButton rMRealtime;
	JRadioButton rMManuell;
	JXButton bManuell = new JXButton("Abfrage");;
	JXLabel lMeasurementTime;
	public static boolean isMRealtime = false;
	public static boolean nextMeasurement = false;
	
	//G-Force
	JXLabel lGforceX;
	JXLabel lGforceY;
	JXLabel lGforceZ;
	public static JXLabel lMgForceX;
	public static JXLabel lMgForceY;
	public static JXLabel lMgForceZ;
	String gForceXText = "";
	String gForceYText = "";
	String gForceZText = "";
	
	//Acceleration
	JXLabel lAccelerationX;
	JXLabel lAccelerationY;
	JXLabel lAccelerationZ;
	public static JXLabel lMAccelerationX;
	public static JXLabel lMAccelerationY;
	public static JXLabel lMAccelerationZ;
	String accelerationXText = "";
	String accelerationYText = "";
	String accelerationZText = "";
	
	public GForceMenuBar(){
		this.menuBar = new JXPanel();
		buildMenuBar();
	}
	
	public void buildMenuBar(){
		
		//Device
		JXPanel pDevice = new JXPanel();
		Border borderD = BorderFactory.createEtchedBorder();
		borderD=BorderFactory.createTitledBorder(borderD,"Anzeige");
		
		pDevice.setBorder(borderD);
		rDAxis = new JRadioButton("Achsen");
		rDAxis.setName("rDAxis");
		rDAxis.addItemListener(this);
		rDAxis.setSelected(true);
		rDSmartphone = new JRadioButton("Smartphone");
		rDSmartphone.setName("rDSmartphone");
		rDSmartphone.addItemListener(this);
		rDWii = new JRadioButton("Wii-Controller");
		rDWii.setName("rDWii");
		rDWii.addItemListener(this);
		
		ButtonGroup bgD = new ButtonGroup();
		bgD.add(rDAxis);
		bgD.add(rDSmartphone);
		bgD.add(rDWii);
		
		GridRows gridRowsDevice = new GridRows();
		gridRowsDevice.newRow().add(rDAxis);
		gridRowsDevice.newRow().add(rDSmartphone);
		gridRowsDevice.newRow().add(rDWii);
		CellGrid cellgridDevice = gridRowsDevice.createCellGrid();
		cellgridDevice.createLayout(pDevice);
		
		//Orientation
		JXPanel pOrientation = new JXPanel();
		Border border = BorderFactory.createEtchedBorder();
		border=BorderFactory.createTitledBorder(border,"Orientation");
		
		pOrientation.setBorder(border);
		
		lOrientationX = new JXLabel("x : ");
		lOrientationY = new JXLabel("y : ");
		lOrientationZ = new JXLabel("z : ");
		lMOrientationX = new JXLabel("-");
		lMOrientationX.setPreferredSize(new Dimension(15, 50));
		lMOrientationY = new JXLabel("-");
		lMOrientationY.setPreferredSize(new Dimension(15, 50));
		lMOrientationZ = new JXLabel("-");
		lMOrientationZ.setPreferredSize(new Dimension(15, 50));
		
		GridRows gridRowsOrientation = new GridRows();
		gridRowsOrientation.newRow().add(lOrientationX,lMOrientationX);
		gridRowsOrientation.newRow().add(lOrientationY,lMOrientationY);
		gridRowsOrientation.newRow().add(lOrientationZ,lMOrientationZ);
		CellGrid cellgridOrientation = gridRowsOrientation.createCellGrid();
		cellgridOrientation.createLayout(pOrientation);
		
		//Measurement
		JXPanel pMeasurement = new JXPanel();
		Border borderM = BorderFactory.createEtchedBorder();
		borderM=BorderFactory.createTitledBorder(borderM,"Messungen");
		pMeasurement.setBorder(borderM);
		
		rMManuell = new JRadioButton("Manuelle Messung");
		rMManuell.setName("rMManuell");
		rMManuell.addItemListener(this);
		rMRealtime = new JRadioButton("Echtzeit Messung");
		rMRealtime.setName("rMRealtime");
		rMRealtime.addItemListener(this);
		rMRealtime.setSelected(true);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(rMRealtime);
		bg.add(rMManuell);
		
		
		bManuell.setActionCommand("bManuell");
		bManuell.addActionListener(this);
		
		GridRows gridRowsMeasurement = new GridRows();
		gridRowsMeasurement.newRow().add(rMRealtime);
		gridRowsMeasurement.newRow().add(rMManuell,bManuell);
		CellGrid cellgridMeasurement = gridRowsMeasurement.createCellGrid();
		cellgridMeasurement.createLayout(pMeasurement);
		
		//G-Force
		JXPanel pGforce = new JXPanel();
		Border borderGF = BorderFactory.createEtchedBorder();
		borderGF=BorderFactory.createTitledBorder(borderGF,"G-Kraft");
		
		pGforce.setBorder(borderGF);
		
		lGforceX = new JXLabel("x : ");
		lGforceY = new JXLabel("y : ");
		lGforceZ = new JXLabel("z : ");
		lMgForceX = new JXLabel("-");
		lMgForceX.setPreferredSize(new Dimension(15, 50));
		lMgForceY = new JXLabel("-");
		lMgForceY.setPreferredSize(new Dimension(15, 50));
		lMgForceZ = new JXLabel("-");
		lMgForceZ.setPreferredSize(new Dimension(15, 50));
		
		GridRows gridRowsGforce = new GridRows();
		gridRowsGforce.newRow().add(lGforceX,lMgForceX);
		gridRowsGforce.newRow().add(lGforceY,lMgForceY);
		gridRowsGforce.newRow().add(lGforceZ,lMgForceZ);
		CellGrid cellgridGforce = gridRowsGforce.createCellGrid();
		cellgridGforce.createLayout(pGforce);
		
		//Acceleration
		JXPanel pAcceleration = new JXPanel();
		Border bAcceleration = BorderFactory.createEtchedBorder();
		bAcceleration=BorderFactory.createTitledBorder(bAcceleration,"Beschleunigung in ");
		
		pAcceleration.setBorder(bAcceleration);
		
		lAccelerationX = new JXLabel("x : ");
		lAccelerationY = new JXLabel("y : ");
		lAccelerationZ = new JXLabel("z : ");
		lMAccelerationX = new JXLabel("-");
		lMAccelerationX.setPreferredSize(new Dimension(15, 50));
		lMAccelerationY = new JXLabel("-");
		lMAccelerationY.setPreferredSize(new Dimension(15, 50));
		lMAccelerationZ = new JXLabel("-");
		lMAccelerationZ.setPreferredSize(new Dimension(15, 50));
		
		GridRows gridRowsAcceleration = new GridRows();
		gridRowsAcceleration.newRow().add(lAccelerationX,lMAccelerationX);
		gridRowsAcceleration.newRow().add(lAccelerationY,lMAccelerationY);
		gridRowsAcceleration.newRow().add(lAccelerationZ,lMAccelerationZ);
		CellGrid cellgridAcceleration = gridRowsAcceleration.createCellGrid();
		cellgridAcceleration.createLayout(pAcceleration);
		
		//MenuBar
		GridRows gridRows = new GridRows();
		gridRows.newRow().add(pDevice);
		//gridRows.newRow().add(pOrientation);
		gridRows.newRow().add(pMeasurement);
		gridRows.newRow().add(pGforce);
		gridRows.newRow().add(pAcceleration);
		CellGrid cellgrid = gridRows.createCellGrid();
		cellgrid.createLayout(menuBar);
		
	}
	
	public JXPanel getMenuBar(){
		return menuBar;
	}

	
	public void actionPerformed(ActionEvent actionEvent) {
		String actionSrc = actionEvent.getActionCommand();
		//System.out.println(actionSrc);
		if(actionSrc.equals("bManuell"))
		{
			nextMeasurement = true;
			//System.out.println("Button");
		}
	}

	
	public void itemStateChanged(ItemEvent itemEvent) {
		
		Object source = itemEvent.getSource();
		
		if(source instanceof JRadioButton)
		{
			JRadioButton rb = (JRadioButton)source;
			if(rb.getName().equals("rMManuell") && rb.isSelected())
			{
				System.out.println("Manuell Selected");
				bManuell.setEnabled(true);
				isMRealtime=false;
			}
			else if(rb.getName().equals("rMRealtime") && rb.isSelected())
			{
				System.out.println("Realtime Selected");
				bManuell.setEnabled(false);
				isMRealtime=true;
			}
			else if(rb.getName().equals("rDSmartphone") && rb.isSelected())
			{
				System.out.println("Smartphone Selected");
				isDeviceSmartPhone=true;
				isNoDevice = false;

			}
			else if(rb.getName().equals("rDWii") && rb.isSelected())
			{
				System.out.println("Wii Selected");
				isDeviceSmartPhone=false;
				isNoDevice = false;
			}
			else if(rb.getName().equals("rDAxis") && rb.isSelected())
			{
				System.out.println("Achsen Selected");
				isDeviceSmartPhone=false;
				isNoDevice = true;
			}
		}
		
	}
	
}
