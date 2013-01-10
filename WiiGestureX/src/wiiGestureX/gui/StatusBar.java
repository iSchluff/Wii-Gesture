package wiiGestureX.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class StatusBar extends JPanel {
	private static final long serialVersionUID = -3519920173870678330L;
	private JLabel label;
	public StatusBar(){
		GridBagLayout gbl=new GridBagLayout();
		GridBagConstraints c=new GridBagConstraints();
		setLayout(gbl);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		setBackground(Color.gray);
		
		
		label=new JLabel("test");
		//label.setHorizontalAlignment(JLabel.LEFT);
		//label.setHorizontalTextPosition(JLabel.LEFT);
		//label.setVerticalAlignment(JLabel.TOP);
		//label.setBorder(BorderFactory.createLineBorder(Color.gray));
		//c.anchor=GridBagConstraints.WEST;
		c.insets=new Insets(5,5,5,5);
		gbl.setConstraints(label, c);
		add(label,c);
	}
	
	public void setText(String text,Color c){
		
		label.setText(text);
		label.setForeground(c);
		//Log.write(label.getBounds().toString(), label);
	}

}
