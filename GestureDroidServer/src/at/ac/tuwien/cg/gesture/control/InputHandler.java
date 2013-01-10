package at.ac.tuwien.cg.gesture.control;

import at.ac.tuwien.cg.gesture.data.MatlabLogger;
import at.ac.tuwien.cg.gesture.data.MeasuringPoint;
import at.ac.tuwien.cg.gesture.data.MeasuringPointStorage;
import at.ac.tuwien.cg.gesture.math.Matrix4f;
import at.ac.tuwien.cg.gesture.math.Vector3f;

/**
 * 
 * @author This class is responsible for all input events. If an input Device
 *         like the Wii-Remote or an Mobilephone send new Data, the new Data are
 *         delivered to this class (eg.: by calling the receive method).
 */
public class InputHandler
{
	// relative acceleration from the device
	public static Vector3f MPAcceleration = new Vector3f(0.0f, 0.0f, 0.0f);
	// rotation matrix from the device orientation
	public static Matrix4f MPOrientationMatrix = new Matrix4f();
	// covered distance 
	public static Vector3f MPDistance = new Vector3f(0.0f, 0.0f, 0.0f);
	// actually velocity
	public static Vector3f MPVelocity = new Vector3f(0.0f, 0.0f, 0.0f);
	// important data or not
	public static boolean MPIsRealMeasurement = false;
	// measurement ID
	public static int MPID = 0;
	
	public static long tempTime=0;
	
	private static MatlabLogger mlog = new  MatlabLogger();
	
	public static MeasuringPointStorage mpStorage;

	public InputHandler(MeasuringPointStorage mpStorage){
		this.mpStorage = mpStorage;
	}

	/**
	 * @param object received Object, if it is a MeasuringPoint then it set the actual parameter, else it call toString method
	 */
	public static synchronized void receive(Object object)
	{
		try
		{
		
			
			if (object instanceof MeasuringPoint)
			{
				MeasuringPoint m = (MeasuringPoint) object;
				
				
	
				MPAcceleration.set(m.getAcceleration());
				MPOrientationMatrix.set(m.getOrientationMatrix());
				MPDistance.set(m.getDistance());
				MPVelocity.set(m.getVelocity());
				MPIsRealMeasurement = m.getIsRealMeasurement();
				MPID = m.getMeasurementID();
				
				if(MPIsRealMeasurement)
				{
					if(mpStorage.getMeasurementID()==MPID)
						mpStorage.addObject(m);
					else
					{
						mpStorage.removeAll();
						mpStorage.setMeasurementID(MPID);
						mpStorage.addObject(m);
					}
					System.out.println("Storage-Size: "+mpStorage.getSize()+" "+m);
					System.out.println(" "+m.getDistance()+" "+m.getVelocity());
					if(m.getTime()<tempTime)
						System.err.println(m.getTime());
					tempTime=m.getTime();
					
					if(m.getRowData()!=null)
						mlog.addData(m.getRowData(),m.getDeltaTime());
					else
						System.err.println("rowdata=null!!!!");
					
					
					
				}
				else
				{
					// write to Matlab readable File 
					mlog.writeData();	
					
				}
				
				//System.out.println(MPAcceleration);
	
			} 
			else
			{
				System.out.println(object);
			}
			
		}
		catch (Exception e) {
			System.err.println("Exception im InputHandler:");
			e.printStackTrace();
		}

	}

}
