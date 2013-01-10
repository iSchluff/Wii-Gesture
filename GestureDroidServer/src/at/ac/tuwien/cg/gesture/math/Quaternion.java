package at.ac.tuwien.cg.gesture.math;

import java.text.DecimalFormat;

public class Quaternion {
	
	private float x;
	private float y;
	private float z;
	private float w;
	
	final float rad2deg = (float)(180.0f/Math.PI);
	final float deg2rad = (float)(Math.PI/180.0f);
	
	public Quaternion(){
		
	}
	
	public Quaternion(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
//	public Quaternion(Vector3f vc, float phi){
//		this.x = vc.x * (float)Math.sin(phi/2);
//		this.y = vc.y * (float)Math.sin(phi/2);
//		this.z = vc.z * (float)Math.sin(phi/2);
//		this.w = (float)Math.cos(phi/2);
//	}
	
    public Quaternion(Matrix4f m) {

		float t = m.m00 + m.m11 + m.m22;

		if (t >= 0) 
		{
			float s = (float) Math.sqrt(t + 1);
			w = 0.5f * s;
			s = 0.5f / s;
			x = (m.m21 - m.m12) * s;
			y = (m.m02 - m.m20) * s;
			z = (m.m10 - m.m01) * s;
		} 
		else if ((m.m00 > m.m11) && (m.m00 > m.m22)) 
		{
			float s = (float) Math.sqrt(1.0 + m.m00 - m.m11 - m.m22);
			x = s * 0.5f; // |x| >= .5
			s = 0.5f / s;
			y = (m.m10 + m.m01) * s;
			z = (m.m02 + m.m20) * s;
			w = (m.m21 - m.m12) * s;
		} else if (m.m11 > m.m22) 
		{
			float s = (float) Math.sqrt(1.0 + m.m11 - m.m00 - m.m22);
			y = s * 0.5f; // |y| >= .5
			s = 0.5f / s;
			x = (m.m10 + m.m01) * s;
			z = (m.m21 + m.m12) * s;
			w = (m.m02 - m.m20) * s;
		} else 
		{
			float s = (float) Math.sqrt(1.0 + m.m22 - m.m00 - m.m11);
			z = s * 0.5f;
			s = 0.5f / s;
			x = (m.m02 + m.m20) * s;
			y = (m.m21 + m.m12) * s;
			w = (m.m10 - m.m01) * s;
		}
    }
    
    public Matrix4f getRotationMatrix() {

    	Matrix4f matrix = new Matrix4f();
        float norm = norm();
        float s = (norm==1f) ? 2f : (norm > 0f) ? 2f/norm : 0;
        
        float xs      = x * s;
        float ys      = y * s;
        float zs      = z * s;
        float xx      = x * xs;
        float xy      = x * ys;
        float xz      = x * zs;
        float xw      = w * xs;
        float yy      = y * ys;
        float yz      = y * zs;
        float yw      = w * ys;
        float zz      = z * zs;
        float zw      = w * zs;

        matrix.m00  = 1 - ( yy + zz );
        matrix.m01  =     ( xy - zw );
        matrix.m02  =     ( xz + yw );
        matrix.m10  =     ( xy + zw );
        matrix.m11  = 1 - ( xx + zz );
        matrix.m12  =     ( yz - xw );
        matrix.m20  =     ( xz - yw );
        matrix.m21  =     ( yz + xw );
        matrix.m22  = 1 - ( xx + yy );

        return matrix;
    }
    
  public Vector3f getAngles(Vector3f angle) {
	if (angle == null)
		angle = new Vector3f(0.f, 0.f, 0.f);
    

	double norm = x * x + y * y + z * z + w * w;
	double s;
	if ( norm > 0.0 ) { s = 2.0 / norm; }
	else { s = 0.0; }
	
	double xx = 1.0 - s*( y*y + z*z );
	double xy = s*( x*y - w*z );
	double xz = s*( x*z + w*y );
	
	double yx = s*( x*y + w*z );
	double yy = 1.0 - s*( x*x + z*z );
	double yz = s*( y*z - w*x );

	double zz = 1.0 - s*( x*x + y*y );

	double cy = Math.sqrt( zz*zz + xz*xz );
	if ( cy > 16.0 * Float.MIN_VALUE) {
		angle.z = (float)Math.atan2( yx, yy );
		angle.x = (float)Math.atan2( -yz, cy );
		angle.y = (float)Math.atan2( xz, zz );
	}
	else {
		angle.z = (float)Math.atan2( -xy, xx );
		angle.x = (float)Math.atan2( -yz, cy );
		angle.y = 0.f;
	}
	
	angle = Vector3f.mult(angle, rad2deg);
	
//	if (getRotationMatrix().transform(new Vector3f(1.f,0.f,0.f)).x<0)
//		angles.x*=-1;
//	
	
	if (getRotationMatrix().transform(new Vector3f(0.f,1.f,0.f)).y<0)
		angle.y*=-1;
	
	if (getRotationMatrix().transform(new Vector3f(0.f,0.f,1.f)).z<0)
		angle.z*=-1;
	
	
//	yz=angle.y*angle.z;
//	yz/=398.0f;
//	
//	if(Math.abs(yz)>0){
//		angle.x+=(float)yz;
//	System.out.println("yz= "+yz);	
//	}
//	

		
	
	return angle;
}
  
//    
//    public Vector3f getAngles(Vector3f angles) {
//		if (angles == null)
//			angles = new Vector3f(0.f, 0.f, 0.f);
//
//		float sqw = w * w;
//		float sqx = x * x;
//		float sqy = y * y;
//		float sqz = z * z;
//		float unit = sqx + sqy + sqz + sqw; 
//		float test = x * y + z * w;
//		if (test > 0.499 * unit) { 
//			angles.y = (float) (2 * Math.atan2(x, w));
//			angles.z = (float) Math.PI/2;
//			angles.x = 0.f;
//		} else if (test < -0.499 * unit) {
//			angles.y = (float) ( -2 * Math.atan2(x, w));
//			angles.z = (float) (-Math.PI/2.f);
//			angles.x = 0.f;
//		} else {
//			angles.y = (float) Math.atan2(2 * y * w - 2 * x * z, sqx - sqy - sqz + sqw);
//			angles.z = (float) Math.asin(2 * test / unit);
//			angles.x = (float) Math.atan2(2 * x * w - 2 * y * z, -sqx + sqy - sqz + sqw);
//		}
//		
//		angles=Vector3f.mult(angles, rad2deg);
//		
//		;
//		
//		if (
//				getRotationMatrix().transform(new Vector3f(1.f,0.f,0.f)).x<0&&
//				
//				getRotationMatrix().transform(new Vector3f(0.f,0.f,1.f)).z>0)
//			angles.z*=-1;
//		
//		
//		return angles;
//	}


    public float norm() {
        return w * w + x * x + y * y + z * z;
    }
	
//	public Quaternion(Vector3f vc1, Vector3f vc2){
//		
//		Vector3f crossVc = new Vector3f(0.0f, 0.0f, 0.0f);
//		float phi = 0.0f;
//		
//		crossVc = Vector3f.crossProduct(vc1, vc2);
//		phi = (float)Math.acos(Vector3f.dotProduct(vc1, vc2));
//		
//		this.x = crossVc.x * (float)Math.sin(phi/2);
//		this.y = crossVc.y * (float)Math.sin(phi/2);
//		this.z = crossVc.z * (float)Math.sin(phi/2);
//		this.w = (float)Math.cos(phi/2);
//	}
	
//	public Vector3f getEulerAngles(){
//		
//		Vector3f eulerAngles = new Vector3f(0.0f, 0.0f, 0.0f);
//		
//		eulerAngles.set((float)Math.atan2(2*(this.w*this.x+this.y*this.z), 1-2*(this.x*this.x+this.y*this.y))*rad2deg, 
//						(float)(Math.asin(2*(this.w*this.y-this.z*this.x))*rad2deg), 
//						(float)Math.atan2(2*(this.w*this.z+this.x*this.y), 1-2*(this.y*this.y+this.z*this.z))*rad2deg);
//		
//		return eulerAngles;
//	}
//	
//	public Vector3f compareQuaternion(Vector3f vcX, Vector3f vcY, Vector3f vcZ){
//		
//		Vector3f eulerAngles = new Vector3f((vcY.x+vcZ.x)/2,
//											(vcX.y+vcZ.y)/2,
//											(vcX.z+vcY.z)/2);
//		
//		return eulerAngles;
//	}
//	
//	//***********************************************
//	// ACHTUNG gehört nicht zu Quaterionen
//	//***********************************************
//	public Vector3f calculateAngles(Vector3f vc, float angle){
//		
//		//vc.set(1f, 0f, 0f);
//		//angle =90f;
//		double heading = 0.0;
//		double attitude = 0.0;
//		double bank = 0.0;
//		
//		double sin=Math.sin(angle*deg2rad);
//		double cos=Math.cos(angle*deg2rad);
//		double t=1-cos;
//
//		/*
//		if ((vc.x*vc.y*t + vc.z*sin) > 0.998) {
//			heading = 2*Math.atan2(vc.x*Math.sin(angle/2*deg2rad),Math.cos(angle/2*deg2rad));
//			attitude = Math.PI/2;
//			bank = 0;
//			Vector3f angles = new Vector3f((float)(heading*rad2deg), (float)(attitude*rad2deg), (float)(bank*rad2deg));
//			return angles;
//		}
//		if ((vc.x*vc.y*t + vc.z*sin) < -0.998) {
//			heading = -2*Math.atan2(vc.x*Math.sin(angle/2*deg2rad),Math.cos(angle/2*deg2rad));
//			attitude = -Math.PI/2;
//			bank = 0;
//			Vector3f angles = new Vector3f((float)(heading*rad2deg), (float)(attitude*rad2deg), (float)(bank*rad2deg));
//			return angles;
//		}*/
//		heading = Math.atan2(vc.y * sin- vc.x * vc.z * t , 1 - (vc.y*vc.y+ vc.z*vc.z ) * t);
//		attitude = Math.asin(vc.x * vc.y * t + vc.z * sin) ;
//		bank = Math.atan2(vc.x * sin - vc.y * vc.z * t , 1 - (vc.x*vc.x + vc.z*vc.z) * t);
//
//		Vector3f angles = new Vector3f((float)(heading*rad2deg), (float)(attitude*rad2deg), (float)(bank*rad2deg));
//		
//		return angles;
//	}
	public void testMatrixToQuaternion()
	{

		int i =  2;// minimal test resolution
		while(i< 36// maximal test resolution
				&& testMatrixToQuaternion(i, false))
			i++;

		

	}
    
    /**
	 * this method test if the matrix to quanternion calculation works correct
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
	public static boolean testMatrixToQuaternion(int resolution, boolean showCorrectAngle)
	{
		Vector3f rotation = new Vector3f(0f, 0f, 0f);
		Vector3f euler = new Vector3f(0f, 0f, 0f);
		int error = 0;
		int loop = 0;
		DecimalFormat gf = new DecimalFormat("######000.0");

		if ((resolution) < 2){
			System.err
					.println("testMatrixToQuaternion: the test resolution is not correct! it must be >=0");
			return false;}
		float angle = 360f / (resolution);
		System.out.println("START: testMatrixToQuaternion testangle=" + angle);

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
					Matrix4f mX = new Matrix4f();
					mX.setRotateX(-rotation.x);
					Matrix4f mY = new Matrix4f();
					mY.setRotateY(rotation.y);
					Matrix4f mZ = new Matrix4f();
					mZ.setRotateZ(-rotation.z);
					// MR = mZ*mY*mZ :
					Matrix4f mR = mZ.mult(mY.mult(mX));
					Quaternion q = new Quaternion(mR);

					if (!q.getRotationMatrix().equals(mR))
					{

						System.err.println(gf.format(rotation.x) + " ; "
								+ gf.format(rotation.y) + " ; "
								+ gf.format(rotation.z));
						
						System.err.println(q.getRotationMatrix());
						System.err.println(mR);
						
						error++;
					} else
					{
						if(showCorrectAngle)
						System.out.println(gf.format(rotation.x) + " ; "
								+ gf.format(rotation.y) + " ; "
								+ gf.format(rotation.z) + " ;       "

								+ gf.format(euler.x) + " ; "
								+ gf.format(euler.y) + " ; "
								+ gf.format(euler.z));
					}
					rotation.z += angle;
				}
				rotation.y += angle;
			}
			rotation.x += angle;
		}
		if (error == 0)
		{
			System.out.println("END: testMatrixToQuaternion with no errors");
			return true;
		}
		else
			System.err.println("END: testMatrixToQuaternion with " + error
					+ " error/s from "+loop);
		
		return false;
		

	}


}
