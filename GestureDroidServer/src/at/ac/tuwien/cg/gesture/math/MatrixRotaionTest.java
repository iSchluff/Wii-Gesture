package at.ac.tuwien.cg.gesture.math;

import java.util.Random;

import at.ac.tuwien.cg.gesture.control.InputHandler;
import at.ac.tuwien.cg.gesture.data.MeasuringPoint;

public class MatrixRotaionTest extends Thread {
	boolean finished = false;
	Long speed = 10L;
	Vector3f rotation = new Vector3f(390.f, 12.f, 90.f);
	Matrix4f matrix = new Matrix4f();
	int x=0;
	int y=0;
	int z=0;
	/*
	 * this class simulate a back rotation from a matrix
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	
	public MatrixRotaionTest() {
	
		matrix=Matrix4f.rotate(matrix, rotation.x, new Vector3f(1.f,0.f,0.f));
		matrix=Matrix4f.rotate(matrix, rotation.y, new Vector3f(0.f,1.f,0.f));
		matrix=Matrix4f.rotate(matrix, rotation.z, new Vector3f(0.f,0.f,1.f));
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		Vector3f rotation = new Vector3f(0.f, 0.f, 0.f);
		Vector3f preRotation = new Vector3f(0.f, 0.f, 0.f);
		Vector3f preDist = new Vector3f(0.f, 0.f, 0.f);	// vorherige distanz
		Vector3f dist = new Vector3f(0.f, 0.f, 0.f);		// aktuelle distanz
		float preError = 1;
		
		Random random = new Random();
		
int i=0;
		
		while(!finished)
		{i++;
			
			Vector3f vx = new Vector3f(0.f, 0.f, 0.f);
			Vector3f vy = new Vector3f(0.f, 0.f, 0.f);
			Vector3f vz = new Vector3f(0.f, 0.f, 0.f);
			Matrix4f mr = new Matrix4f();
			
			
			Float xrand = preRotation.x+   (0.5f-random.nextFloat()*200*preError);//* ((2-(preDist.y+preDist.z))*90+0.1f)
			Float yrand = preRotation.y+   (0.5f-random.nextFloat()*200*preError);
			Float zrand = preRotation.z+   (0.5f-random.nextFloat()*200*preError);//
			rotation.set( xrand, yrand, zrand);
			
			System.out.println("----------------------------------------------------------------------------------------");
			

							// zuerst -z dann -y dann -x
							mr = Matrix4f.rotate(matrix, -rotation.z, new Vector3f(0.f,0.f,1.f));
							mr = Matrix4f.rotate(mr, -rotation.y, new Vector3f(0.f,1.f,0.f));
							mr = Matrix4f.rotate(mr, -rotation.x, new Vector3f(1.f,0.f,0.f));

							
							vx.set(1.f, 0.f, 0.f);
							vy.set(0.f, 1.f, 0.f);
							vz.set(0.f, 0.f, 1.f);

							Vector3f rx = mr.transform(vx);
							Vector3f ry = mr.transform(vy);
							Vector3f rz = mr.transform(vz);
							
							// Abstand:
							
							float distX = (vx.dot(rx)+1)/2;// -1 == 100% falsch , 1== 100% richtig
							float distY = (vy.dot(ry)+1)/2;
							float distZ = (vz.dot(rz)+1)/2;
							
							dist.set(distX,distY,distZ);
							
							float predistance = 1-(preDist.x+preDist.y+preDist.z)/3;// wie richtig stehen die achsen? 1== ganz richtig, 0 == ganz falsch
							float distance = 1-(dist.x+dist.y+dist.z)/3;// wie richtig stehen die achsen? 1== ganz richtig, 0 == ganz falsch
							
							
							if(predistance<distance){// schlechte wahl 
								rotation.set(preRotation);//=rotation;
								distance = predistance;
								
								System.out.println("schlechte wahl!!     pre="+preRotation);
							}
							else{
								// gute wahl
								preDist.set(dist);
								preError=distance;
								System.out.println("gute wahl!!");
							}
							System.out.println(predistance+"<"+distance);

							System.out.println("gegenrotation: "+rotation+"         dist="+dist);
							if( distance<0.01 ){
								rotation.set(-(float)x,-(float)y,-(float)z);
								System.out.println("found      i=("+i+")  "+ rotation);
								finished=true;
								x=y=z=360;
							}
							

							
							
//							try {
//								Thread.sleep(speed);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						// Visualize
							Vector3f zero = new Vector3f(0.f, 0.f, 0.f);
							long time =10L;
							
							MeasuringPoint mp = new MeasuringPoint(zero,matrix,time);
							mp.setMatrix(mr.getMatrix());
							
							InputHandler.receive(mp);

//						}
//					
//					}
//
//			}
		}
	}
}