package at.ac.tuwien.cg.gesture.math;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * The class Matrix4f
 * @author roman hochstoger & christoph fuchs
 */

public class Matrix4f
{
	 public float m00, m01, m02, m03;
     public float m10, m11, m12, m13;
     public float m20, m21, m22, m23;
     public float m30, m31, m32, m33;
     public static HashMap<String,Vector3f> map;
     public static Vector3f lastPosition = new Vector3f(0.f,0.f,0.f);
     
	public Matrix4f() {
		setIdentity();
	}

	public Matrix4f(float[] m) {
		set(m);
	}
     
     public Matrix4f(Matrix4f m)
 	{
 		set(m);
 	}
     
     public void set(float[] array) {
         m00 = array[0];         m01 = array[1];         m02 = array[2];         m03 = array[3];
         m10 = array[4];         m11 = array[5];         m12 = array[6];         m13 = array[7];
         m20 = array[8];         m21 = array[9];         m22 = array[10];        m23 = array[11];
         m30 = array[12];        m31 = array[13];        m32 = array[14];        m33 = array[15];
     }
     private void set(Matrix4f other)
 	{
         m00 = other.m00;        m01 = other.m01;        m02 = other.m02;        m03 = other.m03;
         m10 = other.m10;        m11 = other.m11;        m12 = other.m12;        m13 = other.m13;
         m20 = other.m20;        m21 = other.m21;        m22 = other.m22;        m23 = other.m23;
         m30 = other.m30;        m31 = other.m31;        m32 = other.m32;        m33 = other.m33;
 		
 	}
     public void setIdentity() {
         m00 = 1.0f;         m01 = 0.0f;         m02 = 0.0f;         m03 = 0.0f;
         m10 = 0.0f;         m11 = 1.0f;         m12 = 0.0f;         m13 = 0.0f;
         m20 = 0.0f;         m21 = 0.0f;         m22 = 1.0f;         m23 = 0.0f;
         m30 = 0.0f;         m31 = 0.0f;         m32 = 0.0f;         m33 = 1.0f;
     }

	public float[] getMatrix()
	{
		float[] m = new float[16];
		m[0] = m00;		m[1] = m01;		m[2] = m02;		m[3] = m03;
		m[4] = m10;		m[5] = m11;		m[6] = m12;		m[7] = m13;
		m[8] = m20;		m[9] = m21;		m[10] = m22;		m[11] = m23;
		m[12] = m30;	m[13] = m31;	m[14] = m32;		m[15] = m33;
		return m;
	}
	
	public boolean equals(Object o) {
		
		Matrix4f comp = new Matrix4f();

			if(o instanceof Matrix4f)
				comp= (Matrix4f) o;
			
			
			comp = Matrix4f.sub(this,comp);
				
		
	        if (this == comp) 
	            return true;
	        
	        float diff= Math.abs(comp.m00) + Math.abs(comp.m01) + Math.abs(comp.m02) + Math.abs(comp.m03) +
	        			Math.abs(comp.m10) + Math.abs(comp.m11) + Math.abs(comp.m12) + Math.abs(comp.m13) +
	        			Math.abs(comp.m20) + Math.abs(comp.m21) + Math.abs(comp.m22) + Math.abs(comp.m23) +
	        			Math.abs(comp.m30) + Math.abs(comp.m31) + Math.abs(comp.m32) + Math.abs(comp.m33);
	        
	        if(diff>1.0E-4)// if more then epsion = 1.0E-6
	        	return false;
	        	
	        return true;
	   }
	
