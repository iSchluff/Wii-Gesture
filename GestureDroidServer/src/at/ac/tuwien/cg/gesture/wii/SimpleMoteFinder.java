package at.ac.tuwien.cg.gesture.wii;

import motej.Mote;
import motej.MoteFinder;
import motej.MoteFinderListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleMoteFinder implements MoteFinderListener {

	private Logger log = LoggerFactory.getLogger(SimpleMoteFinder.class);
	private MoteFinder finder;
	private Object lock = new Object();
	private Mote mote;
	
	public SimpleMoteFinder() {
		// TODO Auto-generated constructor stub
	}

	public void moteFound(Mote mote) {
		log.info("SimpleMoteFinder received notification of a found mote.");
		this.mote = mote;
		synchronized(lock) {
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
			synchronized(lock) {
				lock.wait();
			}
		} catch (InterruptedException ex) {
			log.error(ex.getMessage(), ex);
		}
		return mote;
	}


}
