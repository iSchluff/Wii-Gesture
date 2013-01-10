package wiiGestureX.events;

import java.util.EventListener;

/**
 * Listener für GestureEvents
 * @author Anton Schubert
 *
 */
public interface GestureListener extends EventListener {
	    public void GestureRecorded(GestureEvent evt);

}