	/**
	 * multiply this matrix with the given right. this = this*right and returns this
	 *
	 * @param right the right multiplied matrix
	 * @return this
	 */
	public Matrix4f mult(Matrix4f right)
	{
		m00 = m00*right.m00 + m01*right.m10 + m02*right.m20 + m03*right.m30;
		m01 = m00*right.m01 + m01*right.m11 + m02*right.m21 + m03*right.m31;
		m02 = m00*right.m02 + m01*right.m12 + m02*right.m22 + m03*right.m32;
		m03 = m00*right.m03 + m01*right.m13 + m02*right.m23 + m03*right.m33;
		m10 = m10*right.m00 + m11*right.m10 + m12*right.m20 + m13*right.m30;
		m11 = m10*right.m01 + m11*right.m11 + m12*right.m21 + m13*right.m31;
		m12 = m10*right.m02 + m11*right.m12 + m12*right.m22 + m13*right.m32;
		m13 = m10*right.m03 + m11*right.m13 + m12*right.m23 + m13*right.m33;
		m20 = m20*right.m00 + m21*right.m10 + m22*right.m20 + m23*right.m30;
		m21 = m20*right.m01 + m21*right.m11 + m22*right.m21 + m23*right.m31;
		m22 = m20*right.m02 + m21*right.m12 + m22*right.m22 + m23*right.m32;
		m23 = m20*right.m03 + m21*right.m13 + m22*right.m23 + m23*right.m33;
		m30 = m30*right.m00 + m31*right.m10 + m32*right.m20 + m33*right.m30;
		m31 = m30*right.m01 + m31*right.m11 + m32*right.m21 + m33*right.m31;
		m32 = m30*right.m02 + m31*right.m12 + m32*right.m22 + m33*right.m32;
		m33 = m30*right.m03 + m31*right.m13 + m32*right.m23 + m33*right.m33;
		return this;
	}
	  
    public Matrix4f transposeLocal() {
        float temp = 0;
        temp = m01;
        m01 = m10;
        m10 = temp;

        temp = m02;
        m02 = m20;
        m20 = temp;

        temp = m03;
        m03 = m30;
        m30 = temp;

        temp = m12;
        m12 = m21;
        m21 = temp;

        temp = m13;
        m13 = m31;
        m31 = temp;

        temp = m23;
        m23 = m32;
        m32 = temp;      
        
        return this;
    }

    public float getDeterminante(){
    	
        float fA0 = m00*m11 - m01*m10;
        float fA1 = m00*m12 - m02*m10;
        float fA2 = m00*m13 - m03*m10;
        float fA3 = m01*m12 - m02*m11;
        float fA4 = m01*m13 - m03*m11;
        float fA5 = m02*m13 - m03*m12;
        float fB0 = m20*m31 - m21*m30;
        float fB1 = m20*m32 - m22*m30;
        float fB2 = m20*m33 - m23*m30;
        float fB3 = m21*m32 - m22*m31;
        float fB4 = m21*m33 - m23*m31;
        float fB5 = m22*m33 - m23*m32;
        return fA0*fB5-fA1*fB4+fA2*fB3+fA3*fB2-fA4*fB1+fA5*fB0;
    }
    
    
    /**
     * multiply this matrix with a scalar
     * @param scalar
     */
    public void mult(float scalar) {
        m00 *= scalar;
        m01 *= scalar;
        m02 *= scalar;
        m03 *= scalar;
        m10 *= scalar;
        m11 *= scalar;
        m12 *= scalar;
        m13 *= scalar;
        m20 *= scalar;
        m21 *= scalar;
        m22 *= scalar;
        m23 *= scalar;
        m30 *= scalar;
        m31 *= scalar;
        m32 *= scalar;
        m33 *= scalar;
    }
    
    /**
     * divide this matrix with a scalar
     * @param scalar
     */
    public void divide(float scalar) {
        m00 /= scalar;
        m01 /= scalar;
        m02 /= scalar;
        m03 /= scalar;
        m10 /= scalar;
        m11 /= scalar;
        m12 /= scalar;
        m13 /= scalar;
        m20 /= scalar;
        m21 /= scalar;
        m22 /= scalar;
        m23 /= scalar;
        m30 /= scalar;
        m31 /= scalar;
        m32 /= scalar;
        m33 /= scalar;
    }
    
    /**
     * add the given matrix to this matrix
     * @param right the matrix to add
     * @return this matrix
     */
	public Matrix4f add(Matrix4f right) {
		
		m00 += right.m00;
		m01 += right.m01;
		m02 += right.m02;
		m03 += right.m03;
		m10 += right.m10;
		m11 += right.m11;
		m12 += right.m12;
		m13 += right.m13;
		m20 += right.m20;
		m21 += right.m21;
		m22 += right.m22;
		m23 += right.m23;
		m30 += right.m30;
		m31 += right.m31;
		m32 += right.m32;
		m33 += right.m33;
		
		return this;
	} 
	
