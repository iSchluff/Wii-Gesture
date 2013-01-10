package at.ac.tuwien.cg.gesture.math;

import java.io.Serializable;
import java.text.DecimalFormat;


public class Vector3f implements Serializable {

	/**
	 * The class Color
	 * @author roman hochstoger & christoph fuchs
	 */
	
	private static final long serialVersionUID = -8111125934436898879L;
	public Float x;
	public Float y;
	public Float z;
	
	public Vector3f(Float x, Float y, Float z) {
		set(x,y,z);
	}
	
	public Vector3f(Vector3f vc) {
		this.x = vc.x;
		this.y = vc.y;
		this.z = vc.z;
	}

	/**
	 * set this vector.
	 */
	public void set(Float x, Float y, Float z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	/**
	 * set this vector.
	 */
	public void set(Vector3f v) {
		this.x=v.x;
		this.y=v.y;
		this.z=v.z;
	}
	
	/**
	 * @return the length of this vector.
	 */
	public Float length(){
		
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * will normalize this vector.
	 */
	public void normalize ()
	{
		float lenght = length();

		if (lenght != 0) 
		{
			this.x /= lenght;
			this.y /= lenght;
			this.z /= lenght;
		}
	}
	/**
	 * set the parameter of this vector
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/**
	 * Return a new vector that is the crossProduct of the two input vector.
	 */
	public static Vector3f crossProduct (Vector3f v1, Vector3f v2)
	{
		return (new Vector3f(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x*v2.z, v1.x*v2.y - v1.y*v2.x));
	}

	/**
	 * Return the dot product of two vectors.
	 */
	public static float dotProduct (Vector3f v1, Vector3f v2)
	{
		return (v1.x*v2.x + v1.y*v2.y + v1.z*v2.z);
	}

	/**
	 * Return a new vector that is the sum of vector v1 and v2.
	 */
	public static Vector3f add (Vector3f v1, Vector3f v2)
	{
		return (new Vector3f(v1.x+v2.x, v1.y+v2.y, v1.z+v2.z));
	}
	
	/**
	 * add the vector v to this vector
	 */
	public void add (Vector3f v)
	{
		x+=v.x;
		y+=v.y;
		z+=v.z;
	}

	/**
	 * Return a new vector that points from one point to another.
	 */
	public static Vector3f subtract (Vector3f pointEnd, Vector3f pointStart)
	{
		return new Vector3f(pointEnd.x-pointStart.x, pointEnd.y-pointStart.y, pointEnd.z-pointStart.z);
	}
	
	/**
	 * subtract this vector with the given this= this - given.
	 */
	public Vector3f subtract(Vector3f v) {
		x -=v.x;
		y -=v.y;
		z -=v.z;
		return this;
	}

	/**
	 * Multiply the given vector v with a scalar s.
	 */
	public static Vector3f mult(Vector3f v, float s) {
	
		v.x *= s;
		v.y *= s;
		v.z *= s;
		return v;
	}

	/**
	 * multiply this vector with a scalar s.
	 */
	public void mult (float s)
	{
		x *= s;
		y *= s;
		z *= s;
	}
	
	/**
	 * Return a new vector that is the result of numerator divided denominator.
	 */
	public static Vector3f divide(Vector3f numerator,Vector3f denominator)
	{
		return new Vector3f(numerator.x / denominator.x, numerator.y / denominator.y, numerator.z / denominator.z);
	}
	
	/**
	 * divide this vector with the given denominator. ==> (this/denominator)
	 */
	public void divide(Vector3f denominator)
	{
		this.x /= denominator.x;
		this.y /= denominator.y;
		this.z /= denominator.z;
	}

	/**
	 * Invert this vector.
	 */
	public void invert()
	{
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}
	
	/**
	 * Vector cross product
	 * @param v1 vector
	 * @param v2 vector
	 */
	public Vector3f cross(Vector3f v1, Vector3f v2) {
		Vector3f c = new Vector3f(0.f, 0.f, 0.f);
		c.z = v1.x * v2.y - v1.y * v2.x;
		c.x = v1.y * v2.z - v1.z * v2.y;
		c.y = v1.z * v2.z - v1.z * v2.z;
		return c;
	}
	
	/**
	 * Vector dot product
	 * @param v vector
	 * @return 
	 */
	public float dot(Vector3f v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public void rotate(float angle, Vector3f axis)
	{
		Vector3f c = new Vector3f(0.f,0.f,0.f);
		Vector3f a = new Vector3f(axis);
		a.normalize();
		c.cross(this, a);

		Vector3f result = getRejection(axis);
		result.mult((float) Math.cos(angle));

		c.mult((float) Math.sin(angle));
		result.add(c);
		result.add(getProjection(axis));

		this.set(result);
	}

	public static Vector3f abs(Vector3f d) {
		if(d.x<0)
			d.x*=-1;
		if(d.y<0)
			d.y*=-1;
		if(d.z<0)
			d.z*=-1;
		return d;
	}
	
	/**
	 * this method eliminate tiny values, eg.: x<1.0E-6f =x==0;
	 * @param d to zeor values eliminate vector
	 * @param epsilon parameter eq.: 1.0E-6f
	 * @return
	 */
	public static Vector3f eliminateEpsilon(Vector3f v,float epsilon) {
		if(v.x<epsilon)
			v.x=0.f;
		if(v.y<epsilon)
			v.y=0.f;
		if(v.z<epsilon)
			v.z=0.f;
		return v;
	}

	/**
	 * gets the projection of this vector on v
	 * @param v
	 * @return
	 */
	public Vector3f getProjection(Vector3f v) {
		Vector3f e = new Vector3f(v);
		e.normalize();
		e.mult(this.dot(e));
		return e;
	}

	/**
	 *  gets the rejection of this vector on v
	 * @param v
	 * @return
	 */
	public Vector3f getRejection(Vector3f v) {
		Vector3f u = new Vector3f(this);
		u.subtract(getProjection(v));
		return u;
	}

	/**
	 * evaluet if the given vector and this vector Point are in the same quadrant
	 * @param v
	 * @return true if the two vectors are in the 
	 */
	
	public boolean isSameQuadrant(Vector3f v) {
	
		if (   Math.signum(x) == Math.signum(v.x)
			&& Math.signum(y) == Math.signum(v.y)
			&& Math.signum(z) == Math.signum(v.z))
			return true;
		
		return false;
	}

	@Override
	public String toString() {
		DecimalFormat f = new DecimalFormat("######0.00000");
		return "x = "+f.format(x)+ "     y = "+f.format(y)+ "     z = "+f.format(z)+   "          length="+length() ;
	}

	/**
	 * return a new Vector3f with the absolute value of each entry of this vector
	 * @return
	 */
	public Vector3f getAbs()
	{
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}
	
	

	

}
