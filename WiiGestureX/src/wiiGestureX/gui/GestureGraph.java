package wiiGestureX.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.Vector;
import javax.swing.JPanel;
import wiiGestureX.util.Log;

public class GestureGraph extends JPanel {
	private static final long serialVersionUID = 1L;
	private int rand = 20;
	private int x=rand;
	private int yScale=30;
	private Vector<Double> oldAccel;
	private Vector<Double> newAccel;
	public boolean recording=false;
	private Color[] colors1;
	private Color[] colors2;
	
	public GestureGraph(){
		reset();
		colors1=new Color[]{new Color(255,0,17,100),new Color(92,230,0,100), new Color(0,134,179,100)};
		colors2=new Color[]{Color.decode("0xFF0012"),Color.decode("0x5BE300"),Color.decode("0x0084B0")};
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
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
/*		if(recording){
			//grauer Balken
			//g2d.setColor(Color.decode("0xEBEBEB"));
			//g2d.draw(new Line2D.Double(x+1,0,x+1,getHeight()/2-1));
			//g2d.draw(new Line2D.Double(x+1,getHeight()/2+1,x+1,getHeight()));
			
			//roter Strich
			//g2d.setColor(Color.decode("0xFF0012"));
			//g2d.draw(new Line2D.Double(x, getHeight()/2, x+1, getHeight()/2)); 
			
			colors=new Color[]{Color.decode("0xFF0012"),Color.decode("0x5BE300"),Color.decode("0x0084B0")};
		}else{
			colors=new Color[]{Color.decode("0xFF6670"),Color.decode("0x94FF4D"),Color.decode("0x1AC6FF")};
		}*/
		
		for(int i=0;i<newAccel.size();i++){	
			if(recording){
				g2d.setColor(colors1[i]);
				g2d.draw(new Line2D.Double(x, getHeight()/2,
						x, yScale*newAccel.get(i)+getHeight()/2));
			}else{
				g2d.setColor(colors2[i]);
				//Log.write(Log.DEBUG,oldAccel.size()+" "+newAccel.size(),this);
				g2d.draw(new Line2D.Double(x, yScale*oldAccel.get(i)+getHeight()/2,
						x+1, yScale*newAccel.get(i)+getHeight()/2));
			}
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
		//Log.write(newAccel.toString(),this);
		if(newAccel.get(0)!=null && newAccel.get(1)!=null && newAccel.get(2)!=null){
			this.newAccel=(Vector<Double>) newAccel.clone();
			//Log.write(Log.DEBUG,newAccel.size()+" "+this.newAccel.size(),this);
			drawAccelLines();
		}
	}
	
	public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
	    int w = getWidth();
	    int h = getHeight();
	    // Draw ordinate.
	    g2d.draw(new Line2D.Double(rand, h-rand, rand, rand));
	    // Draw abscissa.
	    g2d.draw(new Line2D.Double(rand, h/2, w-rand, h/2)); 
	}
}