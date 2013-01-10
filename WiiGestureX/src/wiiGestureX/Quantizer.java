package wiiGestureX;

import java.util.Collections;
import java.util.Vector;

import wiiGestureX.util.Log;

/**
 * @author Anton Schubert
 * 
 * bestimmt die Clusterzentren eines Gestentyps mithilfe von K-Means
 */
public class Quantizer {
	public double[][] map;
	
	@SuppressWarnings("unused")
	private int states;

	/**
	 * @param states Zahl der Zustände des HMM
	 */
	public Quantizer(Integer states) {
		//Log.write(Log.DEBUG,"Creating Quantizer",this);
		this.states=states;
		this.map=new double[14][3];
	}
	
	/**
	 * Initialisiert die Zentren
	 * @param gesture Geste zur Bestimmung des Radius
	 */
	private void initMap(Gesture gesture){
		//Log.write(Log.DEBUG,"Initializing map",this);
		Vector<Vector<Double>> accelData=gesture.returnData();
		double total=0;
		for(int i=0;i<accelData.size();i++){
			for(int j=0;j<accelData.get(0).size();j++){
				total+=Math.abs(accelData.get(i).get(j));
			}
			
		}
		double radius=(total/accelData.size())/3;
		
		double pi=Math.PI;
		
		this.map[0] = new double[] { radius, 0.0, 0.0 };
		this.map[1] = new double[] { Math.cos(pi / 4) * radius, 0.0,
				Math.sin(pi / 4) * radius };
		this.map[2] = new double[] { 0.0, 0.0, radius };
		this.map[3] = new double[] { Math.cos(pi * 3 / 4) * radius,
				0.0, Math.sin(pi * 3 / 4) * radius };
		this.map[4] = new double[] { -radius, 0.0, 0.0 };
		this.map[5] = new double[] { Math.cos(pi * 5 / 4) * radius,
				0.0, Math.sin(pi * 5 / 4) * radius };
		this.map[6] = new double[] { 0.0, 0.0, -radius };
		this.map[7] = new double[] { Math.cos(pi * 7 / 4) * radius,
				0.0, Math.sin(pi * 7 / 4) * radius };

		this.map[8] = new double[] { 0.0, radius, 0.0 };
		this.map[9] = new double[] { 0.0, Math.cos(pi / 4) * radius,
				Math.sin(pi / 4) * radius };
		this.map[10] = new double[] { 0.0,
				Math.cos(pi * 3 / 4) * radius,
				Math.sin(pi * 3 / 4) * radius };
		this.map[11] = new double[] { 0.0, -radius, 0.0 };
		this.map[12] = new double[] { 0.0,
				Math.cos(pi * 5 / 4) * radius,
				Math.sin(pi * 5 / 4) * radius };
		this.map[13] = new double[] { 0.0,
				Math.cos(pi * 7 / 4) * radius,
				Math.sin(pi * 7 / 4) * radius };
		//Log.write(Log.DEBUG,"Zentren initialisiert!",this);
	}
	
	
	/**
	 * Finden der Cluster-Zentren
	 * @param gesture zur Bestimmung genutzte Vektoren
	 */
	public void kMeans(Gesture gesture){
		initMap(gesture);
		this.printMap();
		Vector<Vector<Double>> accelData=gesture.returnData();
		
		double groups[][]=new double[map.length][accelData.size()];
		double groupsOld[][]=new double[map.length][accelData.size()];
		
		int counter=0;
		
		do{
			groupsOld=copy(groups);
			groups=findGroups(accelData);
			
			for(int i=0;i<groups.length;i++){
				double xTotal=0;
				double yTotal=0;
				double zTotal=0;
				int teiler=0;
				for(int j=0;j<groups[0].length;j++){
					if(groups[i][j]==1){
						Vector<Double> vector=accelData.get(j);
						xTotal+=vector.get(0);
						yTotal+=vector.get(1);
						zTotal+=vector.get(2);
						teiler++;
					}
				}
				if(teiler>1){
					map[i][0]=xTotal/(double)teiler;
					map[i][1]=yTotal/(double)teiler;
					map[i][2]=zTotal/(double)teiler;
				}
			}
			//print();
			counter++;
			 
		}while(!equal(groups,groupsOld) && counter<1000);
		//Log.write(Log.DEBUG,"Zentren gefunden!",this);
		//Log.write(Log.DEBUG,"Map", map);
		this.printMap();
	}
	
	/**
	 * @param accelData Ordnet jeden Vektor der Geste einer Gruppe zu
	 */
	public double[][] findGroups(Vector<Vector<Double>> accelData){
		double[][] groups=new double[map.length][accelData.size()];
		for(int i=0;i<accelData.size();i++){
			Vector<Double> distances=new Vector<Double>();
			for(int j=0;j<map.length;j++){
				Vector<Double> vector=accelData.get(i);
				double[] zentrum=map[j];
				double x=Math.pow(vector.get(0)-zentrum[0], 2);
				double y=Math.pow(vector.get(1)-zentrum[1], 2);
				double z=Math.pow(vector.get(2)-zentrum[2], 2);
				distances.add(Math.sqrt(x+y+z));
			}
			int group=distances.indexOf(Collections.min(distances));
			groups[group][i]=1;
		}
		return groups;
	}
	
	/**
	 * Ermittelt die Beochbachtungs-Sequenz einer Geste.
	 * D.h. eine Folge von Zahlen, die angibt in welche Gruppen die 
	 * jeweiligen Vektoren der Geste eingeordnet werden.
	 */
	public int[] bSequenz(Gesture gesture){
		Vector<Vector<Double>> accelData=gesture.returnData();
		double[][] groups=findGroups(accelData);
		
		int[] sequenz=new int[groups[0].length];
		
		for(int j=0;j<groups[0].length;j++){ //um die zeitliche abfolge zu wahren
			for(int i=0;i<groups.length;i++){
				if(groups[i][j]==1){
					sequenz[j]=i;
					break;
				}
			}
		}
		
		String s="";
		for(int n:sequenz){
			s+=String.valueOf(n)+" ";
		}
		Log.write(Log.DEBUG,s,sequenz);
		
		return sequenz;
	}

	/**
	 * Vergleicht zweidimensionale Arrays gleicher Größe
	 */
	private boolean equal(double[][] groups1, double[][] groups2) {
		for(int i=0;i<groups1.length;i++){
			for(int j=0;j<groups1[0].length;j++){
				if(groups1[i][j]!=groups2[i][j]){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Erstellt eine Kopie eines zweidimensionalen Arrays
	 */
	private double[][] copy(double[][] groups){
		double[][] groupsNew=new double[groups.length][groups[0].length];
		for(int i=0;i<groups.length;i++){
			for(int j=0;j<groups.length;j++){
				groupsNew[i][j]=groups[i][j];
			}
		}
		return groupsNew;
		
	}
	
	public void printMap() {
		Log.write(Log.DEBUG, "Centeroids:", this);
		for (int i = 0; i < this.map.length; i++) {
			Log.write(Log.DEBUG, i + ". :" + this.map[i][0] + ":"
					+ this.map[i][1] + ":" + this.map[i][2], this);
		}
	}

}
