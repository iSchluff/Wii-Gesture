package wiiGestureX;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * @author Anton Schubert
 * 
 * Klasse zum Ausführen einer bestimmten Aktion bei der
 * Erkennung einer Geste.
 */
public class ActionExecutor {
	public static int KEYSIMULATIONMODE=1;
	public static int COMMANDEXECUTIONMODE=2;
	
	private String command="";
	private int key=KeyEvent.VK_UNDEFINED;
	private int modifiers=0;
	
	private int mode;
	private Robot robot;
	public boolean changed=false;
	
	public ActionExecutor(){
		mode=KEYSIMULATIONMODE;
		try {
			robot=new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Legt den Aktionsmodus fest
	 */
	public void setMode(int mode){
		this.mode=mode;
		this.changed=true;
	}
	
	public int getMode(){
		return this.mode;
	}
	
	/**
	 * Setzt die zu drückende Taste inklusive key modifiers
	 */
	public void setKeyAction(int key,int modifiers){
		this.key=key;
		this.modifiers=modifiers;
		this.changed=true;
	}
	
	public int getKey(){
		return this.key;
	}
	
	public int getModifiers(){
		return this.modifiers;
	}
	
	/**
	 * Setzt den auszuführenden Kommandozeilenbefehl
	 */
	public void setCommand(String command){
		this.command=command;
		this.changed=true;
	}
	
	public String getCommand(){
		return this.command;
	}
	
	
	/**
	 * Simuliert abhängig vom derzeitigen Modus einen Tastendruck oder
	 * führt einen Kommandozeilenbefehl aus
	 */
	public void execute(){
		if(mode==KEYSIMULATIONMODE && key!=0){
			//modifiers drücken
			if((modifiers & KeyEvent.CTRL_MASK)!=0) robot.keyPress(KeyEvent.VK_CONTROL);
			if((modifiers & KeyEvent.ALT_MASK)!=0) robot.keyPress(KeyEvent.VK_ALT);
			if((modifiers & KeyEvent.ALT_GRAPH_MASK)!=0) robot.keyPress(KeyEvent.VK_ALT_GRAPH);
			if((modifiers & KeyEvent.SHIFT_MASK)!=0) robot.keyPress(KeyEvent.VK_SHIFT);
			//key
			robot.keyPress(key);
			robot.keyRelease(key);
			//modifiers releasen
			if((modifiers & KeyEvent.CTRL_MASK)!=0) robot.keyRelease(KeyEvent.VK_CONTROL);
			if((modifiers & KeyEvent.ALT_MASK)!=0) robot.keyRelease(KeyEvent.VK_ALT);
			if((modifiers & KeyEvent.ALT_GRAPH_MASK)!=0) robot.keyRelease(KeyEvent.VK_ALT_GRAPH);
			if((modifiers & KeyEvent.SHIFT_MASK)!=0) robot.keyRelease(KeyEvent.VK_SHIFT);
			
		}else if(mode==COMMANDEXECUTIONMODE){
			try {
				@SuppressWarnings("unused")
				Process child= Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Gibt den String zurück, der im entsprechenden Action-Field angezeigt wird
	 */
	public String getActionString(){
		if(mode==KEYSIMULATIONMODE){
			String s="";
			if(key!=KeyEvent.VK_UNDEFINED){
				if(modifiers!=0)
					s+=KeyEvent.getKeyModifiersText(modifiers)+"+";
				s+=KeyEvent.getKeyText(key);
			}
			return s;
		}else if(mode==COMMANDEXECUTIONMODE){
			return command;
		}else{
			return "";
		}
	}

}
