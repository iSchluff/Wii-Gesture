package at.ac.tuwien.cg.gesture.visualization;

import java.text.DecimalFormat;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import org.apache.commons.math.geometry.CardanEulerSingularityException;
import org.apache.commons.math.geometry.NotARotationMatrixException;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;

import at.ac.tuwien.cg.gesture.control.InputHandler;
import at.ac.tuwien.cg.gesture.data.MeasuringPoint;
import at.ac.tuwien.cg.gesture.data.MeasuringPointStorage;
import at.ac.tuwien.cg.gesture.math.Matrix4f;
import at.ac.tuwien.cg.gesture.math.Quaternion;
import at.ac.tuwien.cg.gesture.math.Vector3f;
import at.ac.tuwien.cg.gesture.visualization.menu.GForceMenuBar;
import at.ac.tuwien.cg.gesture.visualization.menu.GForceVis;


public class RenderLoop extends Thread{
	
	//Window that shows the context
	private MainWindow mainWindow;
	private GForceVis gForceWindow;
	
	private boolean isLoopOfMain = false;
	private boolean isLoopOfGforce = false;
	
	private MeasuringPointStorage mpStorage;
	MeasuringPoint m1,m2;
	Vector3f coordsDistance1,coordsDistance2;
	
	public static Vector3f gForceVec = new Vector3f(0.0f, 0.0f, 0.0f);
	private Vector3f accelerationVec = new Vector3f(0.0f, 0.0f, 0.0f);
	String orientationXText = "";
	String orientationYText = "";
	String orientationZText = "";
	String gForceXText = "";
	String gForceYText = "";
	String gForceZText = "";
	String accelerationXText = "";
	String accelerationYText = "";
	String accelerationZText = "";
	DecimalFormat f = new DecimalFormat("######0.000");
	
	int aswitch = 0;
	Vector3f down = new Vector3f(0.f,0.f,1.f);
	double rad2deg = (180f/Math.PI);
	
	
	public RenderLoop(MainWindow mainWindow, MeasuringPointStorage mpStorage){
		this.mainWindow = mainWindow;
		isLoopOfMain=true;
		
		this.mainWindow.setRenderLoop(this);
		this.mpStorage = mpStorage;
	}
	
	public RenderLoop(GForceVis gForceWindow){
		this.gForceWindow = gForceWindow;
		isLoopOfGforce=true;
		
	}
	
	//Thread-Start
	public void run(){
		if(isLoopOfMain)
			mainRenderLoop();
		else if(isLoopOfGforce)
			gForceRenderLoop();
	}
	
	//RENDERLOOP for MAINWINDOW
	public void mainRenderLoop(){
		while ( mainWindow.isVisible() ) {
			
			//Draw Window
			mainWindow.draw();
						
			//FPS
			mainWindow.calculateFPS();	
		}
	}
	
	public void drawMainWinLoop(GL gl){
		
		// Clear the screen
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		if(true)//InputHandler.MPIsRealMeasurement
		{
			int numberOfPoints = mpStorage.getSize();
			
			if(numberOfPoints>0)
			{					
				//DRAW POINTS
				gl.glColor3f(1.0f, 1.0f, 0.0f);
				gl.glPointSize(5);
				gl.glPushMatrix();
				gl.glBegin(GL.GL_POINTS);
					
				for(int i=0; i<(numberOfPoints-1); i++)
				{
					m1 = (MeasuringPoint) mpStorage.getObject(i);
					coordsDistance1 = new Vector3f(m1.getDistance());
					coordsDistance1.mult(10^2);
					
					m2 = (MeasuringPoint) mpStorage.getObject(i+1);
					coordsDistance2 = new Vector3f(m2.getDistance());
				
					gl.glVertex3f(coordsDistance1.x, coordsDistance1.y, coordsDistance1.z);
					
				}
				
				gl.glEnd();
				gl.glPopMatrix();
			}	
		}
		
		//Coordinate-System
		gl.glColor3f(0.0f, 1.0f, 0.0f);
	    gl.glPushMatrix();
	    gl.glBegin(GL.GL_LINES);
	    gl.glVertex3f(0.0f,0.0f,0.0f);
	    gl.glVertex3f(0.0f,200.0f,0.0f);
	    gl.glEnd();
	    gl.glPopMatrix();
	    gl.glColor3f(1.0f, 0.0f, 0.0f);
	    gl.glPushMatrix();
	    gl.glBegin(GL.GL_LINES);
	    gl.glVertex3f(0.0f,0.0f,0.0f);
	    gl.glVertex3f(200.0f,0.0f,0.0f);
	    gl.glEnd();
	    gl.glPopMatrix();
	    gl.glColor3f(0.0f, 1.0f, 1.0f);
	    gl.glPushMatrix();
	    gl.glBegin(GL.GL_LINES);
	    gl.glVertex3f(0.0f,0.0f,0.0f);
	    gl.glVertex3f(0.0f,0.0f,200.0f);
	    gl.glEnd();
	    gl.glPopMatrix();
	    gl.glFlush();
		
		
	}
	
