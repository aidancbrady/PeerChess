package com.aidancbrady.peerchess.net;

import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.aidancbrady.peerchess.PeerChess;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.gui.ChessPanel;
import com.aidancbrady.peerchess.net.GameScanner.Server;

public class GameConnector extends Thread
{	
	public ChessPanel panel;
	
	public Server server;
	
	public Socket socket;
	
	public GameConnector(Server s, ChessPanel p)
	{
		server = s;
		panel = p;
		
		start();
	}
	
	@Override
	public void run()
	{
		try {
		    socket = new Socket();
			socket.connect(new InetSocketAddress(server.ip, PeerChess.instance().port), GameScanner.MAX_PING);
			
			(panel.connection = new PeerConnection(socket, panel, false)).start();
			panel.connection.username = server.username;
			panel.updateText();
			
			PeerUtils.debug("Connected to " + server.ip);
			
			synchronized(panel.frame.connecting)
			{
			    panel.frame.connecting.setVisible(false);
			}
			
			panel.frame.forceChess(true);
			
			panel.frame.revalidate();
	        panel.frame.onWindowResized();
	        panel.chess.setupBoard(true);
		} catch(Exception e) {
			panel.frame.connecting.setVisible(false);
			JOptionPane.showMessageDialog(panel, "Couldn't connect to server: " + e.getLocalizedMessage());
			
			try {
				socket.close();
			} catch(Exception e1) {}
			
			e.printStackTrace();
		}
	}
}
