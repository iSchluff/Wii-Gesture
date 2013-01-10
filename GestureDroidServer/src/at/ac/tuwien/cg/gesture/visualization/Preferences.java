package at.ac.tuwien.cg.gesture.visualization;

import at.ac.tuwien.cg.gesture.math.Color;
import at.ac.tuwien.cg.gesture.math.Vector3f;




public class Preferences {
	
	private int windowWidth = 1024;
	private int windowHeight = 768;
	
	private String serverSocketIP = new String("xxx.xxx.xxx.xxx");
	private String serverSocketPort = "xxxx";
	
	private Vector3f cameraPosition = new Vector3f(50f, 50f, 100f);
	private Vector3f cameraView = new Vector3f(0f, 0f, 0f);
	private Vector3f cameraUp = new Vector3f(0f, 1f, 0f);
	
	private float cameraStandardXRotation=0;
	private float cameraStandardYRotation=0;
	private float cameraStandardZRotation=0;
	
	private float cameraStandardZoomFac = 1.0f;
	
	private enum axisType {onlyAxis,axisWithArrows};
	
	
	
	private int animationMinSpeed = -4;
	private int animationMaxSpeed = 4;
	private int animationSpeedStep = 1;
	private int animationSpeedFac =0;
	private float animationDuration =0;
	
	private float displayStandardGridAlpha = 100.0f;
	private float displayStandardPhoneAlpha = 100.0f;
	private float displayStandardPointsAlpha = 100.0f;
	private float displayStandardAxisAreaAlpha = 100.0f;
	private float displayGridAlpha = 0f;
	private float displayPhoneAlpha = 0f;
	private float displayPointsAlpha = 0f;
	private float displayAxisAreaAlpha = 0f;
	
	private boolean showFunction = true;
	private boolean showAxis = true;
	private float axisLineWidth = 3.0f;
	private Color xAxisColor;
	private Color yAxisColor;
	private Color zAxisColor;
	
	private boolean showGrid = false;
	private float gridLineWidth = 2.0f;
	
	private boolean showPhone = false;
	private boolean showAxisArea = false;
	private boolean showPoints = false;
	
	public Vector3f getCameraPosition(){
		return cameraPosition;
	}
	
	public Vector3f getCameraView(){
		return cameraView;
	}
	
	public Vector3f getCameraUp(){
		return cameraUp;
	}
	
	public int getWindowWidth(){
		return windowWidth;
	}
	
	public int getWindowHeight(){
		return windowHeight;
	}
	
	public float getStandardXRotation(){
		return cameraStandardXRotation;
	}
	
	public float getStandardYRotation(){
		return cameraStandardYRotation;
	}
	
	public float getStandardZRotation(){
		return cameraStandardZRotation;
	}
	
	public float getStandardZoomFac(){
		return cameraStandardZoomFac;
	}
	
	public String getServerSocketIP(){
		return serverSocketIP;
	}
	
	public String getServerSocketPort(){
		return serverSocketPort;
	}
	
	public int getAnimationMaxSpeed(){
		return animationMaxSpeed;
	}
	
	public int getAnimationMinSpeed(){
		return animationMinSpeed;
	}
	
	public int getAnimationSpeedStep(){
		return animationSpeedStep;
	}

	public float getDisplayStandardGridAlpha() {
		return displayStandardGridAlpha;
	}

	public float getDisplayStandardPhoneAlpha() {
		return displayStandardPhoneAlpha;
	}

	public float getDisplayStandardPointsAlpha() {
		return displayStandardPointsAlpha;
	}

	public float getDisplayStandardAxisAreaAlpha() {
		return displayStandardAxisAreaAlpha;
	}

	public void setShowAxis(boolean showAxis) {
		this.showAxis = showAxis;
	}

	public boolean getShowAxis() {
		return showAxis;
	}

	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}

	public boolean getShowGrid() {
		return showGrid;
	}

	public void setShowPhone(boolean showPhone) {
		this.showPhone = showPhone;
	}

	public boolean getShowPhone() {
		return showPhone;
	}

	public void setShowPoints(boolean showPoints) {
		this.showPoints = showPoints;
	}

	public boolean getShowPoints() {
		return showPoints;
	}

	public void setShowAxisArea(boolean showAxisArea) {
		this.showAxisArea = showAxisArea;
	}

	public boolean getShowAxisArea() {
		return showAxisArea;
	}

	public void setShowFunction(boolean showFunction) {
		this.showFunction = showFunction;
	}

	public boolean getShowFunction() {
		return showFunction;
	}

	public void setDisplayGridAlpha(float displayGridAlpha) {
		this.displayGridAlpha = displayGridAlpha;
	}

	public float getDisplayGridAlpha() {
		return displayGridAlpha;
	}

	public void setDisplayPhoneAlpha(float displayPhoneAlpha) {
		this.displayPhoneAlpha = displayPhoneAlpha;
	}

	public float getDisplayPhoneAlpha() {
		return displayPhoneAlpha;
	}

	public void setDisplayPointsAlpha(float displayPointsAlpha) {
		this.displayPointsAlpha = displayPointsAlpha;
	}

	public float getDisplayPointsAlpha() {
		return displayPointsAlpha;
	}

	public void setDisplayAxisAreaAlpha(float displayAxisAreaAlpha) {
		this.displayAxisAreaAlpha = displayAxisAreaAlpha;
	}

	public float getDisplayAxisAreaAlpha() {
		return displayAxisAreaAlpha;
	}

	public void setAnimationSpeedFac(int animationSpeedFac) {
		this.animationSpeedFac = animationSpeedFac;
	}

	public int getAnimationSpeedFac() {
		return animationSpeedFac;
	}
}
