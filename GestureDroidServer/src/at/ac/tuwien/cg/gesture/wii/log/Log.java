package at.ac.tuwien.cg.gesture.wii.log;

public class Log {

	public static void d(String key, String value) {
		System.out.println(key+" "+value);
		
	}

	public static void e(String key, String value) {
		System.err.println(key+" "+value);
		
	}

}
