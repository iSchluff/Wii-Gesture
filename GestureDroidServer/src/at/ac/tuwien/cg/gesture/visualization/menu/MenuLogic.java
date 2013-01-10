package at.ac.tuwien.cg.gesture.visualization.menu;


import javax.swing.JTextField;

import at.ac.tuwien.cg.gesture.visualization.Preferences;




public class MenuLogic {
	
	private Menu menu = null;
	Preferences preferences;
	public static String statusText="";
	
	public MenuLogic(Menu menu,Preferences p){
		this.menu = menu;
		this.preferences = p;
	}
	
	public void resetTcpValues(){
		menu.tcpIpTextField.setText(preferences.getServerSocketIP());
		menu.tcpPortTextField.setText(preferences.getServerSocketPort());
	}
	
	public void resetDisplayOptions(){
		menu.displayShowFunction.setSelected(true);
		menu.displayShowAxis.setSelected(true);
		menu.displayShowGrid.setSelected(false);
		menu.displayGridTransparity.setValue(100);
		menu.displayShowPoints.setSelected(false);
		menu.displayPointsTransparity.setValue(100);
		menu.displayShowPhone.setSelected(false);
		menu.displayPhoneTransparity.setValue(100);
		menu.displayShowAxisArea.setSelected(false);
		menu.displayAxisAreaTransparity.setValue(100);
	}
	
	public void resetAnimationOptions(){
		menu.animationSpeedSlider.setValue(0);
	}

	public void initGForceVis(){
		GForceVis gForceVis = new GForceVis();
		gForceVis.setVisible(true);
		gForceVis.setAlwaysOnTop(true);
	}
}
