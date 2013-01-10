package at.ac.tuwien.cg.gesture.visualization;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import at.ac.tuwien.cg.gesture.math.Vector3f;




public class Camera {
	
	private GL gl;
	private GLU glu;
	private Preferences preferences;
	
	private Vector3f cameraPos;
	private Vector3f cameraView;
	private Vector3f cameraUp;
	
	public static float cameraZoomFac;
	public static float cameraXRotationFac;
	public static float cameraYRotationFac;
	public static float cameraZRotationFac;
	
	public static float cameraZoomStep = 0.01f;
	
	
	public Camera(GL gl, GLU glu, Preferences preferences){
		this.gl = gl;
		this.glu = glu;
		this.preferences = preferences;
		
		this.cameraXRotationFac=preferences.getStandardXRotation();
		this.cameraYRotationFac=preferences.getStandardYRotation();
		this.cameraZRotationFac=preferences.getStandardZRotation();
		this.cameraZoomFac=preferences.getStandardZoomFac();
	}
	
	/**
	 * Initializes the camera and the camera's parameter 
	 */
	public void initCamera(){
		
		cameraPos = preferences.getCameraPosition();
		cameraView = preferences.getCameraView();
		cameraUp = preferences.getCameraUp();
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		float aspectRatio = (float)preferences.getWindowWidth()/preferences.getWindowHeight();
		glu.gluPerspective(45, aspectRatio, 0.1f, 500);
		//gl.glFrustum(arg0, arg1, arg2, arg3, arg4, arg5)
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		
		glu.gluLookAt(cameraPos.x, cameraPos.y, cameraPos.z, 
					  cameraView.x, cameraView.y, cameraView.z,
					  cameraUp.x, cameraUp.y, cameraUp.z);
		
	}
	
	/**
	 * 
	 */
	public void updateCamera(){
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		glu.gluLookAt(cameraPos.x, cameraPos.y, cameraPos.z, 
					  cameraView.x, cameraView.y, cameraView.z,
					  cameraUp.x, cameraUp.y, cameraUp.z);
		
		gl.glRotatef(cameraXRotationFac, 1, 0, 0);
		gl.glRotatef(cameraYRotationFac, 0, 1, 0);
		gl.glRotatef(cameraZRotationFac, 0, 0, 1);
		gl.glScalef(cameraZoomFac, cameraZoomFac, cameraZoomFac);
		
	}
	

}
