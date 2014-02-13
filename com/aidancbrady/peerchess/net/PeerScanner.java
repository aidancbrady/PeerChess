package com.aidancbrady.peerchess.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import com.aidancbrady.peerchess.JoinPanel;
import com.aidancbrady.peerchess.PeerChess;

public class PeerScanner extends Thread
{
	public static final int MAX_PING = 2000;
	
	public List<Server> pingedServers = new ArrayList<Server>();
	
	public boolean valid = true;
	
	public DatagramSocket socket;
	
	public JoinPanel panel;
	
	public PeerScanner(JoinPanel p)
	{
		panel = p;
		setDaemon(true);
	}
	
	@Override
	public void run()
	{
		try {
			socket = new DatagramSocket();
			socket.setBroadcast(true);
			
			byte[] b = new String("PING:" + PeerChess.instance().username).getBytes();
			DatagramPacket packet = new DatagramPacket(b, b.length);
			packet.setAddress(InetAddress.getByAddress(new byte[] {(byte)255, (byte)255, (byte)255, (byte)255}));
			packet.setPort(PeerChess.instance().port);
			
			socket.send(packet);
			socket.setBroadcast(false);
			
			while(true)
			{
				try {
					DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
					socket.setSoTimeout(MAX_PING);
					socket.receive(response);
					
					String s = new String(response.getData());
					pingedServers.add(new Server(s.split(":")[1], response.getAddress().getHostAddress()));
				} catch(SocketTimeoutException e) {
					break;
				} catch(Exception e) {}
			}
			
			doUpdate();
			
			socket.close();
		} catch(Exception e) {
			e.printStackTrace();
			socket.close();
		}
	}
	
	public void doUpdate()
	{
		if(panel.isVisible())
		{
			panel.populateServers(pingedServers);
		}
	}
	
	public static class Server
	{
		public String username;
		public String ip;
		
		public Server(String s, String s1)
		{
			username = s;
			ip = s1;
		}
	}
}
