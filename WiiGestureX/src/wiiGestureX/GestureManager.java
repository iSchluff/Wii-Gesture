package wiiGestureX;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

import wiiGestureX.gui.GestureList;
import wiiGestureX.gui.MainGui;
import wiiGestureX.util.IO;
import wiiGestureX.util.Log;

/**
 * @author Anton Schubert
 *
 * Verwaltet Gestentypen sowie ihre Files
 */
public class GestureManager {
	private String workingDir="";
	
	private Vector<GestureType> types;
	//private double lastprob;
	
	public GestureManager(String gestureFolderPath){
		this.types=new Vector<GestureType>();
		//this.lastprob=0.0;
		
		workingDir=gestureFolderPath;
		
		File dir = new File(workingDir);
		
		if(dir.exists()){
			// Filters all files that have the suffix .ges
			FilenameFilter filter = new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.toLowerCase().endsWith(".dat");
			    }
			};
			
			String[] children = dir.list(filter);
	
			for (int i=0; i<children.length; i++) {
			    String filename = children[i].toLowerCase();
			    String name=filename.replaceAll(".dat","");
			    Log.write(Log.DEBUG,"Loading: "+name,this);
			    GestureType type=new GestureType(name);
			    
			    try {
					type=IO.readFromFile(workingDir+"/"+filename);
				} catch (IOException e) {
					Log.write("Fehler beim Lesen von: "+filename);
					e.printStackTrace();
				}
				
			    type.setName(name);
			    types.add(type);
			}
		}else{
			dir.mkdir();
		}

	}
	
	/**
	 * Fügt einen neuen Gestentyp hinzu
	 * @throws IOException 
	 */
	public void addGestureType(GestureType type) {
		types.add(type);
		Log.write("Writing: "+type.getName()+".dat");
		try {
			IO.writeToFile(workingDir, type);
		} catch (IOException e) {
			Log.write("Fehler beim Schreiben von: "+type.getName());
			e.printStackTrace();
		}
		MainGui.gesturePanel.remove(MainGui.gestureList);
		MainGui.gestureList=new GestureList();
		MainGui.gesturePanel.setViewportView(MainGui.gestureList);
		MainGui.gestureList.setSelectedIndex(types.indexOf(type));
	}

	/**
	 * Gibt den Gestentyp an dieser Position zurück
	 */
	public GestureType getGestureType(int index) {
		return types.get(index);
	}
	
	/**
	 * Gibt die Namen aller Gestentypen zurück
	 */
	public String[] getGestureNames(){
		String[] names=new String[types.size()];
		for(int i=0;i<types.size();i++){
			names[i]=types.get(i).getName();
		}
		return names;
	}
	
	/**
	 * Gibt den Gestentyp des entsprechenden Namens zurück
	 */
	public GestureType getTypeByName(String name){
		for(int i=0;i<types.size();i++){
			GestureType type=types.get(i);
			if(type.getName().equals(name)){
				return type;
			}
		}
		return null;
	}
	
	/**
	 * Löscht einen Gestentyp
	 */
	public void deleteGestureType(int index){
		GestureType gestureType=getGestureType(index);
		File file=new File(workingDir+"/"+ gestureType.getName()+".dat");
		Log.write("Deleting: "+gestureType.getName()+".dat", this);
		file.delete();
		//MainGui.gestureList.setSelectedIndex(0);
		Log.write("Removing "+gestureType.getName()+" from types", this);
		this.types.remove(index);
		
		MainGui.gesturePanel.remove(MainGui.gestureList);
		MainGui.gestureList=new GestureList();
		MainGui.gesturePanel.setViewportView(MainGui.gestureList);
		MainGui.gestureList.setSelectedIndex(0);
	}
	
	/**
	 * Schreibt alle geänderten Gestentypen in ihre entsprechenden Files.
	 */
	public void writeAllChanges(){
		for(GestureType type: types){
			if(type.getExecutor().changed){
				Log.write("Writing "+type.getName()+" to File", this);
				try {
					IO.writeToFile(workingDir, type);
				} catch (IOException e) {
					Log.write("Fehler beim Schreiben von: "+type.getName());
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Ordnet eine Geste einem Gestentyp zu.
	 */
	public int classify(Gesture g) {
		Log.write("Gestenerkenung...");
		
		/* Wert im Nenner berechnen, nach Bayes */
		double sum = 0;
		for(int i=0; i<types.size(); i++) {
			sum+=types.get(i).getDefaultProbability()*types.get(i).matches(g);
		}
		
		int recognized = -1; // erkannter Gestentyp
		double recogProb = Integer.MIN_VALUE; // Wahrscheinlichkeit der gefundenen Geste
		double foundGestureProb = 0; // Wahrscheinlichkeit der Geste unter Annahme des Modells
		double foundModelProb = 0; // Wahrscheinlichkeit des Modells
		
		for(int i=0; i<this.types.size(); i++) {
			double gestureProb = types.get(i).matches(g);
			double modelProb = types.get(i).getDefaultProbability();
			
			/* Beobachtungswahrscheinlichkeit im derzeitigen Modell */
			double prob=(modelProb*gestureProb)/sum;
			
			/* Vergleich */
			if(prob>recogProb) {
				foundGestureProb=gestureProb;
				foundModelProb=modelProb;
				recogProb=prob;
				recognized=i;
			}
		}
		
		// Geste konnte klassifiziert werden
		if(recogProb>0 && foundModelProb>0 && foundGestureProb>0 && sum>0) {
			//this.lastprob=recogProb;
			return recognized;
		} else {
			// Geste konnte nicht klassifiziert werden
			return -1;
		}
	}
}
