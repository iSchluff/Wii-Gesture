package at.ac.tuwien.cg.gesture.wii;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;


public class ConnectionHandler extends Thread
{
	private static boolean running;
	Socket socket = null;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	ArrayList<Object> objectQueue;
	
	public ConnectionHandler()
	{
		running = true;
		socket = null;
		objectQueue = new ArrayList<Object>();
	}

	public void setRunning(boolean run){
		this.running=run;
	}
	
	
	public synchronized Object getObject(){
		return objectQueue.get(0);
	}
	public synchronized void removeObject(){
		if(objectQueue.size()>0)
		objectQueue.remove(0);
	}
	
	@Override
	public void run()
	{
		while(ConnectionHandler.running){
			if(objectQueue.size()>0){

				// if successfully sent remove it from queue
				 if( sendData(getObject(), Config.serverIpAdress, Config.serverPort))
					 removeObject();
				 
			}
			else{
				try
				{
					Thread.sleep(5);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

	public void addToQueue(Object data)
	{
		objectQueue.add(data);
	}

	public boolean sendData(Object data, String IpAdress, Integer serverport)
	{

		try
		{

			if (socket == null)
				socket = new Socket(InetAddress.getByName(IpAdress), serverport);
			
			
			oos = new ObjectOutputStream(socket.getOutputStream());

			Log.d("try to send to", InetAddress.getByName(IpAdress).toString()
					+ " " + serverport);

			oos.writeObject(data);
			//oos.writeObject(new String("test"));
			oos.flush();

			Log.d("flush done: sendData", data.toString());
			return true;

		} catch (Exception e)
		{
			
			//e.printStackTrace();
			try
			{
//				if(oos=!null)
//				oos.close();
//				ois.close();
				socket.close();

			} catch (Exception e1)
			{
			}
			socket = null;
			oos = null;
			ois = null;

		}
		return false;

	}

}
