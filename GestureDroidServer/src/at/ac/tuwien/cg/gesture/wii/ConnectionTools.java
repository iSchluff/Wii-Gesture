package at.ac.tuwien.cg.gesture.wii;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import at.ac.tuwien.cg.gesture.math.Matrix4f;
import at.ac.tuwien.cg.gesture.math.Quaternion;
import at.ac.tuwien.cg.gesture.math.Vector3f;

public class ConnectionTools
{

	public static String getCurrentEnvironmentNetworkIp() {
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
		}

		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				InetAddress addr = address.nextElement();
				if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()
						&& !(addr.getHostAddress().indexOf(":") > -1)) {
					return addr.getHostAddress();
				}
			}
		}
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "127.0.0.1";
		}
	}

	/**
	 * this method send an descovery UDP Package and when the server run in the same ip subnet the server response with an ok
	 * @return the Server IP Adress
	 */
	public static String discoverServerIP() {
		String myIP = getCurrentEnvironmentNetworkIp();
		Log.d("myIP",myIP);
		String[] preIP = myIP.split("\\.");

		// now broadcast the descovery Package

		DatagramSocket datagramSocket;
		try {
		        datagramSocket = new DatagramSocket();
		        InetAddress receiverAddress = InetAddress.getByName(preIP[0] + "."	+ preIP[1] + "." + preIP[2] + "." + 255);
		byte[] buffer = myIP.getBytes();
		

		DatagramPacket packet = new DatagramPacket(
		        buffer, buffer.length, receiverAddress, 12321);
		datagramSocket.send(packet);
		
         // datagramSocket.receive(pack);
		Log.d("discoverServerIP", "send broadcast Package to:"+receiverAddress+" port:12321");
		        
		        
		        
		} catch (Exception e) {
			Log.e("Exception discoverServerIP",e.toString());  
		}
		
		// now hopfully the server will respond!
		
		try {
			ServerSocket ss = new ServerSocket(12321);
			ss.setSoTimeout(2000);
			Socket message = ss.accept();
			String ServerIp = message.getInetAddress().toString();
			ss.close();
			if(ServerIp!=null)
			return ServerIp.substring(1, ServerIp.length());
		} catch (IOException e) {
			Log.e("Exception discoverServerIP",e.toString());  
		}

		return "0.0.0.0";
	}



}
