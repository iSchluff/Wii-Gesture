package at.ac.tuwien.cg.gesture.visualization.menu;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.MenuBar;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;


import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXCollapsiblePane.Orientation;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;

import at.ac.tuwien.cg.gesture.visualization.Preferences;

import pagelayout.Cell;
import pagelayout.CellGrid;
import pagelayout.Column;
import pagelayout.GridRows;
import pagelayout.PageLayout;
import pagelayout.Row;


public class Menu implements ActionListener,ChangeListener,ItemListener{
	
	private JXPanel menuFrame = null;
	private JScrollPane scrollPane = null;
	private JPanel statusPanel = null;
	private JMenuBar menuBar = null;
	
	//Status
	JTextArea statusTextArea = null;
	private JLabel statusLabel = new JLabel("Status: ");
	
	//Connection
	JTextField tcpIpTextField;
	JTextField tcpPortTextField;
	JXButton tcpConnectButton;
	JXButton tcpDisconnectButton;
	JXButton tcpStandardValueButton;
	JRadioButton tcpRadioButton;
	
	//Animation
	JXButton animationStart;
	JXButton animationStandard;
	JSlider animationSpeedSlider;
	JXLabel animationDurationInSecLabel;
	
	//Display
	JCheckBox displayShowPhone;
	JCheckBox displayShowAxis;
	JCheckBox displayShowGrid;
	JCheckBox displayShowPoints;
	JCheckBox displayShowFunction;
	JCheckBox displayShowAxisArea;
	JSlider displayPhoneTransparity;
	JSlider displayGridTransparity;
	JSlider displayPointsTransparity;
	JSlider displayAxisAreaTransparity;
	JXButton displayStandard;
	
	//Info
	JXLabel infoAmountOfPoints;
	JXLabel infoDuration;
	JXLabel infoDistance;
	JXLabel infoDifference;
	JXLabel infoClassification;
	JXLabel infoSource;
	
	
	MenuLogic menuLogic = null;
	Preferences p = null;
	
	
	public Menu(Preferences p){
		this.menuFrame = new JXPanel(new VerticalLayout(4));
		this.p = p;
		buildMenuPanel();
		buildMenuBar();
	}
	
	public void buildMenuBar(){
		menuBar = new JMenuBar();
		
		JMenu dataMenu = new JMenu("Daten");
		JMenu helpMenu = new JMenu("Hilfe");
		
		JMenuItem menuNewData = new JMenuItem("Neue Daten");
		menuNewData.addActionListener(this);
		menuNewData.setActionCommand("menuNewData");
		JMenuItem menuImportData = new JMenuItem("Importieren...");
		menuImportData.addActionListener(this);
		menuImportData.setActionCommand("menuImportData");
		JMenuItem menuExportData = new JMenuItem("Exportieren...");
		menuExportData.addActionListener(this);
		menuExportData.setActionCommand("menuExportData");
		JMenuItem menuClose = new JMenuItem("Beenden");
		menuClose.addActionListener(this);
		menuClose.setActionCommand("menuClose");
		
		menuBar.add(dataMenu);
		menuBar.add(helpMenu);
		
		dataMenu.add(menuNewData);
		dataMenu.add(menuImportData);
		dataMenu.add(menuExportData);
		dataMenu.add(menuClose);
	}
	
