package wiiGestureX.events;
import motejx.extensions.motionplus.MotionPlusEvent;
import motejx.extensions.motionplus.MotionPlusListener;
import wiiGestureX.Gesture;

/**
 * @author Anton Schubert
 *
 */
public class GestureRotationListener implements MotionPlusListener {
	private Gesture _gesture;
	private Double yawSpeed=0.0;
	private Double pitchSpeed=0.0;
	private Double rollSpeed=0.0;
	
	public GestureRotationListener(Gesture gesture){
		_gesture=gesture; //initialize
	}

	@Override
	public void speedChanged(MotionPlusEvent evt) {
		yawSpeed=evt.getYawLeftSpeed(); //Werte auslesen
		pitchSpeed=evt.getPitchDownSpeed();
		rollSpeed=evt.getRollLeftSpeed();
		
		_gesture.setRotStep(rollSpeed, pitchSpeed, yawSpeed); //Werte in Geste schreiben
		
	}
}
