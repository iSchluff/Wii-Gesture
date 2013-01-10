package wiiGestureX.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import wiiGestureX.ActionExecutor;
import wiiGestureX.GestureType;

public class PropertyPanel extends JPanel{
	private static final long serialVersionUID = -1158742228294681855L;
	GridBagLayout layout;
	private GestureType currentGesture;
	private JLabel nameLabel, actionLabel;
	private JTextField nameField, actionField;
	private JButton editNameButton, editActionButton;
	private JToggleButton commandActionButton, keyActionButton;
	private ButtonGroup bGroup;
	

	public PropertyPanel(){
		layout=new GridBagLayout();
		setLayout(layout);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		/* ---- Name Row ---- */
		nameLabel=new JLabel("Name:");
		nameLabel.setHorizontalTextPosition(JLabel.RIGHT);
		addComponent(nameLabel,new Insets(0,0,0,0),0,0,1,1,0.1,1);
		
		nameField=new JTextField();
		nameField.setEditable(false);
		nameField.setFocusable(false);
		addComponent(nameField,new Insets(0,0,0,0),1,0,2,1,1,1,GridBagConstraints.HORIZONTAL);
		
		/* editNameButton */
		editNameButton=new JButton("Edit");
		addComponent(editNameButton,new Insets(0,20,0,20),3,0,1,1,0.1,1,GridBagConstraints.HORIZONTAL);
		
		createActionRow();
		
		createActionSelectButtons();
	}
	
	private void createActionRow(){
		actionLabel=new JLabel("Action:");
		actionLabel.setHorizontalTextPosition(JLabel.RIGHT);
		addComponent(actionLabel,new Insets(0,0,0,0),0,1,1,1,0,1);
		
		actionField=new JTextField();
		actionField.setEditable(false);
		actionField.setFocusable(false);
		actionField.setFocusTraversalKeysEnabled(false);
		addComponent(actionField,new Insets(0,0,0,0),1,1,2,1,0.2,1,GridBagConstraints.HORIZONTAL);
		
		/* editActionButton */
		editActionButton=new JButton("Edit");
		final KeyListener keyListener=new KeyListener(){
			@Override
			public void keyReleased(KeyEvent event) {
				ActionExecutor executor=currentGesture.getExecutor();
				if(executor.getMode()==ActionExecutor.KEYSIMULATIONMODE){
					executor.setKeyAction(event.getKeyCode(), event.getModifiers());
					actionField.setText(executor.getActionString());
					actionField.removeKeyListener(this);
					actionField.setFocusable(false);
					
				}else if(executor.getMode()==ActionExecutor.COMMANDEXECUTIONMODE){
					if(event.getKeyCode()==KeyEvent.VK_ENTER){
						executor.setCommand(actionField.getText());
						actionField.removeKeyListener(this);
						actionField.setFocusable(false);
						actionField.setEditable(false);
					}
				}
			}
			@Override
			public void keyPressed(KeyEvent event) {}
			@Override
			public void keyTyped(KeyEvent event) {}
			
		};
		
		ActionListener editActionListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				if(currentGesture!=null){
					actionField.setFocusable(true);
					if(currentGesture.getExecutor().getMode()==ActionExecutor.COMMANDEXECUTIONMODE){
						actionField.setEditable(true);
					}
					actionField.addKeyListener(keyListener);
					actionField.requestFocus();
					}	
				}
			};
		
		
		editActionButton.addActionListener(editActionListener);
		addComponent(editActionButton,new Insets(0,20,0,20),3,1,1,1,0.15,1,GridBagConstraints.HORIZONTAL);
	}
	
	private void createActionSelectButtons(){
		bGroup=new ButtonGroup();
		
		/* clickListeners */
		ActionListener keyActionBtnListener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evt) {
				changeActionMode(ActionExecutor.KEYSIMULATIONMODE);
			}
		};
		
		ActionListener commandActionBtnListener=new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent evt) {
				changeActionMode(ActionExecutor.COMMANDEXECUTIONMODE);
			}
		};
		
		/* Mode-Buttons */
		keyActionButton=new JToggleButton("Key-Action");
		addComponent(keyActionButton, new Insets(0,30,0,30),1,2,1,1,0.3,1,GridBagConstraints.HORIZONTAL);
		keyActionButton.addActionListener(keyActionBtnListener);
		bGroup.add(keyActionButton);
		
		commandActionButton=new JToggleButton("Command-Action");
		addComponent(commandActionButton, new Insets(0,30,0,30),2,2,1,1,0.3,1,GridBagConstraints.HORIZONTAL);
		commandActionButton.addActionListener(commandActionBtnListener);
		bGroup.add(commandActionButton);
	}
	
	private void changeActionMode(int mode){
		currentGesture.getExecutor().setMode(mode);
		actionField.setText(currentGesture.getExecutor().getActionString());
	}
	
	public void gestureSelected(int index){
		currentGesture=MainGui.manager.getGestureType(index);
		nameField.setText(currentGesture.getName());
		actionField.setText(currentGesture.getExecutor().getActionString());
		int mode=currentGesture.getExecutor().getMode();
		if(mode==ActionExecutor.KEYSIMULATIONMODE){
			keyActionButton.setSelected(true);
		}else if(mode==ActionExecutor.COMMANDEXECUTIONMODE){
			commandActionButton.setSelected(true);
		}
	}
	
	
	private void addComponent(JComponent component,Insets insets, int x, int y, int width, int height, double weightx, double weighty){
		addComponent(component, insets,  x,  y,  width,  height,  weightx,  weighty, Integer.MAX_VALUE);
	}
	
	private void addComponent(JComponent component,Insets insets , int x, int y, int width, int height, double weightx, double weighty, int fill){
		GridBagConstraints c=new GridBagConstraints();
		c.gridx=x;c.gridy=y;
		c.gridwidth=width;c.gridheight=height;
		c.weightx=weightx;c.weighty=weighty;
		c.insets=insets;
		if(fill!=Integer.MAX_VALUE){
			c.fill=fill;
		}
		layout.setConstraints(component, c);
		add(component);
	}

}
