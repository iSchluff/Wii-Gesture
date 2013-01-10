package wiiGestureX;

import java.text.DecimalFormat;
import java.util.Vector;

import wiiGestureX.util.Log;


/**
 * @author Anton Schubert
 * 
 * Implementierung des Forward-, Backward-, sowie Baum-Welch-Algorithmus
 */
public class HMM {
	
	public int states; //Zahl der versteckten Zustände
	public int observations; //Zahl der Beobachtungen
	public double[] pi; //Menge der Anfangswahrscheinlichkeiten für die Zustände Pi
	public double[][] a; //Matrix der Übergangswahrscheinlichkeiten A
	public double[][] b; //Matrix der Emissionswahrscheinlichkeiten B

	public HMM(Integer states, Integer observations) {
		//Log.write(Log.DEBUG,"Creating HMM",this);
		this.states=states;
		this.observations=observations;
		this.pi=new double[states];
		this.a=new double[states][states];
		this.b=new double[states][observations];
		
		init();
		//print();
	}
	
	/**
	 * Initialisiert Hidden Markov Model in der Left-To-Right version
	 */
	private void init() {
		//Log.write(Log.DEBUG,"Initializing HMM",this);
		int jumplimit = 2;
		
		// set startup probability
		pi[0] = 1;
		for(int i=1; i<states; i++) {
			pi[i] = 0;
		}
		
		for(int i=0; i<states; i++) {
			for(int j=0; j<states; j++) {
				if(i==states-1 && j==states-1) { // last row
					a[i][j] = 1.0;
				} else if(i==states-2 && j==states-2) { // next to last row
					a[i][j] = 0.5;
				} else if(i==states-2 && j==states-1) { // next to last row
					a[i][j] = 0.5;
				} else if(i<=j && i>j-jumplimit-1) {
					a[i][j] = 1.0/(jumplimit+1);
				} else {
					a[i][j] = 0.0;
				}
			}
		}
		
		// emission probability
		for(int i=0; i<states; i++) {
			for(int j=0; j<observations; j++) {
				b[i][j] = 1.0/(double)observations;
			}
		}
	}
	
	/**
	 * speziell optimierter Baum-Welch Algorithmus
	 * 
	 * Quelle:
	 * 
	 * "Gesture Recognition with a Wii Controller"
	 * http://www.wiigee.org/download_files/gesture_recognition_with_a_wii_controller-schloemer_poppinga_henze_boll.pdf
	 * 
	 * Vesa-Matti Mäntylä - "Discrete Hidden Markov Models with
	 * application to isolated user-dependent hand gesture recognition"
	 * 
	 * http://www.vtt.fi/inf/pdf/publications/2001/P449.pdf
	 * 
	 */
	public void train(Vector<int[]> trainingsSequenz) {

		double[][] a_new = new double[a.length][a.length];
		double[][] b_new = new double[b.length][b[0].length];
		// re calculate state change probability a
		for(int i=0; i<a.length; i++) {
			for(int j=0; j<a[i].length; j++) {	
				double zaehler=0;
				double nenner=0;
			
				for(int k=0; k<trainingsSequenz.size(); k++) {
					int[] sequenz = trainingsSequenz.elementAt(k);
					
					double[][] fwd = this.forwardAlg(sequenz);
					double[][] bwd = this.backwardAlg(sequenz);
					double prob = this.getProbability(sequenz);
		
					double zaehler_innersum=0;
					double nenner_innersum=0;
					
					
					for(int t=0; t<sequenz.length-1; t++) {
						zaehler_innersum+=fwd[i][t]*a[i][j]*b[j][sequenz[t+1]]*bwd[j][t+1];
						nenner_innersum+=fwd[i][t]*bwd[i][t];
					}
					zaehler+=(1/prob)*zaehler_innersum;
					nenner+=(1/prob)*nenner_innersum;
				} // k
		
				a_new[i][j] = zaehler/nenner;
			} // j
		} // i
		
		// re calculate emission probability b
		for(int i=0; i<b.length; i++) { // zustaende
			for(int j=0; j<b[i].length; j++) {	// symbole
				double zaehler=0;
				double nenner=0;
			
				for(int k=0; k<trainingsSequenz.size(); k++) {
					int[] sequenz = trainingsSequenz.elementAt(k);
					
					double[][] fwd = this.forwardAlg(sequenz);
					double[][] bwd = this.backwardAlg(sequenz);
					double prob = this.getProbability(sequenz);
		
					double zaehler_innersum=0;
					double nenner_innersum=0;
					
					
					for(int t=0; t<sequenz.length-1; t++) {
						if(sequenz[t]==j) {
							zaehler_innersum+=fwd[i][t]*bwd[i][t];
						}
						nenner_innersum+=fwd[i][t]*bwd[i][t];
					}
					zaehler+=(1/prob)*zaehler_innersum;
					nenner+=(1/prob)*nenner_innersum;
				} // k
		
				b_new[i][j] = zaehler/nenner;
			} // j
		} // i
	
		this.a=a_new;
		this.b=b_new;
	}
	
