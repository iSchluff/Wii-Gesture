package at.ac.tuwien.cg.gesture.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class DataInputHandler extends Thread
{

	private ServerSocket socket = null;
	ArrayList<Object> objectQueue;// queue for sending data to the phone
	private DiscoveryService ds;
	boolean running = true;

	public DataInputHandler(InputHandler ih)
	{

		// this.ih = ih;
		ds = new DiscoveryService();
		ds.start();
		
	}

	@Override
	public void run()
	{
		System.out.println("run @ init DataInputHandler listen@ 12345");
		while (this.running)
		{

			try
			{
				if(socket==null)
					try
					{
						socket = new ServerSocket(12345);
	
					} catch (SocketException e)
					{
						e.printStackTrace();
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				System.out.println("try to accept on port 12345");
				Socket s = socket.accept();
				System.out.println("socket.accept from:" + s.getInetAddress());
				SocketInputHandler sh = new SocketInputHandler(s);
				sh.start();

			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}

	}

	public class SocketInputHandler extends Thread
	{
		Socket s = null;
		ObjectInputStream ois = null;
		InputStream is;
		boolean running;

		public SocketInputHandler(Socket s)
		{
			this.s = s;
			System.out.println("socket input Handler");
			this.running=true;

		}

		@Override
		public void run()
		{
			while (this.running)
			{
				try
				{
					is = s.getInputStream();
				
					ois = new ObjectInputStream(is);
					if (ois == null)
						s.close();
					

					InputHandler.receive(ois.readObject());

				} catch (Exception e)
				{
					System.err.println("die Verbindung wurde getrennt");
					e.printStackTrace();
					this.running=false;
					try
					{
						
						//s.close();
						is.close();
						ois.close();
						//ois=null;
						//s=null;
					} catch (IOException e1)
					{
						System.err.println("ois.close() exception");
					}
				}

			}
		}

	}

	public class SocketOutputHandler extends Thread
	{
		Socket s;
		ObjectOutputStream oos;

		public SocketOutputHandler(Socket s)
		{
			this.s = s;
		}

		@Override
		public void run()
		{
			while (s.isBound())
			{

				try
				{
					if (oos == null)
						oos = new ObjectOutputStream(s.getOutputStream());

					// if data exist write and flush
					// oos.writeObject(obj)
					// oos.flush();

				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		}
	}

	public class DiscoveryService extends Thread
	{

		@Override
		public void run()
		{

			while (true)
			{
				DatagramSocket datagramSocket;
				try
				{
					datagramSocket = new DatagramSocket(12321);
					DatagramPacket p = null;

					byte[] buffer = new byte[256];
					DatagramPacket packet = new DatagramPacket(buffer,
							buffer.length);
					System.out.println("listen on 12321 UDP");
					datagramSocket.receive(packet);
					System.out.println("discovery packet received");

					datagramSocket.getInetAddress();

					String ServerIp = new String(packet.getData(), 0, packet
							.getLength());

					datagramSocket.close();
					// now send beck to the mobile

					Socket ss = new Socket(ServerIp, 12321);
					ss.setSoTimeout(2000);

					PrintWriter out = new PrintWriter(ss.getOutputStream(),
							true);
					out.print("ok");
					out.close();

					ss.close();

					// 

				} catch (SocketException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}
}
