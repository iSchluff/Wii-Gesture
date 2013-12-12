/*
 * Copyright 2007-2008 Volker Fritzsch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package at.ac.tuwien.cg.gesture.wii;

import java.util.Collections;
import java.util.Vector;

import motej.Extension;
import motej.Mote;
import motej.event.AccelerometerEvent;
import motej.event.AccelerometerListener;
import motej.event.CoreButtonEvent;
import motej.event.CoreButtonListener;
import motej.request.ReportModeRequest;
import motejx.extensions.motionplus.MotionPlus;
import motejx.extensions.motionplus.MotionPlusEvent;
import motejx.extensions.motionplus.MotionPlusListener;
import at.ac.tuwien.cg.gesture.data.DeviceCalibrationData;
import at.ac.tuwien.cg.gesture.data.MeasuringPoint;
import at.ac.tuwien.cg.gesture.math.Matrix4f;
import at.ac.tuwien.cg.gesture.math.Vector3f;

/**
 * 

 */
public class WiiRemoteClient {

	static MotionPlus motionplus;
	static AccelerometerEvent<Mote> accelerometerEvent;
	static MotionPlusEvent motionPlusEvent;
	static long lasttime= 0l;
	static double timeCount=0l;
	static int roundCount = 0;
	static DeviceCalibrationData dcd ;//= new DeviceCalibrationData(new Vector3f(1, 1, 1), new Vector3f(1, 1, 1));
	
//	static DeviceCalibrationData dcd = new DeviceCalibrationData(new Vector3f(1, 1, 0.897196), new Vector3f(1, 1, 1.009345));
	
	final static int MODE_NEUTRAL= -1;
	final static int MODE_RECEIVEDATA =0;
	final static int MODE_CALIBRATE = 1;
	final static int MODE_SHUTHDOWN =999;
	static int mode = MODE_NEUTRAL;
	protected static boolean calibrateAxis;
	protected static boolean calibrateAngles;
	
	final static int calibationLength = 80;
	private static Vector<Vector3f> axisCalibrationData = null;
	protected static int lastmode;
	protected static boolean NoPreviesButtonWasPressed;
	static double lastCalibrationDifference =0;
	static ConnectionHandler connetion;
	static long lastButtonTime=System.currentTimeMillis();
	static int measuringID=0;

	
	