    /**
     * subtract the given matrix this matrix
     * @param right the matrix to add
     * @return this matrix
     */
	public Matrix4f subThis(Matrix4f right) {
		
		m00 -= right.m00;
		m01 -= right.m01;
		m02 -= right.m02;
		m03 -= right.m03;
		m10 -= right.m10;
		m11 -= right.m11;
		m12 -= right.m12;
		m13 -= right.m13;
		m20 -= right.m20;
		m21 -= right.m21;
		m22 -= right.m22;
		m23 -= right.m23;
		m30 -= right.m30;
		m31 -= right.m31;
		m32 -= right.m32;
		m33 -= right.m33;
		
		return this;
	} 
	
    /**
     * subtract the given left matrix from the given right matrix
     * @param left
     * @param right
     * @return the result matrix
     */
	public static Matrix4f sub(Matrix4f left,Matrix4f right) {
		
		Matrix4f result = new Matrix4f();
		result.m00 = left.m00 - right.m00;
		result.m01 = left.m01 - right.m01;
		result.m02 = left.m02 - right.m02;
		result.m03 = left.m03 - right.m03;
		result.m10 = left.m10 - right.m10;
		result.m11 = left.m11 - right.m11;
		result.m12 = left.m12 - right.m12;
		result.m13 = left.m13 - right.m13;
		result.m20 = left.m20 - right.m20;
		result.m21 = left.m21 - right.m21;
		result.m22 = left.m22 - right.m22;
		result.m23 = left.m23 - right.m23;
		result.m30 = left.m30 - right.m30;
		result.m31 = left.m31 - right.m31;
		result.m32 = left.m32 - right.m32;
		result.m33 = left.m33 - right.m33;
		
		return result;
	} 
     

     /**
      * Rotates the source matrix around the given axis the specified angle and
      * put the result in the destination matrix.
      * @param src The matrix to rotate
      * @param inangle the angle, in radians.
      * @param axis The vector representing the rotation axis. Must be normalized.
      * @return The rotated matrix
      */
     public static Matrix4f rotate(Matrix4f src, float inangle, Vector3f axis) {

		axis.x *= -1;
		axis.y *= -1;
		axis.z *= -1;
		
		
		double angle = inangle*Math.PI/180;
		Matrix4f dest = new Matrix4f();
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float oneminusc = 1.0f - c;
		float xy = axis.x * axis.y;
		float yz = axis.y * axis.z;
		float xz = axis.x * axis.z;
		float xs = axis.x * s;
		float ys = axis.y * s;
		float zs = axis.z * s;

		float f00 = axis.x * axis.x * oneminusc + c;
		float f01 = xy * oneminusc + zs;
		float f02 = xz * oneminusc - ys;
		// n[3] not used
		float f10 = xy * oneminusc - zs;
		float f11 = axis.y * axis.y * oneminusc + c;
		float f12 = yz * oneminusc + xs;
		// n[7] not used
		float f20 = xz * oneminusc + ys;
		float f21 = yz * oneminusc - xs;
		float f22 = axis.z * axis.z * oneminusc + c;

		float t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
		float t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
		float t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
		float t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;
		float t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
		float t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
		float t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
		float t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;
		dest.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
		dest.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
		dest.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
		dest.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;
		dest.m00 = t00;
		dest.m01 = t01;
		dest.m02 = t02;
		dest.m03 = t03;
		dest.m10 = t10;
		dest.m11 = t11;
		dest.m12 = t12;
		dest.m13 = t13;
		return dest;
     }

     
//	/**
//	 * Rotates the source matrix around the given axis the specified angle and
//     * put the result in the destination matrix.
//	 * @param src The matrix to rotate
//	 * @param rotation The vector representing the rotation angles in degrees.
//	 * @return dest The matrix to put the result, or null if a new matrix is to be created
//	 */
//	public static Matrix4f rotateDegree(Matrix4f src, Vector3f rotation)
//	{
//		Matrix4f dest = Matrix4f.rotate(src, (float)(rotation.x/180f*Math.PI), new Vector3f(1f,0f,0f));
//        dest= Matrix4f.rotate(dest, (float)(rotation.y/180f*Math.PI), new Vector3f(0f,1f,0f));
//        dest= Matrix4f.rotate(dest, (float)(rotation.z/180f*Math.PI), new Vector3f(0f,0f,1f));
//
//		return dest;
//	}
//	
	/**
	 * Rotates the source matrix around the given axis the specified angle and
     * put the result in the destination matrix.
	 * @param src The matrix to rotate
	 * @param rotation The vector representing the rotation angles in radian.
	 * @return dest The matrix to put the result, or null if a new matrix is to be created
	 */
	public static Matrix4f rotateRadian(Matrix4f src, Vector3f rotation)
	{
		Matrix4f dest =src;
//		dest= Matrix4f.rotate(dest, (float)(rotation.x), new Vector3f(1f,0f,0f));
//        dest= Matrix4f.rotate(dest, (float)(rotation.y), new Vector3f(0f,1f,0f));
//        dest= Matrix4f.rotate(dest, (float)(rotation.z), new Vector3f(0f,0f,1f));
//		
//		// float z = (float)Math.atan2(R[1], R[5]);
//		// float y = (float)Math.asin(-R[9]);
//		// float x = (float)Math.atan2(-R[8], R[10]);

		return dest;
	}
	public Vector3f transform(Vector3f d)
	{
		return new Vector3f(
				m00*d.x+m01*d.y+m02*d.z, 
				m10*d.x+m11*d.y+m12*d.z, 
				m20*d.x+m21*d.y+m22*d.z);
	}

