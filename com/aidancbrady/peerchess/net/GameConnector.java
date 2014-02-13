package com.aidancbrady.peerchess.net;

import java.net.Socket;

import com.aidancbrady.peerchess.ChessPanel;
import com.aidancbrady.peerchess.PeerChess;

public class GameConnector extends Thread
{
	public String ip;
	
	public ChessPanel panel;
	
	public GameConnector(String s, ChessPanel p)
	{
		ip = s;
		panel = p;
	}
	
	@Override
	public void run()
	{
		try {
			Socket s = new Socket(ip, PeerChess.instance().port);
			
			(panel.connection = new PeerConnection(s, panel)).start();
			
			panel.connection.write("USER:" + PeerChess.instance().username);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
