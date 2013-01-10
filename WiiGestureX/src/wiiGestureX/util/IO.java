package wiiGestureX.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import wiiGestureX.ActionExecutor;
import wiiGestureX.GestureType;
import wiiGestureX.HMM;
import wiiGestureX.Quantizer;

/**
 * @author Anton Schubert
 *
 * File I/O von Gestentypen
 */
public class IO {
	/**
	 * Write a GestureType to a file
	 * @param path
	 * @param type
	 * @throws IOException
	 */
	public static void writeToFile(String path,GestureType type) throws IOException{
		  //File file = new File(path);
		  //file.createNewFile();
		
		  FileWriter writer = new FileWriter(path+"/"+type.getName()+".dat");
		  BufferedWriter out = new BufferedWriter(writer);
		  
		  Log.write(Log.DEBUG,"IO: Beginning to write to File",null);
		  /* normale Variablen */
		  out.write("#STATES");
		  out.newLine();
		  out.write(String.valueOf(type.states));
		  out.newLine();
		  out.write("#OBSERVATIONS");
		  out.newLine();
		  out.write(String.valueOf(type.observations));
		  out.newLine();
		  out.write("#DEFAULTPROBABILITY");
		  out.newLine();
		  out.write(String.valueOf(type.getDefaultProbability()));
		  out.newLine();
		  
		  /* Quantisierer */
		  Quantizer quantizer=type.getQuantizer();
		  out.write("#QUANTIZER");
		  out.newLine();
		  out.write("$map");
		  out.newLine();
		  writeArr(quantizer.map,out);
		  
		  /* Hidden Markov Model */
		  HMM markovModel=type.getMarkovModel();
		  out.write("#HMM");
		  out.newLine();
		  out.write("$pi");
		  out.newLine();
		  for(int i=0;i<markovModel.pi.length;i++){
			  out.write(String.valueOf(markovModel.pi[i])+"\t");
		  }
		  out.newLine();
		  out.write("$a");
		  out.newLine();
		  writeArr(markovModel.a,out);
		  out.write("$b");
		  out.newLine();
		  writeArr(markovModel.b,out);
		  
		  /* ActionExecutor */
		  ActionExecutor executor=type.getExecutor();
		  out.write("#EXECUTOR");
		  out.newLine();
		  out.write("$mode");
		  out.newLine();
		  out.write(String.valueOf(executor.getMode()));
		  out.newLine();
		  out.write("$key");
		  out.newLine();
		  out.write(String.valueOf(executor.getKey()));
		  out.newLine();
		  out.write("$modifiers");
		  out.newLine();
		  out.write(String.valueOf(executor.getModifiers()));
		  out.newLine();
		  out.write("$command");
		  out.newLine();
		  out.write(executor.getCommand());
		  out.newLine();
		  out.write("#END");
		  
		  out.close();
		  Log.write(Log.DEBUG,"IO: Finished writing to File",null);
	}
	
	/**
	 * Load a GestureType from a file
	 * @param path
	 * @throws IOException
	 */
	public static GestureType readFromFile(String path) throws IOException{
		FileReader reader=new FileReader(path);
		BufferedReader in=new BufferedReader(reader);
		
		GestureType type=new GestureType("temp");
		
		/* normale Variablen */
		in.readLine();
		type.states=Integer.valueOf(in.readLine());
		in.readLine();
		type.observations=Integer.valueOf(in.readLine());
		in.readLine();
		type.setDefaultProbability(Double.valueOf(in.readLine()));
		
		/* Quantisierer */
		Quantizer quantizer=new Quantizer(type.states);
		nl(2,in);
		quantizer.map=readArr(in);
		
		/* Hidden Markov Model */
		HMM markovModel=new HMM(type.states, type.observations);
		in.readLine();
		String[] piStr=in.readLine().split("\t");
		double[] pi=new double[piStr.length];
		for(int i=0;i<piStr.length;i++)
			pi[i]=Double.valueOf(piStr[i]);
		markovModel.pi=pi;
		in.readLine();
		markovModel.a=readArr(in);
		markovModel.b=readArr(in);
		
		/* Executor */
		ActionExecutor executor=new ActionExecutor();
		nl(1,in);
		executor.setMode(Integer.valueOf(in.readLine()));
		in.readLine();
		int key=Integer.valueOf(in.readLine());
		in.readLine();
		int modifiers=Integer.valueOf(in.readLine());
		executor.setKeyAction(key, modifiers);
		in.readLine();
		executor.setCommand(in.readLine());
		in.close();
		
		type.setQuantizer(quantizer);
		type.setMarkovModel(markovModel);
		executor.changed=false;
		type.setExecutor(executor);
		
		return type;
	}
	
	/**
	 * Generiert n newlines
	 * (Überspringt n Zeilen)
	 */
	private static void nl(int n,BufferedReader in) throws IOException{
		for(int i=0;i<n;i++){
			in.readLine();
		}
	}
	
	private static void writeArr(double[][] arr,BufferedWriter out) throws IOException{
		  for(int i=0;i<arr.length;i++){
			  for(int j=0;j<arr[0].length;j++){
				  out.write(String.valueOf(arr[i][j])+"\t");
			  }
			  out.newLine();
		  }
	}
	
	private static double[][] readArr(BufferedReader in) throws IOException{
		Vector<double[]> vector=new Vector<double[]>();
		String zeile=in.readLine();
		
		while(!zeile.startsWith("$")&& !zeile.startsWith("#")){
			String[] spaltenStr=zeile.split("\t");
			double[] spalten=new double[spaltenStr.length];
			/* Werte in Zahlen umwandeln */
			for(int i=0;i<spaltenStr.length;i++){
				spalten[i]=Double.parseDouble(spaltenStr[i]);                                         
			}
			vector.add(spalten);
			zeile=in.readLine();
		}
		
		/* Zeilen in Array überführen */
		double[][] arr=new double[vector.size()][vector.get(0).length];
		for(int i=0;i<vector.size();i++){
			arr[i]=vector.get(i);
		}
		return arr;
	}
}
