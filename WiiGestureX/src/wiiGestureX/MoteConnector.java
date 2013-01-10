package wiiGestureX;

import wiiGestureX.util.Log;
import motej.Mote;
import motej.MoteFinder;
import motej.MoteFinderListener;

/**
 * @author Anton Schubert
 *
 * Stellt die Verbindung zur Wii-Remote her.
 */
public class MoteConnector implements MoteFinderListener {
	private MoteFinder finder;
	private final Object lock = new Object();
	private Mote mote;

	@Override
	public void moteFound(Mote mote) {
		this.mote = mote;
		synchronized(lock){
			lock.notifyAll();
		}
		
	}
	
	public Mote findMote() {
		if (finder == null) {
			finder = MoteFinder.getMoteFinder();
			finder.addMoteFinderListener(this);
		}
		finder.startDiscovery();
		try {
			synchronized(lock){
				lock.wait(25000);
			}
		} catch (InterruptedException ex) {
			Log.write(ex.getMessage(),ex);
		}
		
		return mote;
	}

}
