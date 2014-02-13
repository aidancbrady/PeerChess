package com.aidancbrady.peerchess.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import com.aidancbrady.peerchess.ChessPanel;
import com.aidancbrady.peerchess.PeerChess;

public class ConnectionWaiter extends Thread
{
	public ChessPanel panel = PeerChess.instance().frame.chess;
	
	public PingResponder responseThread;
	
	public ServerSocket serverSocket;
	
	@Override
	public void run()
	{
		try {
			serverSocket = new ServerSocket(PeerChess.instance().port);
			
			Socket connection = serverSocket.accept();
			
			if(connection != null)
			{
				new PeerConnection(connection, panel).start();
			}
			
			serverSocket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public class PingResponder extends Thread
	{
		@Override
		public void run()
		{
			try {
				byte[] receiveData = new byte[1024];
				
				DatagramSocket clientSocket = new DatagramSocket(PeerChess.instance().port);
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	
				while(true)
				{
					clientSocket.receive(receivePacket);
					
					DatagramPacket response = new DatagramPacket(receiveData, receiveData.length);
					response.setAddress(receivePacket.getAddress());
					response.setPort(receivePacket.getPort());
					response.setData(new String("PING:" + PeerChess.instance().username).getBytes());
					clientSocket.send(response);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