	public static void main(String[] args) throws InterruptedException{
		connetion= new ConnectionHandler();
		connetion.start();
//		connetion.connectSocket();
		
//		System.setProperty("bluecove.jsr82.psm_minimum_off", "true");
		AccelerometerListener<Mote> listener = new AccelerometerListener<Mote>() {
		
			public void accelerometerChanged(AccelerometerEvent<Mote> evt) {
				boolean modeChanged = (mode!=lastmode);
				if(modeChanged)
				{
					System.out.println("modeChanged to: "+mode+" last mode was: "+lastmode);
					MeasuringPoint m= new MeasuringPoint(new Vector3f(0f,0f,0f),new Matrix4f(),0l,false,new Vector3f(0f,0f,0f),new Vector3f(0f,0f,0f),measuringID);
					connetion.addToQueue(m);
					measuringID++;
					
				}
				
				accelerometerEvent=evt;
				
				if(motionPlusEvent!=null){
					if(lasttime==0l)
						lasttime = System.currentTimeMillis();
					
					timeCount+=((System.currentTimeMillis()-lasttime));
					
//					if(roundCount!=0 && mode==MODE_RECEIVEDATA)
//						System.out.println("round:"+roundCount+" time:"+timeCount/1000+" ="+(roundCount/timeCount*1000)+"data/secound");
//					
					
					Vector3f acceleration = new Vector3f((float)accelerometerEvent.getX(), (float)accelerometerEvent.getY(), (float)accelerometerEvent.getZ());
					dcd.calibrateAcceleration(acceleration);
					
					Vector3f orientation = new Vector3f((float)motionPlusEvent.getPitch() , (float)motionPlusEvent.getRoll(),(float)motionPlusEvent.getYaw());
					
					// calculate orientation matrix from orientation angles
					Matrix4f orientationMatrix = new Matrix4f();
					orientationMatrix=Matrix4f.rotate(orientationMatrix, -orientation.x*1.2f, new Vector3f(1.f,0.f,0.f));
					orientationMatrix=Matrix4f.rotate(orientationMatrix, -orientation.y*1.2f, new Vector3f(0.f,1.f,0.f));
					orientationMatrix=Matrix4f.rotate(orientationMatrix, -orientation.z*1.2f, new Vector3f(0.f,0.f,1.f));
					
					// calculate opposing acceleration from the orientation Matrix
					Vector3f opposingAcceleration = new Vector3f(0.f, 0.f, 1.f);
					Matrix4f opposingMatrix = new Matrix4f(orientationMatrix).invert();
					opposingAcceleration= opposingMatrix.transform(opposingAcceleration);
					
					//opposingAcceleration.z*=-1;//TODO: wy *-1 ??
					
//					System.out.println("------------------------------------");
//					
//					System.out.println("acceleration: "+acceleration);
//					System.out.println("opposing    : "+opposingAcceleration);
//					
					//acceleration.add(opposingAcceleration);
//					System.out.println("result    : "+opposingAcceleration);
					
					
					MeasuringPoint m = new MeasuringPoint(acceleration,orientationMatrix, lasttime,true,new Vector3f(0f,0f,0f),new Vector3f(0f,0f,0f),measuringID);
					
			    	Vector3f[] raw = new Vector3f[2];
			    	raw[0]=new Vector3f(acceleration);
			    	raw[1]=orientation;
			    	m.setRowData(raw);
			    	
		
					
					if(mode==MODE_CALIBRATE){
						if(modeChanged)
						{
							System.out.println("Press A to calibrate an axis. You mus calibrate each axis in positive and negative direction");
							System.out.println("actual calibrationData: "+dcd);
						}
						if(calibrateAxis)
						{
							if(axisCalibrationData==null)
								axisCalibrationData = new Vector<Vector3f>(calibationLength,calibationLength);
						
							System.out.println(axisCalibrationData.size()+" >="+calibationLength);
							if(axisCalibrationData.size() < calibationLength )
							{
								axisCalibrationData.add(acceleration);
								if(lastCalibrationDifference!=0)
								System.out.println("diff="+lastCalibrationDifference+" acc="+acceleration);
							}
							else// now the vector is full
							{
								Vector3f sum = new Vector3f(0.f,0.f,0.f);
								for(int i= 0;i<axisCalibrationData.size();i++)
									sum.add(axisCalibrationData.get(i).getAbs());
								
								System.out.println("summe:"+sum.x+" / "+sum.y+" / "+sum.z);
								
								int axis=0;
								// now decide axis calibration direction
								if		(sum.x>sum.y&&sum.x>sum.z)
									axis=0;
								else if	(sum.y>sum.x&&sum.y>sum.z)
									axis=1;
								else if	(sum.z>sum.x&&sum.z>sum.y)
									axis=2;
								
								if(sum.x*sum.y*sum.z>(calibationLength*2))
								{
										System.out.println("axis="+axis+" too inaccurate! i try it again!");
										axisCalibrationData= null;
										lastCalibrationDifference=(sum.x*sum.y*sum.z);
//										try {
//											Thread.sleep(2000L);
//										} catch (InterruptedException e) {
//											e.printStackTrace();
//										}
										return;
								};
								
								Vector<Float> data = new Vector<Float> (calibationLength,calibationLength);
								
								
								for(int i= 0;i<axisCalibrationData.size();i++)
								{
									if		(axis==0)
										data.add(axisCalibrationData.get(i).x);
									else if	(axis==1)
										data.add(axisCalibrationData.get(i).y);
									else 
										data.add(axisCalibrationData.get(i).z);
								}
								
								// now sort vector
								Collections.sort(data);
								System.out.println("data size:"+data.size()+" source size:"+axisCalibrationData.size());
								// and get the media
								float newCalValue = median(data);
								System.out.println("axis="+axis+" newCalValue:"+newCalValue);
								if(newCalValue>0)
								{ 
								  if(axis==0)
									dcd.setAccelerationMaxX(newCalValue);
								  if(axis==1)
									dcd.setAccelerationMaxY(newCalValue);
								  else
									dcd.setAccelerationMaxZ(newCalValue);
								}
									
								else
								{
									if(axis==0)
										dcd.setAccelerationMinX(-newCalValue);
									  if(axis==1)
										dcd.setAccelerationMinY(-newCalValue);
									  else
										dcd.setAccelerationMinZ(-newCalValue);
									
								}

								// and finaly set the calibrated axis data to the DeviceCalibrationData
								System.out.println("new CalibrationData:"+dcd);
								dcd.saveDeviceCalibrationData();
								System.out.println("store new Data");
								axisCalibrationData=null;
								calibrateAxis=false;
								lastCalibrationDifference=0;
								
								
							}
							

//							axisCalibrationData
						}
		
						
					}

//					double absAcceleration = Math.sqrt(
//							 accelerometerEvent.x*accelerometerEvent.x
//							+accelerometerEvent.y*accelerometerEvent.y
//							+accelerometerEvent.z*accelerometerEvent.z);
					
					if(mode==MODE_RECEIVEDATA){
						
//						System.out.println("------------------------------------------------------");
//					System.out.println(m.getAcceleration());
//					System.out.println("with CalibrationData:"+dcd);

//					System.out.println(m.getAcceleration());
					
					connetion.addToQueue(m);
					
					}
//					System.out.println(absAcceleration+" I "+accelerometerEvent.x + " : " + accelerometerEvent.y + " : " + accelerometerEvent.z);
//					System.out.println(motionPlusEvent.getPitch() + " : " + motionPlusEvent.getRoll() + " : " + motionPlusEvent.getYaw());
					
					
					roundCount++;
					lasttime = System.currentTimeMillis();
					WiiRemoteClient.lastmode=mode;
				}
				
				
			}
		
		};
		
		MotionPlusListener motionPlusListener = new MotionPlusListener() {
			
			public void speedChanged(MotionPlusEvent evt) {
				motionPlusEvent=evt;
				// TODO Auto-generated method stub
//				if(WiiRemoteDemo.showData)
//				System.out.println(evt.toString());
				
			}
		};
		
		CoreButtonListener buttonListener = new CoreButtonListener() {
			
			public void buttonPressed(CoreButtonEvent evt) {
				
				
				if(!(evt.isButtonAPressed()||evt.isButtonBPressed()||evt.isButtonHomePressed()||evt.isButtonMinusPressed()||evt.isButtonOnePressed()||evt.isButtonPlusPressed()||evt.isButtonTwoPressed()||evt.isDPadDownPressed()||evt.isDPadLeftPressed()||evt.isDPadRightPressed()||evt.isDPadUpPressed()))
				{
					return;
				}
				//System.out.println(System.currentTimeMillis()-lastButtonTime);
				// nur reagieren wenn mindestens 500 msec vergangen sind
				if(System.currentTimeMillis()-lastButtonTime<500)
					return;
				lastButtonTime=System.currentTimeMillis();
				
				
				
				if(evt.isNoButtonPressed())
					WiiRemoteClient.NoPreviesButtonWasPressed=true;
				
//				System.out.println("Button:"+evt.getButton());
				
				if(evt.isButtonHomePressed()&&NoPreviesButtonWasPressed)
				{
					
					mode=MODE_SHUTHDOWN;
				}
				else if(evt.isButtonOnePressed()&&evt.isButtonTwoPressed()){
					System.out.println("A+B");
					if(mode!=MODE_CALIBRATE)
					mode=MODE_CALIBRATE;
					
					else if(mode==MODE_CALIBRATE)
						mode=MODE_RECEIVEDATA;
					try {
						Thread.sleep(5L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
					
				}
				else if(evt.isButtonBPressed()){
					System.out.println("B");
					if(mode==MODE_NEUTRAL)
						mode=MODE_RECEIVEDATA;
					
					if(motionplus!=null&&mode==MODE_CALIBRATE&& !calibrateAngles){
						
						System.out.println("calibrate angle diffs per secound in 3 sec...");
						try {
							Thread.sleep(1000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("calibrate angle diffs per secound in 2 sec...");
						try {
							Thread.sleep(1000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("calibrate angle diffs per secound in 1 sec...");
						try {
							Thread.sleep(1000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						
						System.out.println("TODO: calibrate angle diffs per secound");
						//TODO: get angle diffs per secound
						calibrateAngles=true;
						
					}
						
					
				
				}
				else if(evt.isButtonAPressed()){
					System.out.println("A");
					
					
					
					if(mode==MODE_NEUTRAL)
						mode=MODE_RECEIVEDATA;
					
					if(motionplus!=null&&mode==MODE_RECEIVEDATA)
						mode=MODE_NEUTRAL;
						//motionplus.resetAbsolutAngles();
					
					if(mode==MODE_CALIBRATE)
					{
						if(!calibrateAxis)
						{
							System.out.println("calibrate axis in 3 sec...");
							try {
								Thread.sleep(1000L);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println("calibrate axis in 2 sec...");
							try {
								Thread.sleep(1000L);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println("calibrate axis in 1 sec...");
							try {
								Thread.sleep(1000L);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
						
						WiiRemoteClient.calibrateAxis = true;
					}
						
						
				}

				NoPreviesButtonWasPressed=false;
			}
		};
		
		
		SimpleMoteFinder simpleMoteFinder;
		simpleMoteFinder= new SimpleMoteFinder();
		System.out.println("now press 1&2 on your WiiRemote");
		Mote mote = simpleMoteFinder.findMote();
		
		
		mote.setPlayerLeds(new boolean[] {false, false, false, true} );
		mote.addAccelerometerListener(listener);
		mote.setPlayerLeds(new boolean[] {false, false, true, true} );
		mote.addCoreButtonListener(buttonListener);
		
		mote.setPlayerLeds(new boolean[] {false, false, true, false} );
//		mote.addExtensionListener(extensionListener);
		mote.setPlayerLeds(new boolean[] {false, true, true, true} );
		
		mote.activateMotionPlus();
		mote.setPlayerLeds(new boolean[] {false, true, false , true} );
		
		
		//mote.setReportMode(ReportModeRequest.DATA_REPORT_0x31);
		mote.setPlayerLeds(new boolean[] {true, false, false, false} );
		mote.requestStatusInformation();
		String btAddress = mote.getBluetoothAddress();
		System.out.println("BluetoothAdress:"+btAddress);
		dcd= DeviceCalibrationData.loadDeviceCalibrationData(btAddress);
		System.out.println(dcd);
		
		
//		Thread.sleep(500L);
//        while (mote.getCalibrationDataReport() == null) {
//                System.out.println("waiting for calibration data report");
//                try {
//                        Thread.sleep(10l);
//                } catch (InterruptedException e) {
//                        e.printStackTrace();
//                }
//        }
//        CalibrationDataReport cali = mote.getCalibrationDataReport();
//        System.out.println(cali);
        
        System.out.println(mote.getStatusInformationReport());
        
		
		System.out.println("search for motion plus");
		while (!(mote.getExtension() instanceof MotionPlus))
			Thread.sleep(500L);

		 Extension extension = mote.getExtension();
		 if(extension instanceof MotionPlus){
			 motionplus = (MotionPlus) extension;
			 motionplus.newCalibration();
			 System.out.println("motion plus found");
			 motionplus.addMotionPlusEventListener(motionPlusListener);
			 mote.setReportMode(ReportModeRequest.DATA_REPORT_0x35);
		 }
			 
		 else
			 System.out.println("motion plus error");
		
		
		while(mode!=MODE_SHUTHDOWN)
			Thread.sleep(100l);
		

		System.out.println("shutting down");
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x30);
		mote.disconnect();
		
	}
	private static float median(Vector<Float> m) {
	    int middle = m.size()/2;  // subscript of middle element
	    if (m.size()%2 == 1) {
	        // Odd number of elements -- return the middle one.
	        return m.get(middle);
	    } else {
	       // Even number -- return average of middle two
	       // Must cast the numbers to double before dividing.
	       return (float) ((m.get(middle-1) + m.get(middle)) / 2.0);
	    }
	}
	
	
}
