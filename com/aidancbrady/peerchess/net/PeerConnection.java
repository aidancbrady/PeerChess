package com.aidancbrady.peerchess.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessPanel;
import com.aidancbrady.peerchess.ChessPiece;
import com.aidancbrady.peerchess.ChessPiece.PieceType;
import com.aidancbrady.peerchess.ChessPiece.Side;
import com.aidancbrady.peerchess.ChessPos;
import com.aidancbrady.peerchess.MoveAction;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.file.SaveHandler;

public class PeerConnection extends Thread
{
	public Socket socket;
	
	public BufferedReader reader;
	
	public PrintWriter writer;
	
	public OutThread out;
	
	public ChessPanel panel;
	
	public String username;
	
	public boolean host = false;
	
	public boolean disconnected = false;
	
	public PeerConnection(Socket s, ChessPanel p)
	{
		socket = s;
		panel = p;
		
		out = new OutThread();
	}
	
	@Override
	public void run()
	{
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			
			out.start();
			
			String reading = "";
			
			while((reading = reader.readLine()) != null && !disconnected)
			{
				reading = reading.trim();
				PeerUtils.debug("Received message: " + reading);
				
				if(reading.startsWith("UPDATE"))
				{
					SaveHandler.loadFromReader(reader, panel.chess);
					panel.chess.side = panel.chess.side == Side.WHITE ? Side.BLACK : Side.WHITE;
					panel.updateText();
				}
				else if(reading.startsWith("MSG"))
				{
					String msg = reading.split(":")[1];
					panel.appendChat(username + ": " + msg);
				}
				else if(reading.startsWith("USER"))
				{
					username = reading.split(":")[1];
					panel.updateText();
					
					if(host)
					{
						JOptionPane.showMessageDialog(panel, username + " has connected to the game");
					}
					else {
						JOptionPane.showMessageDialog(panel, "You have joined " + username + "'s game");
					}
				}
				else if(reading.startsWith("MOVE"))
				{
					String[] split = reading.split(":");
					
					String[] strPiece = split[1].split(",");
					String[] strNew = split[2].split(",");
					String[] strOldPos = split[3].split(",");
					String[] strNewPos = split[4].split(",");
					
					ChessPiece piece = new ChessPiece(PieceType.values()[Integer.parseInt(strPiece[0])], Side.values()[Integer.parseInt(strPiece[1])]);
					ChessPiece newPiece = new ChessPiece(PieceType.values()[Integer.parseInt(strNew[0])], Side.values()[Integer.parseInt(strNew[1])]);
					ChessPos oldPos = new ChessPos(Integer.parseInt(strOldPos[0]), Integer.parseInt(strOldPos[1]));
					ChessPos newPos = new ChessPos(Integer.parseInt(strNewPos[0]), Integer.parseInt(strNewPos[1]));
					
					oldPos.getSquare(panel.chess.grid).setPiece(null);
					
					panel.chess.currentAnimation = new MoveAction(panel.chess, new ChessMove(oldPos, newPos), piece, newPiece);
					panel.updateText();
				}
			}
			
			JOptionPane.showMessageDialog(panel.frame, username + " has disconnected.");
			panel.frame.openMenu();
			
			close();
			
			disconnected = true;
		} catch(Exception e) {
		    if(!e.getMessage().contains("Socket closed")) // ignore
            {
		        e.printStackTrace();
            }
			
			JOptionPane.showMessageDialog(panel.frame, username + " has disconnected.");
			panel.frame.openMenu();
			
			close();
		}
	}
	
	public class OutThread extends Thread
	{
		public LinkedList<String> outList = new LinkedList<String>();
		
		@Override
		public void run()
		{
			while(!disconnected)
			{
				try {
					String s = outList.poll();
					
					if(s != null)
					{
						writer.println(s);
						
						if(s.equals("UPDATE"))
						{
							SaveHandler.saveToWriter(new BufferedWriter(writer), panel.chess);
						}
						
						PeerUtils.debug("Sent message: " + s);
						
						writer.flush();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			PeerUtils.debug("Ending output stream");
		}
	}
	
	public void write(String s)
	{
		out.outList.add(s);
	}
	
	public void move(MoveAction move)
	{
		write(move.write());
	}
	
	public void chat(String text)
	{
		write("MSG:" + text.trim());
	}
	
	public void close()
	{
		if(out != null)
		{
			out.interrupt();
		}
		
		if(socket != null)
		{
			try {
				socket.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		panel.connection = null;
		panel.updateText();
	}
}
