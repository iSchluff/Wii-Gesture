package wiiGestureX;

import java.util.Vector;


/**
 * @author Anton Schubert
 * 
 * 
 */
public class GestureType {
	private String name;
	
	private HMM markovModel;
	private Quantizer quantizer;
	private ActionExecutor executor;
	
	public Integer states;
	public Integer observations;
	private double defaultProbability;
	
	public GestureType(String name){
		this.name=name;
		this.states=8; // n=8 Zahl der Zustände des HMM
		this.observations=14; // k=14 Zahl der Beobachtungen
		this.markovModel = new HMM(states, observations);
		this.quantizer = new Quantizer(states);
		this.executor=new ActionExecutor();
	}
	
	public double train(Vector<Gesture> trainingGestures){
		
		/* Vektoren der Gesten in einer einzelnen Geste zusammenfassen */
 		Gesture mainGesture=new Gesture();
		for(int i=0;i<trainingGestures.size();i++){
			Gesture gesture=trainingGestures.get(i);
			
			Vector<Vector<Double>> accelData=gesture.returnData();
			for(Vector<Double> vector:accelData){
				mainGesture.setAccelStep(vector);
			}
			
		}
		
		/* Quantisierer trainieren */
		quantizer.kMeans(mainGesture);
		
		/* Beobachtungssequenzen finden */
		Vector<int[]> seqs = new Vector<int[]>();
		for(int i=0; i<trainingGestures.size(); i++) {
			seqs.add(quantizer.bSequenz(trainingGestures.get(i)));
		}
		
		/* HMM mit Beobachtungssequenzen trainieren */
		markovModel.train(seqs);
		
		/* Modellwahrscheinlichkeit für die Bayessche Regel setzen */
		this.setDefaultProbability(trainingGestures);
		
		return observations;
	}
	
	/** 
	 * Gibt die Wahrscheinlichkeit des Modells zurück
	 */
	public double getDefaultProbability() {
		return this.defaultProbability;
	}
	
	/**
	 * Berechnet die Wahrscheinlichkeit des Models basierend auf der
	 * durchschnittlichen Wahrscheinlichkeit der Trainingsgesten.
	 * 
	 * TODO: try lowest or highest model probability as alternative
	 * 
	 * @param Vector der Trainingsgesten
	 */
	private void setDefaultProbability(Vector<Gesture> defsequence) {
		double prob=0;
		for(int i=0; i<defsequence.size(); i++) {
			prob+=this.matches(defsequence.elementAt(i));
		}
		
		this.defaultProbability=(prob)/defsequence.size();
	}
	
	public void setDefaultProbability(double defaultProbability){
		this.defaultProbability=defaultProbability;
	}
	
	
	/**
	 * Bestimmt die Wahrscheinlichkeit, mit der eine
	 * Geste dem Modell entspricht.
	 */
	public double matches(Gesture gesture) {
		int[] sequenz = quantizer.bSequenz(gesture);
		return this.markovModel.getProbability(sequenz);
	}

	public String getName() {
		return this.name;
	}
	
	public HMM getMarkovModel(){
		return markovModel;
	}
	
	public Quantizer getQuantizer(){
		return quantizer;
	}
	
	public ActionExecutor getExecutor(){
		return executor;
		
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setMarkovModel(HMM markovModel){
		this.markovModel=markovModel;
	}
	
	public void setQuantizer(Quantizer quantizer){
		this.quantizer=quantizer;
	}
	
	public void setExecutor(ActionExecutor executor){
		this.executor=executor;
	}
}
