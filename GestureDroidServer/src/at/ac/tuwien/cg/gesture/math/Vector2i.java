package at.ac.tuwien.cg.gesture.math;



/**
 * The class Vector2i
 * @author roman hochstoger & christoph fuchs
 */
public class Vector2i {

	public int x, y;	// integer value of the vector

	/**
	 * basic constructor that initialize this vector with 0 0 values
	 */
	public Vector2i() {
		set(0, 0);
	}

	/**
	 * this constructor initialize the vector with the given integers
	 * @param x		x-value
	 * @param y		y-value
	 */
	public Vector2i(int x, int y) {
		set(x,y);
	}
	
	/**
	 * this constructor initialize the vector with the given vector, so it makes a copy of the given vector
	 * @param v		given vector that will initialize this new vector
	 */
	public Vector2i(Vector2i v) {
		set(v.x, v.y);
	}

	/**
	 * set the values of the vector with given integer parameters
	 * @param x		x-value
	 * @param y		y-value
	 */
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * this add method add an other vector, it will change the values of this vector, then it return this changed vector
	 * @param other		vector to add
	 * @return	this vector
	 */
	public Vector2i add(Vector2i other)
	{
		this.x += other.x;
		this.y += other.y;
		return this;
		
	}
	
	/**
	 * this subtract method subtracts an other vector from this, it will change the values of this vector, then it return this changed vector
	 * @param other
	 * @return this vector
	 */
	public Vector2i subtract(Vector2i other)
	{
		return subtract(other.x, other.y);
	}
	
	/**
	 * this subtract method subtracts the given values from this, it will change the values of this vector, then it return this changed vector
	 * @param x		x-value
	 * @param y		y-value
	 * @return this vector
	 */
	public Vector2i subtract(int x, int y) {
		
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	/**
	 * this area method return the area that this span (x*y)
	 * @return the area
	 */
	public int area(){
		return x*y;
	}

	/**
	 * overridden toString method for easy debugging
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "x="+x+" y="+y;
	}
	
}
