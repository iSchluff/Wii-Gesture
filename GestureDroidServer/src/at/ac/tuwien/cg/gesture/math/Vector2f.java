package at.ac.tuwien.cg.gesture.math;



/**
 * The class Vector2f
 * @author roman hochstoger & christoph fuchs
 */
public class Vector2f {

	public float x, y; // float value of the vector

	/**
	 * basic constructor that initialize this vector with 0 0 values
	 */
	public Vector2f() {
		set(0, 0);
	}


	/**
	 * this constructor initialize the vector with the given floats
	 * @param x		x-value
	 * @param y		y-value
	 */
	public Vector2f(float x, float y) {
		set(x, y);
	}


	/**
	 * this constructor initialize the vector with the given vector, so it makes a copy
	 * @param v		given vector that will initialize this new vector
	 */
	public Vector2f(Vector2f v) {
		set(v.x,v.y);
	}


	/**
	 * set the values of the vector with given float parameters
	 * @param x		x-value
	 * @param y		y-value
	 */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}


	/**
	 * set the values of the vector with given vector
	 * @param other is the given vector that will set this
	 */
	public void set(Vector2f other) {
		set(other.x,other.y);
	}

	/**
	 * this add method add an other vector, it will change the values of this vector, then it return this changed vector
	 * @param other		vector to add
	 * @return	this vector
	 */
	public Vector2f add(Vector2f other)
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
	public Vector2f subtract(Vector2f other)
	{
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}
	
	/**
	 * this multiply method multiply an other vector with this, it will change the values of this vector, then it return this changed vector
	 * @param other
	 * @return this vector
	 */
	public Vector2f multiply(Vector2f other)
	{
		this.x *= other.x;
		this.y *= other.y;
		return this;
	}
	
	/**
	 * this divide method divides this vector with an divider, it will change the values of this vector, then it return this changed vector
	 * @param divide
	 * @return
	 */
	public Vector2f divide(float divide)
	{
		this.x /= divide;
		this.y /= divide;
		return this;
	}

	/**
	 * this dotProduct method will return the dot product of the given two vectors (x1*x2+y1*y2)
	 * @param v1	vector 1
	 * @param v2	vector 2
	 * @return the dot product of the v1 v2 vector
	 */
	public static float dotProduct(Vector2f v1, Vector2f v2) {
		return (v1.x * v2.x + v1.y * v2.y);
	}

	/**
	 * this normalize method will normalize this vector == make the length of the vector to 1
	 */
	public void normalize() {
		float length = (float) Math.sqrt(x * x + y * y);

		if (length != 0) {
			this.x /= length;
			this.y /= length;
		}
	}
	
	/**
	 * this equals method is a comparator that compares if this vector is the same as the other
	 * @param other		vector witch will be compared with this
	 * @return true if the values are the same
	 */
	public boolean equals(Vector2f other){
		if(this.x==other.x&&this.y==other.y)
			return true;
		return false;
		
	}
	

	/**
	 * this equals method is a comparator that compares if this vector is the same as the other
	 * @param x		x value witch will be compared with this x value
	 * @param y		y value witch will be compared with this y value
	 * @return true if the values are the same
	 */
	public boolean equals(float x,float y){
		if(this.x==x&&this.y==y)
			return true;
		return false;
		
	}
	
	/**
	 * this area method return the area that this span (x*y)
	 * @return the area
	 */
	public float area(){
		return x*y;
	}


	/**
	 * overridden toString method for easy debugging
	 */
	@Override
	public String toString() {
		return "x="+x+" y="+y;
	}

}
