package at.ac.tuwien.cg.gesture.visualization;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Calendar;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputListener;

import org.jdesktop.swingx.JXFrame;

import at.ac.tuwien.cg.gesture.math.Vector2i;
import at.ac.tuwien.cg.gesture.visualization.menu.Menu;


public class MainWindow extends JXFrame implements GLEventListener,MouseWheelListener,MouseInputListener,KeyListener{
	
	private GLCanvas canvas;
	private GL mainWinGl;
	private GLU mainWinGLU;
	
	private Calendar now = Calendar.getInstance();
	private long ms = 0;
	public int frames = 0;
	
	private Vector2i mousePositionPressed = new Vector2i();
	private Vector2i mousePositionActually = new Vector2i();
	private Vector2i mouseDiff = new Vector2i();
		
	private Preferences preferences;
	
	private boolean mouseIsPressed = false;
	private boolean isCtrlPressed = false;
	
	Camera camera;
	RenderLoop rl;
	
	public MainWindow()
	{
		super("3D-Visualization");
		
		preferences = new Preferences();
		
		//WindowSize
		setSize(preferences.getWindowWidth(), preferences.getWindowHeight());
		//ClosingOperation
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addMouseWheelListener(this);
		
		
		//Canvas
		canvas = new GLCanvas(new GLCapabilities());
		canvas.addGLEventListener(this);
		//KeyListener
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	
		//MENU
		Menu menu = new Menu(preferences);
		JScrollPane scrollPane = menu.getScrollPane();
		
		//StatusBar
		JPanel statusPanel = menu.getStatusPanel();
		
		menu.initMenuLogic();
		
		setLayout(new BorderLayout());
		add(menu.getMenuBar(),BorderLayout.NORTH);
		add(canvas,BorderLayout.CENTER);
		add(scrollPane,BorderLayout.WEST);
		add(statusPanel,BorderLayout.SOUTH);

	}
	
	/**
	 * The window will be redrawn
	 */
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		mainWinGl = drawable.getGL();
		
		//Update Camera
		camera.updateCamera();
		
		if(rl != null)
			rl.drawMainWinLoop(mainWinGl);
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * OpenGL Init
	 */
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		mainWinGl = drawable.getGL();
		mainWinGLU = new GLU();
		
		gl.glViewport(0, 0, preferences.getWindowWidth(), preferences.getWindowHeight());
		