	//RENDERLOOP for G-FORCEWINDOW
	public void gForceRenderLoop(){
		while ( gForceWindow.isVisible() ) {
            
			//Update Orientation
			if(GForceMenuBar.isMRealtime || GForceMenuBar.nextMeasurement)
			{
				
				gForceXText = f.format(gForceVec.x);
				gForceYText = f.format(gForceVec.y);
				gForceZText = f.format(gForceVec.z);

				GForceMenuBar.lMgForceX.setText(gForceXText);
				GForceMenuBar.lMgForceY.setText(gForceYText);
				GForceMenuBar.lMgForceZ.setText(gForceZText);
				
				accelerationVec = new Vector3f(InputHandler.MPAcceleration);
				accelerationXText = f.format(accelerationVec.x);
				accelerationYText = f.format(accelerationVec.y);
				accelerationZText = f.format(accelerationVec.z);
				
				GForceMenuBar.lMAccelerationX.setText(accelerationXText);
				GForceMenuBar.lMAccelerationY.setText(accelerationYText);
				GForceMenuBar.lMAccelerationZ.setText(accelerationZText);
				
				GForceMenuBar.nextMeasurement=false;
			}
			
			
			//FPS
			gForceWindow.calculateFPS();
			
			//Draw Window
			gForceWindow.draw();
			
		}
	}
	
	public void drawGForceWinLoop(GL gl,GLU glu ){
		
		// Clear the screen
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
        if (InputHandler.MPOrientationMatrix != null)
		{
			//gl.glMatrixMode(gl.GL_PROJECTION);
			
			//glu.gluLookAt(0, 5, 5, 0, 0, 0, 0, 1, 0);

			

			//gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        	
        	gl.glMatrixMode(gl.GL_MODELVIEW);
        	
			Matrix4f m = new Matrix4f(InputHandler.MPOrientationMatrix);
			System.out.println("matrix: "+m);
			//m.invert();

			gl.glLoadIdentity();
			
			
			gl.glTranslatef(0.3f, 0.3f, -5);
			gl.glMultMatrixf(m.transposeLocal().getMatrix(), 0);
		    
			
	        
	        // ein mal ganz richtig
	        Vector3f x = new Vector3f(0.7f,0.f,0.f);
	        Vector3f y = new Vector3f(0.f,0.7f,0.f);
	        Vector3f z = new Vector3f(0.f,0.f,0.7f);
	        
			gl.glPushMatrix();
			gl.glBegin(GL.GL_LINES);
			gl.glColor3f(0.0f, 0.0f, 0.0f);
			gl.glVertex3f(0,0,0);
			gl.glColor3f(1.0f, 0.0f, 0.0f);			
			gl.glVertex3f(x.x,x.y,x.z);
			
			gl.glColor3f(0.0f, 0.0f, 0.0f);
			gl.glVertex3f(0,0,0);
			gl.glColor3f(0.0f, 1.0f, 0.0f);
			gl.glVertex3f(y.x,y.y,y.z);
			gl.glColor3f(0.0f, 0.0f, 0.0f);
			gl.glVertex3f(0,0,0);
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			gl.glVertex3f(z.x,z.y,z.z);
			gl.glColor3f(1.0f, 1.0f, 1.0f);

			
			
			GLUquadric qobj0 = glu.gluNewQuadric();
			 if(Math.sin(aswitch/10)>0)
			gl.glColor3f(1.0f, 1.0f, 1.0f);
			 else
			 gl.glColor3f(0.7f, 0.7f, 0.7f);
			glu.gluSphere( qobj0, 0.2f, 12, 12);
			gl.glEnd();
			gl.glPopMatrix();
			
			
return;

		}
		
		
	}

}