	/**
	 * set the rotation of the matrix around the x axis.
	 */
	public Matrix4f setRotateX (double degree)
	{
		float cosa = (float) Math.cos(degree/180f*Math.PI), sina = (float) Math.sin(degree/180f*Math.PI);
		m00 =     1; m01 =     0; m02 =     0; m03 = 0;
		m10 =     0; m11 =  cosa; m12 = -sina; m13 = 0;
		m20 =     0; m21 =  sina; m22 =  cosa; m23 = 0;
		m30 =     0; m31 =     0; m32 =     0; m33 = 1;
		
		return this;
	}
	/**
	 * set the rotation of the matrix around the y axis.
	 */
	public Matrix4f setRotateY (double degree)
	{
		float cosa = (float) Math.cos(degree/180f*Math.PI), sina = (float) Math.sin(degree/180f*Math.PI);
		m00 =  cosa; m01 =     0; m02 =  sina; m03 = 0;
		m10 =     0; m11 =     1; m12 =     0; m13 = 0;
		m20 = -sina; m21 =     0; m22 =  cosa; m23 = 0;
		m30 =     0; m31 =     0; m32 =     0; m33 = 1;
		
		return this;
	}
	/**
	 * set the rotation of the matrix around the z axis.
	 */
	public Matrix4f setRotateZ (double degree)
	{
		float cosa = (float) Math.cos(degree/180f*Math.PI), sina = (float) Math.sin(degree/180f*Math.PI);
		m00 =  cosa; m01 = -sina; m02 =     0; m03 = 0;
		m10 =  sina; m11 =  cosa; m12 =     0; m13 = 0;
		m20 =     0; m21 =     0; m22 =     1; m23 = 0;
		m30 =     0; m31 =     0; m32 =     0; m33 = 1;
		
		return this;
	}
	