	/** 
	 * The whole menu-panel will be constructed
	 */
	public void buildMenuPanel(){
						
		//Display
		JXCollapsiblePane cpDisplay = new JXCollapsiblePane(Orientation.VERTICAL);
		cpDisplay.setAnimated(true);
		cpDisplay.setCollapsed(false);
		buildDisplayOptions(cpDisplay);
		JButton displayButton = new JButton(cpDisplay.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
		displayButton.setText("Display");
		menuFrame.add(displayButton);
		menuFrame.add(cpDisplay);
		
		
		//Animation
		JXCollapsiblePane cpAnimation = new JXCollapsiblePane(Orientation.VERTICAL);
		cpAnimation.setAnimated(true);
		cpAnimation.setCollapsed(true);
		buildAnimationOptions(cpAnimation);
		JButton animationButton = new JButton(cpAnimation.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
		animationButton.setText("Animation");		
		menuFrame.add(animationButton);
		menuFrame.add(cpAnimation);
		
		
		//Verbindung
		JXCollapsiblePane cpConnection = new JXCollapsiblePane(Orientation.VERTICAL);
		cpConnection.setAnimated(true);
		cpConnection.setCollapsed(true);
		buildConnectionOptions(cpConnection);
		JButton connectionButton = new JButton(cpConnection.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
		connectionButton.setText("Verbindung");		
		menuFrame.add(connectionButton);
		menuFrame.add(cpConnection);
		
		
		//Information
		JXCollapsiblePane cpInformation = new JXCollapsiblePane(Orientation.VERTICAL);
		cpInformation.setAnimated(true);
		cpInformation.setCollapsed(true);
		buildInformationOptions(cpInformation);
		JButton informationButton = new JButton(cpInformation.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
		informationButton.setText("Information");		
		menuFrame.add(informationButton);
		menuFrame.add(cpInformation);
		
		
		//Features
		/*
		JXCollapsiblePane cpFeatures = new JXCollapsiblePane(Orientation.VERTICAL);
		cpFeatures.setAnimated(true);
		cpFeatures.setCollapsed(true);
		buildConnectionOptions(cpFeatures);
		JButton informationButton = new JButton(cpInformation.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
		informationButton.setText("Information");		
		menuFrame.add(informationButton);
		menuFrame.add(cpInformation);
		*/
		
		/*
		JButton skipButton = new JButton();
		skipButton.setVisible(false);
		menuFrame.add(skipButton);
		*/
		
		JButton gForceButton = new JButton();
		gForceButton.setText("Lage-Visualisierung");
		gForceButton.addActionListener(this);
		menuFrame.add(gForceButton);
		
		JButton featuresButton = new JButton();
		featuresButton.setText("Merkmale");
		featuresButton.addActionListener(this);
		menuFrame.add(featuresButton);
		
		//menuColumn.add(test,displayButton,cpDisplay,animationButton,cpAnimation);
		 			
	}
	
	private void buildDisplayOptions(JXCollapsiblePane cp){
		JXPanel controls = new JXPanel();
		Border border = BorderFactory.createEtchedBorder();
		border=BorderFactory.createTitledBorder(border,"Optionen");
		controls.setBorder(border);
		
		JXLabel choice = new JXLabel("Auswahl");
		choice.setHorizontalTextPosition(SwingConstants.CENTER);
		//choice.setFont(choice.getFont().deriveFont(choice.getFont().getStyle() ^Font.BOLD));
		JXLabel transparity = new JXLabel("Transparenz");
		transparity.setHorizontalTextPosition(SwingConstants.CENTER);
		
		JXLabel lowAlpha = new JXLabel("0%", SwingConstants.LEFT);
		lowAlpha.setFont(lowAlpha.getFont().deriveFont(lowAlpha.getFont().getStyle() ^Font.BOLD));
		JXLabel highAlpha = new JXLabel("100%", SwingConstants.RIGHT);
		highAlpha.setFont(highAlpha.getFont().deriveFont(highAlpha.getFont().getStyle() ^Font.BOLD));
		
		displayShowFunction = new JCheckBox("Funktion");
		displayShowFunction.setSelected(p.getShowFunction());
		displayShowFunction.setName("displayFunction");
		displayShowFunction.addItemListener(this);
		displayShowAxis = new JCheckBox("Achsen");
		displayShowAxis.setName("displayShowAxis");
		displayShowAxis.setSelected(p.getShowAxis());
		displayShowAxis.addItemListener(this);
		displayShowGrid = new JCheckBox("Gitternetz");
		displayShowGrid.setName("displayShowGrid");
		displayShowGrid.setSelected(p.getShowGrid());
		displayShowGrid.addItemListener(this);
		displayShowPoints = new JCheckBox("Messpunkte");
		displayShowPoints.setName("displayShowPoints");
		displayShowPoints.setSelected(p.getShowPoints());
		displayShowPoints.addItemListener(this);
		displayShowPhone = new JCheckBox("Smartphone");
		displayShowPhone.setName("displayShowPhone");
		displayShowPhone.setSelected(p.getShowPhone());
		displayShowPhone.addItemListener(this);
		displayShowAxisArea = new JCheckBox("Achsenflächen");
		displayShowAxisArea.setName("displayShowAxisArea");
		displayShowAxisArea.setSelected(p.getShowAxisArea());
		displayShowAxisArea.addItemListener(this);
		
		displayGridTransparity = new JSlider(SwingConstants.HORIZONTAL, 0, 100, (int)p.getDisplayStandardGridAlpha());
		displayGridTransparity.setMajorTickSpacing(10);
		displayGridTransparity.setEnabled(p.getShowGrid());
		displayGridTransparity.addChangeListener(this);
		displayGridTransparity.setName("displayGridTransparity");
		displayPointsTransparity = new JSlider(SwingConstants.HORIZONTAL, 0, 100, (int)p.getDisplayStandardPointsAlpha());
		displayPointsTransparity.setMajorTickSpacing(10);
		displayPointsTransparity.setEnabled(p.getShowPoints());
		displayPointsTransparity.addChangeListener(this);
		displayPointsTransparity.setName("displayPointsTransparity");
		displayPhoneTransparity = new JSlider(SwingConstants.HORIZONTAL, 0, 100, (int)p.getDisplayStandardPhoneAlpha());
		displayPhoneTransparity.setMajorTickSpacing(10);
		displayPhoneTransparity.setEnabled(p.getShowPhone());
		displayPhoneTransparity.addChangeListener(this);
		displayPhoneTransparity.setName("displayPhoneTransparity");
		displayAxisAreaTransparity = new JSlider(SwingConstants.HORIZONTAL, 0, 100, (int)p.getDisplayStandardAxisAreaAlpha());
		displayAxisAreaTransparity.setMajorTickSpacing(10);
		displayAxisAreaTransparity.setEnabled(p.getShowAxisArea());
		displayAxisAreaTransparity.addChangeListener(this);
		displayAxisAreaTransparity.setName("displayAxisAreaTransparity");
		
		displayStandard = new JXButton("Standard");
		displayStandard.addActionListener(this);
		displayStandard.setActionCommand("displayStandard");
		
		Cell onlyHighLabel = new Row(Cell.RIGHT,Cell.NO_ALIGNMENT,highAlpha);
		Cell onlyLowLevel = new Row(Cell.LEFT,Cell.NO_ALIGNMENT,lowAlpha);
		Cell percentLabel = new Row(onlyLowLevel,onlyHighLabel);
		
		GridRows gridRows = new GridRows();
		gridRows.newRow().add(Cell.CENTER,Cell.NO_ALIGNMENT,choice,transparity);
		gridRows.newRow().skip(1).add(percentLabel);
		gridRows.newRow().add(displayShowFunction);
		gridRows.newRow().add(displayShowAxis);
		gridRows.newRow().add(displayShowGrid,displayGridTransparity);
		gridRows.newRow().add(displayShowPoints,displayPointsTransparity);
		gridRows.newRow().add(displayShowPhone,displayPhoneTransparity);
		gridRows.newRow().add(displayShowAxisArea,displayAxisAreaTransparity);
		gridRows.newRow().add(displayStandard);
		
		CellGrid cellgrid = gridRows.createCellGrid();
		
		cellgrid.alignBaseline(displayShowGrid,displayGridTransparity);
		
		cellgrid.createLayout(controls);
		
		cp.add("Center",controls);
		
	}
	
	private void buildAnimationOptions(JXCollapsiblePane cp){
		JXPanel controls = new JXPanel();
		Border border = BorderFactory.createEtchedBorder();
		border=BorderFactory.createTitledBorder(border,"Animation");
		controls.setBorder(border);
		
		JXLabel animationSpeedLabel = new JXLabel("Geschwindigkeit:");
		JXLabel animationDurationLabel = new JXLabel("Dauer: ");
		animationDurationInSecLabel = new JXLabel("xxs");
		
		animationStart = new JXButton("Start");
		animationStart.addActionListener(this);
		animationStart.setActionCommand("animationStart");
		animationStandard = new JXButton("Standard");
		animationStandard.addActionListener(this);
		animationStandard.setActionCommand("animationStandard");
		
		animationSpeedSlider = new JSlider(SwingConstants.HORIZONTAL);
		animationSpeedSlider.setMinimum(p.getAnimationMinSpeed());
		animationSpeedSlider.setMaximum(p.getAnimationMaxSpeed());
		animationSpeedSlider.setValue(0);
		animationSpeedSlider.setMajorTickSpacing(p.getAnimationSpeedStep());
		
		animationSpeedSlider.addChangeListener(this);
		animationSpeedSlider.setName("animationSpeed");
		
		GridRows gridRows = new GridRows();
		gridRows.newRow().add(animationSpeedLabel,animationSpeedSlider);
		gridRows.newRow().add(animationDurationLabel,animationDurationInSecLabel);
		gridRows.newRow().add(animationStandard,animationStart);
		
		CellGrid cellgrid = gridRows.createCellGrid();
		cellgrid.createLayout(controls);
		
		cp.add("Center",controls);
	}
	
	private void buildConnectionOptions(JXCollapsiblePane cp){
		JXPanel controls = new JXPanel();
		Border border = BorderFactory.createEtchedBorder();
		border=BorderFactory.createTitledBorder(border,"TCP");
		controls.setBorder(border);
		
		tcpRadioButton = new JRadioButton("TCP-Verbindung");
		tcpRadioButton.addChangeListener(this);
		tcpRadioButton.setEnabled(false);
		tcpRadioButton.setSelected(true);
		
		JXLabel tcpIpLabel = new JXLabel("IP-Adresse: ");
		tcpIpTextField = new JTextField();
		tcpIpTextField.setDocument(new TextLimit(15));
		tcpIpTextField = new JTextField(p.getServerSocketIP());
		
		JXLabel tcpPortLabel = new JXLabel("Port: ");
		tcpPortTextField = new JTextField();
		tcpPortTextField.setDocument(new TextLimit(5));
		tcpPortTextField.setText(p.getServerSocketPort());
	
		
		tcpConnectButton= new JXButton("Start");
		tcpConnectButton.addActionListener(this);
		tcpConnectButton.setActionCommand("tcpConnect");
		tcpDisconnectButton = new JXButton("Trennen");
		tcpDisconnectButton.setActionCommand("tcpDisconnect");
		tcpDisconnectButton.addActionListener(this);
		tcpStandardValueButton = new JXButton("Standard");
		tcpStandardValueButton.setActionCommand("tcpStandardValue");
		tcpStandardValueButton.addActionListener(this);
		
		Cell cell = new Row(Cell.CENTER,Cell.BASELINE,tcpConnectButton,tcpDisconnectButton);
		
		GridRows gridRows = new GridRows();
		
		gridRows.newRow().add(tcpRadioButton);
		gridRows.newRow().add(tcpIpLabel,tcpIpTextField);
		gridRows.newRow().add(tcpPortLabel,tcpPortTextField);
		gridRows.newRow().add(tcpStandardValueButton).add(cell);
		
		CellGrid cellgrid = gridRows.createCellGrid();
		cellgrid.createLayout(controls);
		
		cp.add("Center",controls);
	}
	
	private void buildInformationOptions(JXCollapsiblePane cp){
		JXPanel controls = new JXPanel();
		Border border = BorderFactory.createEtchedBorder();
		border=BorderFactory.createTitledBorder(border,"Messungsinformationen");
		controls.setBorder(border);
		
		JXLabel infoClassificationLabel = new JXLabel("Klasse: ");
		infoClassification = new JXLabel("-");
		JXLabel infoAmountOfPointsLabel = new JXLabel("Anzahl der Messpunkte: ");
		infoAmountOfPoints = new JXLabel("-");
		JXLabel infoDurationLabel = new JXLabel("Dauer: ");
		infoDuration = new JXLabel("- s");
		JXLabel infoDistanceLabel = new JXLabel("Distanz: ");
		infoDistance = new JXLabel("- m");
		JXLabel infoDifferenceLabel = new JXLabel("Änderungsgrad: ");
		infoDifference = new JXLabel("- %");
		JXLabel infoSourceLabel = new JXLabel("Quelle: ");
		infoSource = new JXLabel("Smartphone / Wii");
		
		GridRows gridRows = new GridRows();
		
		Column columnLabel = new Column();
		columnLabel.add(infoClassificationLabel,infoAmountOfPointsLabel,infoDurationLabel,
						infoDistanceLabel,infoDifferenceLabel,infoSourceLabel);
		
		Column columnValues = new Column(Cell.CENTER, Cell.NO_ALIGNMENT);
		columnValues.add(infoClassification,infoAmountOfPoints,infoDuration,
						 infoDistance,infoDifference,infoSource);
		gridRows.newRow().add(columnLabel,columnValues);
		
		/*
		gridRows.newRow().add(infoClassificationLabel,infoClassification);
		gridRows.newRow().add(infoAmountOfPointsLabel,infoAmountOfPoints);
		gridRows.newRow().add(infoDurationLabel,infoDuration);
		gridRows.newRow().add(infoDistanceLabel,infoDistance);
		gridRows.newRow().add(infoDifferenceLabel,infoDifference);
		gridRows.newRow().add(infoSourceLabel,infoSource);
		*/
		CellGrid cellgrid = gridRows.createCellGrid();
		cellgrid.createLayout(controls);
		
		cp.add("Center",controls);
		
	}
	
	public JPanel getStatusPanel(){
		
		statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
		
		statusTextArea = new JTextArea(3, 70);
		statusTextArea.setLineWrap(true);
		statusTextArea.setWrapStyleWord(true);
		statusTextArea.setEditable(false);
		
		JScrollPane areaScrollPane = new JScrollPane(statusTextArea);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//TODO
		//Größe richtig anpassen
		statusLabel.setEnabled(false);
		statusPanel.add(statusLabel);
		statusPanel.add(areaScrollPane);
		
		return statusPanel;
	}
	
	
	public JScrollPane getScrollPane(){
		
		scrollPane = new JScrollPane(menuFrame, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		return scrollPane;
	}
	
	public void initMenuLogic(){
		menuLogic = new MenuLogic(this,p);
	}
	
	public MenuLogic getMenuLogic(){
		return menuLogic;
	}
	
	public JMenuBar getMenuBar(){
		return menuBar;
	}


	
	public void actionPerformed(ActionEvent actionEvent) {
		
		String actionSrc = actionEvent.getActionCommand();
		System.out.println(actionSrc);
		MenuLogic.statusText += (actionSrc+"\n");
		if(actionSrc.equals("tcpStandardValue"))
		{
			menuLogic.resetTcpValues();
		}
		else if(actionSrc.equals("displayStandard"))
		{
			menuLogic.resetDisplayOptions();
		}
		else if(actionSrc.equals("animationStandard"))
		{
			menuLogic.resetAnimationOptions();
		}
		else if(actionSrc.equals("tcpConnect"))
		{
			
		}
		else if(actionSrc.equals("tcpDisconnect"))
		{
			
		}
		else if(actionSrc.equals("displayStandard"))
		{
			
		}
		else if(actionSrc.equals("Lage-Visualisierung"))
		{
			menuLogic.initGForceVis();
		}
		else if(actionSrc.equals("Merkmale"))
		{
			
		}
		statusTextArea.setText(MenuLogic.statusText);
	}

	
	public void stateChanged(ChangeEvent e) {
		
		Object source = e.getSource();
		
		if(source instanceof JSlider)
		{
			JSlider slider = (JSlider)source;
			if(!slider.getValueIsAdjusting()){
				
				String sliderName = slider.getName();
				System.out.println(sliderName+" "+slider.getValue());
				
				if(sliderName.equals("displayGridTransparity"))
					p.setDisplayGridAlpha((float)slider.getValue()/100);
				else if(sliderName.equals("displayPointsTransparity"))
					p.setDisplayPointsAlpha((float)slider.getValue()/100);
				else if(sliderName.equals("displayPhoneTransparity"))
					p.setDisplayPhoneAlpha((float)slider.getValue()/100);
				else if(sliderName.equals("displayAxisAreaTransparity"))
					p.setDisplayAxisAreaAlpha((float)slider.getValue()/100);
				else if(sliderName.equals("animationSpeed"))
					p.setAnimationSpeedFac(slider.getValue());
			}
		}
	
		
	}

	
	public void itemStateChanged(ItemEvent e) {
		
		Object source = e.getSource();
		
		if(source instanceof JCheckBox)
		{
			JCheckBox checkBox = (JCheckBox)source;
			String checkBoxName = checkBox.getName();
			System.out.println("CheckBox "+checkBoxName);
			boolean isSelected = checkBox.isSelected();
			
			if(checkBoxName.equals("displayShowAxis"))
			{
				p.setShowAxis(isSelected);		
			}
			else if(checkBoxName.equals("displayShowGrid"))
			{
				displayGridTransparity.setEnabled(isSelected);
				p.setShowGrid(isSelected);
			}
			else if(checkBoxName.equals("displayShowPoints"))
			{
				displayPointsTransparity.setEnabled(isSelected);
				p.setShowPoints(isSelected);
			}
			else if(checkBoxName.equals("displayShowPhone"))
			{
				displayPhoneTransparity.setEnabled(isSelected);
				p.setShowPhone(isSelected);
			}
			else if(checkBoxName.equals("displayShowAxisArea"))
			{
				displayAxisAreaTransparity.setEnabled(isSelected);
				p.setShowAxisArea(isSelected);
			}
		}
		
	}
}
