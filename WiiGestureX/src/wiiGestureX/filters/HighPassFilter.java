package wiiGestureX.filters;

import java.util.Vector;

/**
 * @author Anton Schubert
 *
 * Filtert langsame und gleichmäßige Bewegungen
 *
 */
public class HighPassFilter implements Filter{
	Vector<Double> lastAccel;
	double factor;
	
	public HighPassFilter(){
		lastAccel=new Vector<Double>();
		lastAccel.add(0.0);
		lastAccel.add(0.0);
		lastAccel.add(0.0);
		factor=0.1;
	}

	@Override
	public Vector<Double> filter(double xAccel, double yAccel, double zAccel) {
		Vector<Double> filtered= new Vector<Double>();
        lastAccel.set(0,xAccel * this.factor + this.lastAccel.get(0) * (1.0 - this.factor));
        lastAccel.set(1,yAccel * this.factor + this.lastAccel.get(1) * (1.0 - this.factor));
        lastAccel.set(2,zAccel * this.factor + this.lastAccel.get(2) * (1.0 - this.factor));
        
        filtered.add(xAccel-lastAccel.get(0));
        filtered.add(yAccel-lastAccel.get(1));
        filtered.add(zAccel-lastAccel.get(2));
        return filtered;
	}

}
