package wiiGestureX.filters;

import java.util.Vector;


/**
 * @author Anton Schubert
 * 
 * Filter schnelle Bewegungen
 *
 */
public class LowPassFilter implements Filter {
	Vector<Double> lastAccel;
	double factor;
	
	public LowPassFilter(){
		lastAccel=new Vector<Double>();
		lastAccel.add(0.0);
		lastAccel.add(0.0);
		lastAccel.add(0.0);
		factor=0.02;
	}

	@Override
	public Vector<Double> filter(double xAccel, double yAccel, double zAccel) {
		Vector<Double> filtered= new Vector<Double>();
        filtered.add(xAccel * this.factor + this.lastAccel.get(0)* (1.0 - this.factor));
        filtered.add(yAccel * this.factor + this.lastAccel.get(1) * (1.0 - this.factor));
        filtered.add(zAccel * this.factor + this.lastAccel.get(2) * (1.0 - this.factor));
        this.lastAccel = filtered;
        return filtered;
	}

}
