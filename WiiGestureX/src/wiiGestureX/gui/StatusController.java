package wiiGestureX.gui;

import java.awt.Color;

public class StatusController {
	private static StatusBar bar;
	
	public static final int SUCCESS=1;
	public static final int PROGRESS=2;
	public static final int ERROR=3;
	
	public static void setStatusBar(StatusBar bar){
		StatusController.bar=bar;
	}
	
	public static void setMessage(String text,int messageType){
		Color c=Color.white;
		
		switch(messageType){
		case SUCCESS: c=Color.decode("0x5BE300");break;
		case PROGRESS: c=Color.decode("0xFFD900");break;
		case ERROR: c=Color.decode("0xFF0012");
		}
		
		bar.setText(text, c);
	}
}