	/**
	 * calculate the euler angles x=Pitch y=Yaw z=Roll from the matrix
	 * @return euler angle in Degree
	 */
	public Vector3f getEulerAngle()
	{
//				
//		System.out.println("------------------------------------new---------------------------------------------------");
// zuerst wird die ursprungsmatrix in die richtigen 4 quadranten gedreht, sprich bis x.x>0 && y.y>0 & z.z>0
Vector3f rotation = new Vector3f(0.f,0.f,0.f);

//for (int i = 0; i < 360; i+=90) {
//
//			Matrix4f mR = new Matrix4f(this);
//			mR=Matrix4f.rotate(mR, i, new Vector3f(1.f, 0.f, 0.f));
//
//			Vector3f rZ = mR.transform(new Vector3f(0.f, 0.f, -1.f));
//			
//			if(rZ.z>0 ){
//				rotation.set(0.f,0.f,(float)i);
//				i=360;
//			}
//		}
	
	Vector3f vx = new Vector3f(0.f, 0.f, 0.f);
	Vector3f vy = new Vector3f(0.f, 0.f, 0.f);
	Vector3f vz = new Vector3f(0.f, 0.f, 0.f);

		
	for (int z = 0; z < 360; z+=1) {
int y=0,x=0;//		for (int y = 0; y < 360; y+=90) {
//				for (int x = 0; x < 360; x+=90) {
				
					
						
					Matrix4f mR = new Matrix4f(this);
					
					
					// zuerst -z dann -y dann -x
					mR = Matrix4f.rotate(mR, -z, new Vector3f(0.f,0.f,1.f));
//					mR = Matrix4f.rotate(mR, -y, new Vector3f(0.f,1.f,0.f));
//					mR = Matrix4f.rotate(mR, -x, new Vector3f(1.f,0.f,0.f));

					
					vx.set(1.f, 0.f, 0.f);
					vy.set(0.f, 1.f, 0.f);
					vz.set(0.f, 0.f, 1.f);

					Vector3f rx = mR.transform(vx);
					Vector3f ry = mR.transform(vy);
					Vector3f rz = mR.transform(vz);
					
					
//					System.out.println(rx);
					if( Math.abs(ry.x)<0.005 ){
						rotation.set((float)x,(float)y,(float)z);
//						System.out.println("found      "+x+" "+ rotation);
						x=y=z=360;
					}
//				}
//			
//			}

	}

		
		return rotation;

	}

	
private float rotateBack(Matrix4f in) {
		
		Matrix4f m = new Matrix4f(in);
		
		Matrix4f tmZ = new Matrix4f().setRotateX(1.000000001f);

		// MR = mZ*mY*mZ :
		int rotation = 0;
		
		if(m.transform(new Vector3f(1.f,0.f,0.f)).x<0)
		{
			m = m.mult(new Matrix4f().setRotateX(180.f));
			rotation+=180;
		}
		
		for (int i = 0; i < 180; i++) {
			if (((Math.abs(m.transform(new Vector3f(0.f, 0.f, 1.f)).y) > 0.01)))

			{
				m = m.mult(tmZ);
				rotation++;
			}
		}
	
		rotation = 180-rotation;
		
		if(transform(new Vector3f(0.f, 0.f, 1.f)).y > 0)
			rotation+=180;
		
//		if(m.transform(new Vector3f(0.f, 0.f, 1.f)).z < 0)
//			zRotation*=-1;
		
		return rotation;
	}

	private float rotateBackZ(Matrix4f in) {
		
		Matrix4f m = new Matrix4f(in);
		Matrix4f tmZ = new Matrix4f();
		
		tmZ.setRotateZ(1.000000001f);

		// MR = mZ*mY*mZ :
		int zRotation = 0;
		
		if(m.transform(new Vector3f(0.f,0.f,1.f)).z<0)
		{
			m = m.mult(new Matrix4f().setRotateZ(180.f));
			zRotation+=180;
		}
		
		for (int i = 0; i < 180; i++) {
			if (((Math.abs(m.transform(new Vector3f(0.f, 1.f, 0.f)).x) > 0.01)))

			{
				m = m.mult(tmZ);
				zRotation++;
			}
		}
	
		zRotation = 180-zRotation;
		
		if(transform(new Vector3f(0.f, 1.f, 0.f)).x > 0)
			zRotation+=180;
		
//		if(m.transform(new Vector3f(0.f, 0.f, 1.f)).z < 0)
//			zRotation*=-1;
		
		return zRotation;
	}
	public void testGetEulerAngle()
	{

		int i = 3;// maximal test resolution
//		if(testGetEulerAngle(i++, true)){}
		while(i<8 && testGetEulerAngle(i, true))
			i++;
//		System.out.println("Genauichkeit == "+i);
		

	}
	
