package wiiGestureX.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

/**
 * @author Anton Schubert
 * 
 * Repräsentiert einen einzelnen Gestentyp in der Gestenliste
 *
 */
public class GestureComponent extends JPanel implements ListCellRenderer{
	private static final long serialVersionUID = 1L;
	private JLabel nameLabel, actionLabel, text1, text2;
	private GridBagLayout layout;
	
	public GestureComponent(){
		this.setPreferredSize(new Dimension(150,60));
		layout=new GridBagLayout();
		setLayout(layout);

		text1=new JLabel("Name:");
		text1.setHorizontalTextPosition(JLabel.LEFT);
		addComponent(text1, new Insets(0,20,0,20),0,0,1,1,0,1,Integer.MAX_VALUE);
		
		nameLabel=new JLabel();
		nameLabel.setHorizontalTextPosition(JLabel.LEFT);
		nameLabel.setHorizontalAlignment(JLabel.LEFT);
		addComponent(nameLabel, new Insets(0,0,0,0),1,0,1,1,1,1,GridBagConstraints.HORIZONTAL);
		
		text2=new JLabel("Action:");
		text2.setHorizontalTextPosition(JLabel.LEFT);
		addComponent(text2, new Insets(0,20,0,20),0,1,1,1,0,1,Integer.MAX_VALUE);
		
		actionLabel=new JLabel();
		actionLabel.setHorizontalTextPosition(JLabel.LEFT);
		actionLabel.setHorizontalAlignment(JLabel.LEFT);
		addComponent(actionLabel, new Insets(0,0,0,0),1,1,1,1,1,1,GridBagConstraints.HORIZONTAL);
		
		
		
		Border border=new Border(){
			@Override
			public Insets getBorderInsets(Component arg0) {
				return new Insets(0,0,0,0);
			}
			
			@Override
			public boolean isBorderOpaque() {
				return true;
			}

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
				int rand=10;
				g.setColor(Color.decode("0xEBEBEB"));
				g.drawLine(x+rand, y, x+w-rand, y);
				g.drawLine(x+rand, y+h-1, x+w-rand, y+h-1);
			}
		};
		
		setBorder(border);		
		setBackground(Color.white);
	}
	
	/**
	 * Fügt eine neue Subkomponente hinzu
	 */
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

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		nameLabel.setText(value.toString());
		actionLabel.setText(MainGui.manager.getTypeByName(value.toString()).getExecutor().getActionString());
        Color background=Color.white;
        Color textColor=Color.black;
		
		if(isSelected){
			background=Color.decode("0x0084B0");
			textColor=Color.white;
		}
		setBackground(background);
		nameLabel.setForeground(textColor);actionLabel.setForeground(textColor);
		text1.setForeground(textColor);text2.setForeground(textColor);
		return this;
	}
}
