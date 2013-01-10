package wiiGestureX.filters;
import java.util.Vector;

/**
 * @author Anton Schubert
 * 
 * Basisklasse für alle Filter
 *
 */
public interface Filter {
	Vector<Double> filter(double xAccel,double yAccel,double zAccel);
}
