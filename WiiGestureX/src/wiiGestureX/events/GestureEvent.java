package wiiGestureX.events;

import java.util.EventObject;

import wiiGestureX.Gesture;

/**
 * Wird ausgelöst, wenn eine neue Geste aufgenommen wurde
 * @author Anton Schubert
 *
 */
public class GestureEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private Gesture gesture;

	public GestureEvent(Object source,Gesture g) {
		super(source);
		gesture=g;
	}
	
	public Gesture getGesture(){
		return gesture;
	}
}
