package com.aidancbrady.peerchess.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import com.aidancbrady.peerchess.PeerChess;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.client.Constants;
import com.aidancbrady.peerchess.gui.JoinPanel;

public class GameScanner extends Thread
{
	private DatagramSocket socket;
	
	private JoinPanel panel;
	
	public GameScanner(JoinPanel p)
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
			packet.setPort(PeerChess.instance().getPort());
			
			socket.send(packet);
			socket.setBroadcast(false);
			
			PeerUtils.debug("Datagram broadcast complete");
			
			while(true)
			{
				try {
					DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
					socket.setSoTimeout(Constants.MAX_PING);
					socket.receive(response);
					
					String s = new String(response.getData());
					
					if(s.startsWith("PING"))
					{
					    panel.addServer(new Server(s.split(":")[1].trim(), response.getAddress().getHostAddress()));
						PeerUtils.debug("Received datagram response from " + response.getAddress().getHostAddress());
					}
				} catch(SocketTimeoutException e) {
					break;
				} catch(Exception e) {
				    e.printStackTrace();
				}
			}
			
			socket.close();
		} catch(Exception e) {
			e.printStackTrace();
			socket.close();
		}
		
		doUpdate();
	}
	
	public void doUpdate()
	{
		if(panel.isVisible())
		{
		    panel.endRefresh();
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
