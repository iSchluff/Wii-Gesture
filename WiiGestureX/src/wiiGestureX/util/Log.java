package wiiGestureX.util;

/**
 * @author Anton Schubert
 * 
 * Logging
 *
 */
public class Log {

	public static final int OFF = -1;
	public static final int NORMAL = 0;
	public static final int DEBUG = 1;
	
	public static int level = Log.NORMAL;
	
	public static void setLevel(int n) {
		level = n;
	}
	
	public static void write(String s) {
		write(Log.NORMAL, s, null);
	}
	
	public static void write(String s, Object o) {
		write(Log.NORMAL, s, o);
	}
	
	public static void write(int n, String s, Object o) {
		if(level>=n) {
			// console output enabled
			if(o!=null) {
				System.out.println(o.getClass()+": "+s);
			} else {
				System.out.println(s);
			}
		}
	}
	
}
