package com.aidancbrady.peerchess.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.aidancbrady.peerchess.PeerChess;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.gui.ChessPanel;

public class ConnectionWaiter extends Thread
{
	private ChessPanel panel;
	
	private PingResponder responseThread;
	
	private ServerSocket serverSocket;
	
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
			serverSocket = new ServerSocket(PeerChess.instance().getPort());
			
			Socket connection = serverSocket.accept();
			
			if(connection != null)
			{
				panel.setConnection(new PeerConnection(connection, panel, true));
				panel.getConnection().start();
				
				PeerUtils.debug("Received connection from " + connection.getInetAddress() + ":" + connection.getPort());
				
				panel.frame.getWaitingFrame().setVisible(false);
				panel.component.host = true;
			}
		} catch(Exception e) {
		    if(!e.getMessage().contains("Socket closed")) // ignore
            {
    			e.printStackTrace();
    			JOptionPane.showMessageDialog(panel, "Error accepting connection.");
            }
		    
			panel.frame.forceMenu();
		}
		
		try {
			serverSocket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() throws Exception
	{
        if(serverSocket != null)
        {
            serverSocket.close();
        }
        
        if(responseThread != null)
        {
            responseThread.socket.close();
        }
        
        interrupt();
	}
	
	public class PingResponder extends Thread
	{
		public DatagramSocket socket;
		
		@Override
		public void run()
		{
			try {
				byte[] receiveData = new byte[1024];
				
				socket = new DatagramSocket(PeerChess.instance().getPort());
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	
				while(true)
				{
					socket.receive(receivePacket);
					
					DatagramPacket response = new DatagramPacket(receiveData, receiveData.length);
					response.setAddress(receivePacket.getAddress());
					response.setPort(receivePacket.getPort());
					response.setData(new String("PING:" + PeerChess.instance().username).getBytes());
					socket.send(response);
					
					PeerUtils.debug("Sending response datagram to " + receivePacket.getAddress() + ":" + receivePacket.getPort());
				}
			} catch(Exception e) {
			    if(!e.getMessage().contains("Socket closed")) // ignore
			    {
			        e.printStackTrace();
			    }
			}
		}
	}
}
