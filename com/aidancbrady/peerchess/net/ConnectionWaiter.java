package com.aidancbrady.peerchess.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.aidancbrady.peerchess.ChessPanel;
import com.aidancbrady.peerchess.PeerChess;

public class ConnectionWaiter extends Thread
{
	public ChessPanel panel;
	
	public PingResponder responseThread;
	
	public ServerSocket serverSocket;
	
	public ConnectionWaiter(ChessPanel p)
	{
		panel = p;
		
		start();
	}
	
	@Override
	public void run()
	{
		try {
			(responseThread = new PingResponder()).start();
			serverSocket = new ServerSocket(PeerChess.instance().port);
			
			Socket connection = serverSocket.accept();
			
			if(connection != null)
			{
				(panel.connection = new PeerConnection(connection, panel)).start();
				panel.connection.write("UPDATE");
				
				panel.frame.waiting.setVisible(false);
			}
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(panel, "Error accepting connection.");
			panel.frame.forceMenu();
		}
		
		try {
			serverSocket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public class PingResponder extends Thread
	{
		public DatagramSocket socket;
		
		@Override
		public void run()
		{
			try {
				byte[] receiveData = new byte[1024];
				
				socket = new DatagramSocket(PeerChess.instance().port);
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	
				while(true)
				{
					socket.receive(receivePacket);
					
					DatagramPacket response = new DatagramPacket(receiveData, receiveData.length);
					response.setAddress(receivePacket.getAddress());
					response.setPort(receivePacket.getPort());
					response.setData(new String("PING:" + PeerChess.instance().username).getBytes());
					socket.send(response);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