		camera = new Camera(gl, mainWinGLU,preferences);
		camera.initCamera();
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_LINE_SMOOTH);
		gl.glEnable(GL.GL_BLEND);
	    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glLineWidth(3.0f);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
	}
	
	/**
	 * Called if the window's size change
	 */
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Redraws the windows contents.
	 */
	public void draw() {
	        canvas.display();
	}
	
	public void calculateFPS(){
		
		if(now.getTimeInMillis() >= (ms+1000))
		{
			ms = now.getTimeInMillis();
			
			frames =1;
		}
		else
		{
			frames++;
		}
	}
	
	
	
	public void keyPressed(KeyEvent e) {
		
		
		System.out.println(String.valueOf(e.getKeyCode()));
		
		if(e.getKeyCode()==KeyEvent.VK_UP)
		{
			//ARROW-UP
			camera.cameraXRotationFac++;
			if(camera.cameraXRotationFac>=360)
				camera.cameraXRotationFac=0;
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			//ARROW-DOWN
			camera.cameraXRotationFac--;
			if(camera.cameraXRotationFac<=0)
				camera.cameraXRotationFac=360;
		}
		else if(e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			//ARROW-LEFT
			camera.cameraYRotationFac++;
			if(camera.cameraYRotationFac>=360)
				camera.cameraYRotationFac=0;

		}
		else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			//ARROW-RIGHT
			camera.cameraYRotationFac--;
			if(camera.cameraYRotationFac<=0)
				camera.cameraYRotationFac=360;
		}
		else if(e.getKeyCode()==KeyEvent.VK_PLUS)
		{
			//PLUS
			camera.cameraZoomFac=camera.cameraZoomFac+camera.cameraZoomStep;
			System.out.println("Zoom: "+String.valueOf(camera.cameraZoomFac));
			
		}
		else if(e.getKeyCode()==KeyEvent.VK_MINUS)
		{
			//MINUS
			camera.cameraZoomFac=camera.cameraZoomFac-camera.cameraZoomStep;
			if(camera.cameraZoomFac<=0)
				camera.cameraZoomFac=0.001f;
			
			System.out.println("Zoom: "+String.valueOf(camera.cameraZoomFac));
		}
		else if(e.getKeyCode()==KeyEvent.VK_SPACE)
		{
			//SPACE
			camera.cameraXRotationFac=preferences.getStandardXRotation();
			camera.cameraYRotationFac=preferences.getStandardYRotation();
			camera.cameraZoomFac=preferences.getStandardZoomFac();
		}
		else if(e.getKeyCode()==KeyEvent.VK_CONTROL)
		{
			//STRG
			isCtrlPressed=true;
		}
	}

	
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode()==KeyEvent.VK_CONTROL)
		{
			//STRG
			isCtrlPressed=false;
		}
		
	}

	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		//UP
		if(e.getWheelRotation()<0)
		{
			camera.cameraZoomFac=camera.cameraZoomFac+camera.cameraZoomStep;
			System.out.println("Zoom: "+String.valueOf(camera.cameraZoomFac));
		}
		//DOWN
		else
		{
			camera.cameraZoomFac=camera.cameraZoomFac-camera.cameraZoomStep;
			if(camera.cameraZoomFac<=0)
				camera.cameraZoomFac=0.001f;
			
			System.out.println("Zoom: "+String.valueOf(camera.cameraZoomFac));
		}	
	}

	
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3)
		{
			mouseIsPressed=true;
			mousePositionPressed.x = e.getXOnScreen();
			mousePositionPressed.y = e.getYOnScreen();
		}
	}

	
	public void mouseReleased(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3)
		{
			mouseIsPressed=false;
		}
	}

	
	public void mouseDragged(MouseEvent e) {
		//if(mouseIsPressed && (mousePositionActually.x != mousePositionPressed.x)
			//		      && (mousePositionActually.y != mousePositionPressed.y))
		if(mouseIsPressed && !isCtrlPressed)
		{
			//X-Y-Rotation
			mousePositionActually.x = e.getXOnScreen();
			mousePositionActually.y = e.getYOnScreen();
			
			mouseDiff = mousePositionPressed.subtract(mousePositionActually);
			
			//System.out.println("DeltaX: " +mouseDiff.x + "    DeltaY: " +mouseDiff.y);
			camera.cameraXRotationFac = camera.cameraXRotationFac + mouseDiff.x/4;
			camera.cameraYRotationFac = camera.cameraYRotationFac + mouseDiff.y/4;
			
			mousePositionPressed.set(mousePositionActually.x, mousePositionActually.y);
			
		} else
		if(mouseIsPressed && isCtrlPressed)
		{
			//Z-Rotation
			mousePositionActually.x = e.getXOnScreen();
			mousePositionActually.y = e.getYOnScreen();
			
			mouseDiff = mousePositionPressed.subtract(mousePositionActually);
			
			//System.out.println("DeltaX: " +mouseDiff.x + "    DeltaY: " +mouseDiff.y);
			camera.cameraZRotationFac = camera.cameraZRotationFac + mouseDiff.x/4;
			
			mousePositionPressed.set(mousePositionActually.x, mousePositionActually.y);
			
		}
	}

	
	public void mouseMoved(MouseEvent arg0) {
	}
	
	/*
	public GL getGL() {
		return mainWinGl;
	}
	*/
	public void setRenderLoop(RenderLoop rl){
		this.rl = rl;
	}
}
