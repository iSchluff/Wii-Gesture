package at.ac.tuwien.cg.gesture.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import at.ac.tuwien.cg.gesture.math.Vector3f;


public class DeviceCalibrationData {

	private Vector3f accelerationMin;
	private Vector3f accelerationMax;
	private Vector3f angleCorrectionPerSecound;
	private double actualEarthGravity;
	private String id;
	final static String PROPERTIE_FILE = "DeviceCalibrationData.txt";

	public DeviceCalibrationData(Vector3f accelerationMin,
			Vector3f accelerationMax,Vector3f angleCorrectionPerSecound, String id) {

		this.accelerationMin = Vector3f.divide(new Vector3f(1.f, 1.f, 1.f), accelerationMin);
		this.accelerationMax = Vector3f.divide(new Vector3f(1.f, 1.f, 1.f), accelerationMax);
		this.angleCorrectionPerSecound = angleCorrectionPerSecound;

		this.actualEarthGravity = 1;
		this.id=id;
		// TODO Auto-generated constructor stub
	}

	/**
	 * this method will load the specific calibration data for an unique
	 * device-id , if they exist!
	 * 
	 * @return
	 */
	public static DeviceCalibrationData loadDeviceCalibrationData(String id) {

		// load the data
		Properties properties = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(PROPERTIE_FILE);
			properties.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vector3f min = new Vector3f(
				Float.valueOf(properties.getProperty("aMinX"+id, "1")),
				Float.valueOf(properties.getProperty("aMinY"+id, "1")),
				Float.valueOf(properties.getProperty("aMinZ"+id, "1")));
		Vector3f max = new Vector3f(
				Float.valueOf(properties.getProperty("aMaxX"+id, "1")),
				Float.valueOf(properties.getProperty("aMaxY"+id, "1")),
				Float.valueOf(properties.getProperty("aMaxZ"+id, "1")));
		
		Vector3f acd = new Vector3f(
				Float.valueOf(properties.getProperty("aAcdX"+id, "0")),
				Float.valueOf(properties.getProperty("aAcdY"+id, "0")),
				Float.valueOf(properties.getProperty("aAcdZ"+id, "0")));
	
		// if not exist return null
		return new DeviceCalibrationData(min, max,acd ,id);

	}
	public void saveDeviceCalibrationData(){
		
		FileOutputStream out;
		try {
			out = new FileOutputStream(PROPERTIE_FILE);
			Properties properties = new Properties();
			
			properties.setProperty("aMinX"+id, String.valueOf(1/accelerationMin.x));
			properties.setProperty("aMinY"+id, String.valueOf(1/accelerationMin.y));
			properties.setProperty("aMinZ"+id, String.valueOf(1/accelerationMin.z));
			
			properties.setProperty("aMaxX"+id, String.valueOf(1/accelerationMax.x));
			properties.setProperty("aMaxY"+id, String.valueOf(1/accelerationMax.y));
			properties.setProperty("aMaxZ"+id, String.valueOf(1/accelerationMax.z));
			
			properties.setProperty("aAcdX"+id, String.valueOf(angleCorrectionPerSecound.x));
			properties.setProperty("aAcdY"+id, String.valueOf(angleCorrectionPerSecound.y));
			properties.setProperty("aAcdZ"+id, String.valueOf(angleCorrectionPerSecound.z));
			
			properties.store(out, "-store-");
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	public Vector3f calibrateAcceleration(Vector3f in) {

		Vector3f out = new Vector3f(in);
		if (in.x > 0)
			out.x=(in.x * accelerationMax.x);
		else
			out.x=(in.x * accelerationMin.x);

		if (in.y > 0)
			out.y=(in.y * accelerationMax.y);
		else
			out.y=(in.y * accelerationMin.y);

		if (in.z > 0)
			out.z=(in.z * accelerationMax.z);
		else
			out.z=(in.z * accelerationMin.z);

		return out;
	}

	public Vector3f getAccelerationMin() {
		return accelerationMin;
	}

	public void setAccelerationMin(Vector3f accelerationMin) {
		this.accelerationMin = accelerationMin;
	}

	public Vector3f getAccelerationMax() {
		return accelerationMax;
	}

	public void setAccelerationMax(Vector3f accelerationMax) {
		this.accelerationMax = accelerationMax;
	}

	public void setAccelerationMinX(float x) {
		this.accelerationMin.x=(1.f/x);
	}

	public void setAccelerationMinY(float y) {
		this.accelerationMin.y=(1.f/y);
	}

	public void setAccelerationMinZ(float z) {
		this.accelerationMin.z=(1.f/z);
	}

	public void setAccelerationMaxX(float x) {
		this.accelerationMax.x=(1.f/x);
	}

	public void setAccelerationMaxY(float y) {
		this.accelerationMax.y=(1.f/y);
	}

	public void setAccelerationMaxZ(float z) {
		this.accelerationMax.z=(1.f/z);
	}
	
	@Override
	public String toString() {
		return "Min:"+accelerationMin+" Max:"+accelerationMax+ " ID:"+id;
	}

	public Vector3f calibrateAngle(Vector3f angle, long deltaTime) {
		return Vector3f.add(Vector3f.mult(angleCorrectionPerSecound, deltaTime), angle);
	}

}