	/**
	 * this method test if the matrix to angle calculation works correct
	 * 
	 * @param resolution
	 *            the test resolution, must be >=2 [resolution is always
	 *            360/(resolution)] resolution=2 ==> 180° test angle
	 *            resolution=4 ==> 90° test angle resolution=8 ==> 45° test
	 *            angle
	 * @param showCorrectAngle
	 *            is the test modus mode = false ==> everything on output mode =
	 *            true ==> only error on output
	 * @return boolean true if the test was without errors
	 *! note this test - tests with opengl negative rotation direction!
	 */
	public static boolean testGetEulerAngle(int resolution, boolean showCorrectAngle)
	{
		Vector3f rotation = new Vector3f(0f, 0f, 0f);
		Vector3f euler = new Vector3f(0f, 0f, 0f);
		int error = 0;
		int loop = 0;
		DecimalFormat gf = new DecimalFormat("######000");

		if ((resolution) < 2){
			System.err
					.println("testGetEulerAngle: the test resolution is not correct! it must be >=0");
			return false;}
		float angle = 360f / (resolution);
		System.out.println("START: testRotaionCalculation testangle=" + angle);

		rotation.x = 0f;
		while (rotation.x < 360)
		{
			rotation.y = 0f;
			while (rotation.y < 360)
			{
				rotation.z = 0f;
				while (rotation.z < 360)
				{
					loop++;
					
					Matrix4f mR = new Matrix4f();
					
					mR = Matrix4f.rotate(mR, rotation.x, new Vector3f(1.f,0.f,0.f));
					mR = Matrix4f.rotate(mR, rotation.y, new Vector3f(0.f,1.f,0.f));
					mR = Matrix4f.rotate(mR, rotation.z, new Vector3f(0.f,0.f,1.f));
					
//					// test matrix wieder zurueckdrehen
//					mR = Matrix4f.rotate(mR, -rotation.z, new Vector3f(0.f,0.f,1.f));
//					mR = Matrix4f.rotate(mR, -rotation.y, new Vector3f(0.f,1.f,0.f));
//					mR = Matrix4f.rotate(mR, -rotation.x, new Vector3f(1.f,0.f,0.f));
//					System.out.println(mR);
					
					euler = mR.getEulerAngle();
					Vector3f d = Vector3f.subtract(rotation, euler);
					d = Vector3f.abs(d);
					d = Vector3f.eliminateEpsilon(d,1.0E-1f);

					if ((d.x % 360 + d.y % 360 + d.z % 360) > 0)
					{

						System.err.println(gf.format(rotation.x) + " ; "
								+ gf.format(rotation.y) + " ; "
								+ gf.format(rotation.z) + " ;       "

								+ gf.format(euler.x) + " ; "
								+ gf.format(euler.y) + " ; "
								+ gf.format(euler.z)+" ;                 "
						
						+ gf.format(d.x) + " ; "
						+ gf.format(d.y) + " ; "
						+ gf.format(d.z));
						
						error++;
					} else
					{
						if(showCorrectAngle)
						System.out.println(gf.format(rotation.x) + " ; "
								+ gf.format(rotation.y) + " ; "
								+ gf.format(rotation.z) + " ;       "

								+ gf.format(euler.x) + " ; "
								+ gf.format(euler.y) + " ; "
								+ gf.format(euler.z)+" ;                 "
								
								+ gf.format(d.x) + " ; "
								+ gf.format(d.y) + " ; "
								+ gf.format(d.z));
					}
					rotation.z += angle;
				}
				rotation.y += angle;
			}
			rotation.x += angle;
		}
		if (error == 0)
		{
			System.out.println("END: testRotaionCalculation with no errors");
			return true;
		}
		else
			System.err.println("END: testRotaionCalculation with " + error
					+ " error/s from "+loop);
		
		return false;
		

	}
	
	
		
