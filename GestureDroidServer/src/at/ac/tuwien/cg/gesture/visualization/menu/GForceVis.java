package at.ac.tuwien.cg.gesture.visualization.menu;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.nio.FloatBuffer;
import java.util.Calendar;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;

import org.apache.commons.math.geometry.CardanEulerSingularityException;
import org.apache.commons.math.geometry.NotARotationMatrixException;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.jdesktop.swingx.JXFrame;
import org.jfree.ui.Drawable;

import at.ac.tuwien.cg.gesture.control.InputHandler;
import at.ac.tuwien.cg.gesture.math.Matrix4f;
import at.ac.tuwien.cg.gesture.math.Quaternion;
import at.ac.tuwien.cg.gesture.math.Vector3f;
import at.ac.tuwien.cg.gesture.visualization.RenderLoop;

public class GForceVis extends JXFrame implements GLEventListener,
		MouseWheelListener, MouseInputListener, KeyListener
{

	private GLCanvas canvas;
	private GL mainWinGl;
	private GLU mainWinGLU;

	private Calendar now = Calendar.getInstance();
	private long ms = 0;
	public int frames = 0;
	int aswitch = 0;

	public RenderLoop rl;
	
	Vector3f down = new Vector3f(0.f,0.f,1.f);
	
	double rad2deg = (180f/Math.PI);

	public GForceVis()
	{

		super("G-Force-Visualization");

		setSize(800, 600);
		// setDefaultCloseOperation(this.EXIT_ON_CLOSE);

		// Canvas
		canvas = new GLCanvas(new GLCapabilities());
		canvas.addGLEventListener(this);
		// KeyListener
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);

		GForceMenuBar gfMenuBar = new GForceMenuBar();

		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		add(gfMenuBar.getMenuBar(), BorderLayout.WEST);
	}

	
	public void mouseClicked(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	
	public void mousePressed(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	
	public void mouseDragged(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	
	public void mouseMoved(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		// TODO Auto-generated method stub

	}

	
	public void display(GLAutoDrawable drawable)
	{
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		
		// Clear the screen
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		rl.drawGForceWinLoop(gl,glu);

		
		
		// Koordinatensystem
//		gl.glPushMatrix();
//		gl.glBegin(GL.GL_LINES);
//		gl.glColor3f(0.0f, 0.0f, 0.0f);
//		gl.glVertex3f(0,0,0);
//		gl.glColor3f(1.0f, 0.0f, 0.0f);
//		gl.glVertex3f(1,0,0);
//		gl.glColor3f(0.0f, 0.0f, 0.0f);
//		gl.glVertex3f(0,0,0);
//		gl.glColor3f(0.0f, 1.0f, 0.0f);
//		gl.glVertex3f(0,1,0);
//		gl.glColor3f(0.0f, 0.0f, 0.0f);
//		gl.glVertex3f(0,0,0);
//		gl.glColor3f(0.0f, 0.0f, 1.0f);
//		gl.glVertex3f(0,0,1);
//		gl.glEnd();
//		gl.glPopMatrix();
		
	

		if (GForceMenuBar.isDeviceSmartPhone && !GForceMenuBar.isNoDevice)
		{
		gl.glPushMatrix();
			// **************************************
			// SmartPhone
			// **************************************
			// Kopfhörer (hinten)
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(-0.3f, 0.06f, 0.6f);
			gl.glVertex3f(0.3f, 0.06f, 0.6f);
			gl.glVertex3f(0.3f, -0.06f, 0.6f);
			gl.glVertex3f(-0.3f, -0.06f, 0.6f);
			gl.glEnd();
			gl.glPopMatrix();

			// USB (vorne)
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(-0.3f, 0.06f, -0.6f);
			gl.glVertex3f(0.3f, 0.06f, -0.6f);
			gl.glVertex3f(0.3f, -0.06f, -0.6f);
			gl.glVertex3f(-0.3f, -0.06f, -0.6f);
			gl.glEnd();
			gl.glPopMatrix();

			// Links
			gl.glColor3f(0.0f, 1.0f, 0.0f);
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(-0.3f, 0.06f, -0.6f);
			gl.glVertex3f(-0.3f, 0.06f, 0.6f);
			gl.glVertex3f(-0.3f, -0.06f, 0.6f);
			gl.glVertex3f(-0.3f, -0.06f, -0.6f);
			gl.glEnd();
			gl.glPopMatrix();

			// Rechts
			gl.glColor3f(1.0f, 1.0f, 0.0f);
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(0.3f, 0.06f, -0.6f);
			gl.glVertex3f(0.3f, 0.06f, 0.6f);
			gl.glVertex3f(0.3f, -0.06f, 0.6f);
			gl.glVertex3f(0.3f, -0.06f, -0.6f);
			gl.glEnd();
			gl.glPopMatrix();

			// Display (oben)
			gl.glColor3f(1.0f, 1.0f, 1.0f);
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(-0.3f, 0.06f, 0.6f);
			gl.glVertex3f(0.3f, 0.06f, 0.6f);
			gl.glVertex3f(0.3f, 0.06f, -0.6f);
			gl.glVertex3f(-0.3f, 0.06f, -0.6f);
			gl.glEnd();
			gl.glPopMatrix();

			// Akku (unten)
			gl.glColor3f(0.3f, 0.3f, 0.3f);
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(-0.3f, -0.06f, 0.6f);
			gl.glVertex3f(0.3f, -0.06f, 0.6f);
			gl.glVertex3f(0.3f, -0.06f, -0.6f);
			gl.glVertex3f(-0.3f, -0.06f, -0.6f);
			gl.glEnd();
			gl.glPopMatrix();
			
		gl.glPopMatrix();
			// **************************************
		} else if(!GForceMenuBar.isDeviceSmartPhone && !GForceMenuBar.isNoDevice)
		{
			// **************************************
			// Wii
			// **************************************

			// Nunchuck (vorne)
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(-0.3f, 0.06f, 0.6f);
			gl.glVertex3f(0.3f, 0.06f, 0.6f);
			gl.glVertex3f(0.3f, -0.06f, 0.6f);
			gl.glVertex3f(-0.3f, -0.06f, 0.6f);
			gl.glEnd();
			gl.glPopMatrix();

			// IR (hinten)
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(-0.3f, 0.06f, -0.6f);
			gl.glVertex3f(0.3f, 0.06f, -0.6f);
			gl.glVertex3f(0.3f, -0.06f, -0.6f);
			gl.glVertex3f(-0.3f, -0.06f, -0.6f);
			gl.glEnd();
			gl.glPopMatrix();

			// Links
			gl.glColor3f(0.0f, 1.0f, 0.0f);
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(-0.3f, 0.06f, -0.6f);
			gl.glVertex3f(-0.3f, 0.06f, 0.6f);
			gl.glVertex3f(-0.3f, -0.06f, 0.6f);
			gl.glVertex3f(-0.3f, -0.06f, -0.6f);
			gl.glEnd();
			gl.glPopMatrix();

			// Rechts
			gl.glColor3f(1.0f, 1.0f, 0.0f);
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(0.3f, 0.06f, -0.6f);
			gl.glVertex3f(0.3f, 0.06f, 0.6f);
			gl.glVertex3f(0.3f, -0.06f, 0.6f);
			gl.glVertex3f(0.3f, -0.06f, -0.6f);
			gl.glEnd();
			gl.glPopMatrix();

			// Buttons (oben)
			gl.glColor3f(1.0f, 1.0f, 1.0f);
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(-0.3f, 0.06f, 0.6f);
			gl.glVertex3f(0.3f, 0.06f, 0.6f);
			gl.glVertex3f(0.3f, 0.06f, -0.6f);
			gl.glVertex3f(-0.3f, 0.06f, -0.6f);
			gl.glEnd();
			gl.glPopMatrix();

			// Akku (unten)
			gl.glColor3f(0.3f, 0.3f, 0.3f);
			gl.glPushMatrix();
			gl.glBegin(GL.GL_QUADS);
			gl.glVertex3f(-0.3f, -0.06f, 0.6f);
			gl.glVertex3f(0.3f, -0.06f, 0.6f);
			gl.glVertex3f(0.3f, -0.06f, -0.6f);
			gl.glVertex3f(-0.3f, -0.06f, -0.6f);
			gl.glEnd();
			gl.glPopMatrix();
			// **************************************
		}

		gl.glFlush();
	}

	
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged)
	{
		// TODO Auto-generated method stub

	}

	
	public void init(GLAutoDrawable drawable)
	{

		GL gl = drawable.getGL();
		GLU glu = new GLU();

		gl.glViewport(0, 0, 800, 600);

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		float aspectRatio = (float) 800 / 600;
		glu.gluPerspective(45, aspectRatio, 0.1f, 500);

		/*
		 * gl.glMatrixMode(GL.GL_MODELVIEW); gl.glLoadIdentity();
		 * 
		 * 
		 * glu.gluLookAt(0, 0, -500, 0, 0, 0, 0, 1, 0);
		 */
		cameraView(gl, glu);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_LINE_SMOOTH);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glLineWidth(3.0f);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		rl = new RenderLoop(this);
		rl.start();
	}

	
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4)
	{
		// TODO Auto-generated method stub

	}

	public void cameraView(GL gl, GLU glu)
	{
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		// glu.gluLookAt(0, 100, 200,
		// 0, 0, 0,
		// 0, 1, 0);

		glu.gluLookAt(0, 100, 100, 0, 0, 0, 0, 1, 0);
	}

	/**
	 * Redraws the windows contents.
	 */
	public void draw()
	{
		canvas.display();
	}

	public void calculateFPS()
	{

		if (now.getTimeInMillis() >= (ms + 1000))
		{
			ms = now.getTimeInMillis();

			frames = 1;
		} else
		{
			frames++;
		}
	}
	

}
