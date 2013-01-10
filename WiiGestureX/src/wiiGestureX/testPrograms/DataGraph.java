package wiiGestureX.testPrograms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Vector;

import javax.swing.JPanel;

import wiiGestureX.util.Log;

public class DataGraph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int rand = 20;
	private int x=rand;
	private int yScale=15;
	private Vector<Double> oldAccel;
	private Vector<Double> newAccel;
	private Color[] colors;
	
	public DataGraph(){
		reset();
		colors=new Color[]{Color.red,Color.green,Color.blue};
		
	}
	
	public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
	    int w = getWidth();
	    int h = getHeight();
	    // Draw ordinate.
	    g2d.draw(new Line2D.Double(rand, h-rand, rand, rand));
	    // Draw abscissa.
	    g2d.draw(new Line2D.Double(rand, h-rand, w-rand, h-rand)); 
	}
	
	public void reset(){
		Log.write(Log.DEBUG,"resetting",this);
		repaint();
		oldAccel=new Vector<Double>();
		newAccel=new Vector<Double>();
		double d=0;
		for(int i=0;i<3;i++){
			oldAccel.add(d);
			newAccel.add(d);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void drawAccelLines() {
		//Log.write(Log.DEBUG,"drawing",this);
		Graphics g=this.getGraphics();
		Graphics2D g2d=(Graphics2D)g;
		for(int i=0;i<newAccel.size();i++){
			g2d.setColor(colors[i]);
			//Log.write(Log.DEBUG,oldAccel.size()+" "+newAccel.size(),this);
			g2d.draw(new Line2D.Double(x, yScale*oldAccel.get(i)+getHeight()/2,
					x+1, yScale*newAccel.get(i)+getHeight()/2));
		}
		
		oldAccel=(Vector<Double>) newAccel.clone();
        
        x++;   
		if(x>=(getWidth()-(rand*2))){
			reset();
			x=rand;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setAccel(Vector<Double> newAccel){
		this.newAccel=(Vector<Double>) newAccel.clone();
		//Log.write(Log.DEBUG,newAccel.size()+" "+this.newAccel.size(),this);
		drawAccelLines();
	}

}