	@Override
	public String toString()
	{
		DecimalFormat f = new DecimalFormat("######0.00000");
		return
		"--------------------Matrix4f--------------------\n"+
		"m00 = "+f.format(m00)+"      m01="+f.format(m01)+"      m02="+f.format(m02)+"      m03="+f.format(m03)+"\n"+
		"m10 = "+f.format(m10)+"      m11="+f.format(m11)+"      m12="+f.format(m12)+"      m13="+f.format(m13)+"\n"+
		"m20 = "+f.format(m20)+"      m21="+f.format(m21)+"      m22="+f.format(m22)+"      m23="+f.format(m23)+"\n"+
		"m30 = "+f.format(m30)+"      m31="+f.format(m31)+"      m32="+f.format(m32)+"      m33="+f.format(m33)+"\n"
		;
	}
//	public void calculateHashMap()
//	{
//		DecimalFormat f = new DecimalFormat("0.0");
//		map = new HashMap<String,Vector3f> ();
//		int angle = 360/80;
//		int count =	0;
//		
//		Vector3f rotation = new Vector3f(0.f,0.f,0.f);
//		rotation .x = 0f;
//		while (rotation.x < 360)
//		{
//			count++;
//			System.out.println("Prozent: "+(count*100/(360/angle)));
//			
//			rotation.y = 0f;
//			while (rotation.y < 360)
//			{
//				rotation.z = 0f;
//				while (rotation.z < 360)
//				{
//
//					Matrix4f mX = new Matrix4f();
//					mX.setRotateX(-rotation.x);
//					Matrix4f mY = new Matrix4f();
//					mY.setRotateY(-rotation.y);
//					Matrix4f mZ = new Matrix4f();
//					mZ.setRotateZ(-rotation.z);
//					// MR = mZ*mY*mZ :
//					Matrix4f m = mZ.mult(mY.mult(mX));
//					String key = 
//						f.format(m.m00) + "|" + f.format(m.m01) + "|" + f.format(m.m02) + "|" + 
//						f.format(m.m10) + "|" + f.format(m.m11) + "|" + f.format(m.m12) + "|"	+ 
//						f.format(m.m20) + "|" + f.format(m.m21) + "|" + f.format(m.m22);
//					
//					if(!map.containsKey(key))
//					{
//					 map.put(key, new Vector3f(rotation));
////					 System.out.println(key+ " "+rotation);
//					}
//
//					rotation.z += angle;
//				}
//				rotation.y += angle;
//			}
//			rotation.x += angle;
//		}
//		
//		System.out.println("map.size= "+map.size());
//		
//		
////		map.containsKey(arg0)
//		
//	}
	
    /**
     * Inverts this matrix locally.
     * 
     * @return this
     */
    public Matrix4f invert() {

        float fA0 = m00*m11 - m01*m10;
        float fA1 = m00*m12 - m02*m10;
        float fA2 = m00*m13 - m03*m10;
        float fA3 = m01*m12 - m02*
        m11;
        float fA4 = m01*m13 - m03*m11;
        float fA5 = m02*m13 - m03*m12;
        float fB0 = m20*m31 - m21*m30;
        float fB1 = m20*m32 - m22*m30;
        float fB2 = m20*m33 - m23*m30;
        float fB3 = m21*m32 - m22*m31;
        float fB4 = m21*m33 - m23*m31;
        float fB5 = m22*m33 - m23*m32;
        float fDet = fA0*fB5-fA1*fB4+fA2*fB3+fA3*fB2-fA4*fB1+fA5*fB0;

        if (Math.abs(fDet) <= 1.1920928955078125E-7f )
            return null;

        float f00 = + m11*fB5 - m12*fB4 + m13*fB3;
        float f10 = - m10*fB5 + m12*fB2 - m13*fB1;
        float f20 = + m10*fB4 - m11*fB2 + m13*fB0;
        float f30 = - m10*fB3 + m11*fB1 - m12*fB0;
        float f01 = - m01*fB5 + m02*fB4 - m03*fB3;
        float f11 = + m00*fB5 - m02*fB2 + m03*fB1;
        float f21 = - m00*fB4 + m01*fB2 - m03*fB0;
        float f31 = + m00*fB3 - m01*fB1 + m02*fB0;
        float f02 = + m31*fA5 - m32*fA4 + m33*fA3;
        float f12 = - m30*fA5 + m32*fA2 - m33*fA1;
        float f22 = + m30*fA4 - m31*fA2 + m33*fA0;
        float f32 = - m30*fA3 + m31*fA1 - m32*fA0;
        float f03 = - m21*fA5 + m22*fA4 - m23*fA3;
        float f13 = + m20*fA5 - m22*fA2 + m23*fA1;
        float f23 = - m20*fA4 + m21*fA2 - m23*fA0;
        float f33 = + m20*fA3 - m21*fA1 + m22*fA0;
        
        m00 = f00;
        m01 = f01;
        m02 = f02;
        m03 = f03;
        m10 = f10;
        m11 = f11;
        m12 = f12;
        m13 = f13;
        m20 = f20;
        m21 = f21;
        m22 = f22;
        m23 = f23;
        m30 = f30;
        m31 = f31;
        m32 = f32;
        m33 = f33;

        return this;
    }



}
