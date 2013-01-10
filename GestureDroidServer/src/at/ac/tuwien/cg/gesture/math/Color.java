package at.ac.tuwien.cg.gesture.math;


/**
 * The class Color
 * @author roman hochstoger & christoph fuchs
 */
public class Color {
	public float r, g, b, a;// float values of the color and a (alpha) for transparency

	/**
	 * basic Color constructor without transparency, the transparency a is set to 1
	 * @param r		red component [0 1]
	 * @param g		green component [0 1]
	 * @param b		blue component [0 1]
	 */
	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1;
	}

	/**
	 * basic Color constructor with transparency
	 * @param r		red component [0 1]
	 * @param g		green component [0 1]
	 * @param b		blue component [0 1]
	 * @param a		alpha component [0 1]
	 */
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
}