	/** 
	 * Berechnet die Wahrscheinlichkeit P(O,A), dass eine
	 * Beobachtungssequenz zu diesem Modell gehört.
	 * (mithilfe des Forward-Algorithmus)
	 * 
	 * @param o Beobachtungssequenz
	 */
	public double getProbability(int[] o) {
		double prob = 0.0;
		double[][] forward = this.forwardAlg(o);
		//	add probabilities
		for (int i = 0; i < forward.length; i++) {
			prob += forward[i][forward[i].length - 1];
		}
		return prob;
	}
	
	/**
	 * Forward Algorithmus
	 * 
	 * Berechnet die Wahrscheinlichkeit, zum Zeitpunkt
	 * t die Beobachtung ot gemacht zu haben und im 
	 * Zustand si zu sein.
	 * 
	 * @param o Beobachtungssequenz
	 * @return Array[Zustand][Zeit] 
	 */
	private double[][] forwardAlg(int[] o) {
		double[][] f = new double[states][o.length];
		
		/* Initialisierung */
		for (int l = 0; l < f.length; l++) {
			f[l][0] = pi[l] * b[l][o[0]];
		}
		
		/* Rekursion */
		for (int i = 1; i < o.length; i++) {
			for (int k = 0; k < f.length; k++) {
				double sum = 0;
				for (int l = 0; l < states; l++) {
					sum += f[l][i-1] * a[l][k];
				}
				f[k][i] = sum * b[k][o[i]];
			}
		}
		return f;
	}
	
	/**
	 * Backward algorithmus
	 * 
	 * @param o Beobachtungs-sequenz
	 * @return Array[Zustand][Zeit]
	 */
	private double[][] backwardAlg(int[] o) {
		int T = o.length;
		double[][] bwd = new double[states][T];
		
		/* Initialisierung */
		for (int i = 0; i < states; i++)
			bwd[i][T - 1] = 1;
		
		/* Rekursion */
		for (int t = T - 2; t >= 0; t--) {
			for (int i = 0; i < states; i++) {
				bwd[i][t] = 0;
				for (int j = 0; j < states; j++)
					bwd[i][t] += (bwd[j][t + 1] * a[i][j] * b[j][o[t + 1]]);
			}
		}
		return bwd;
	}
	
	/** 
	 * Ausgabe
	 */
	public void print() {
		DecimalFormat fmt = new DecimalFormat();
		fmt.setMinimumFractionDigits(5);
		fmt.setMaximumFractionDigits(5);
		for (int i = 0; i < states; i++)
			Log.write(Log.DEBUG, "pi(" + i + ") = " + fmt.format(pi[i]), this);
		Log.write(Log.DEBUG, "", this);
		for (int i = 0; i < states; i++) {
			for (int j = 0; j < states; j++)
				Log.write(Log.DEBUG, "a(" + i + "," + j + ") = "
						+ fmt.format(a[i][j]) + " ", this);
			Log.write(Log.DEBUG, "", this);
		}
		Log.write("");
		for (int i = 0; i < states; i++) {
			for (int k = 0; k < observations; k++)
				Log.write(Log.DEBUG, "b(" + i + "," + k + ") = "
						+ fmt.format(b[i][k]) + " ", this);
			Log.write(Log.DEBUG, "", this);
		}
	}

}
