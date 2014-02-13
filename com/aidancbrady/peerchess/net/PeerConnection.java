package com.aidancbrady.peerchess.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessPanel;
import com.aidancbrady.peerchess.ChessPiece;
import com.aidancbrady.peerchess.ChessPiece.PieceType;
import com.aidancbrady.peerchess.ChessPiece.Side;
import com.aidancbrady.peerchess.ChessPos;
import com.aidancbrady.peerchess.MoveAction;
import com.aidancbrady.peerchess.file.SaveHandler;

public class PeerConnection extends Thread
{
	public Socket socket;
	
	public BufferedReader reader;
	
	public PrintWriter writer;
	
	public OutThread out;
	
	public ChessPanel panel;
	
	public String username;
	
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
				
				if(reading.startsWith("LOAD"))
				{
					SaveHandler.loadFromReader(reader, panel.chess);
				}
				else if(reading.startsWith("MSG"))
				{
					String msg = reading.split(":")[1];
					panel.appendChat(msg);
				}
				else if(reading.startsWith("USER"))
				{
					username = reading.split(":")[1];
					
				}
				else if(reading.startsWith("MOVE"))
				{
					String[] split = reading.split(":");
					
					String[] strPiece = split[0].split(",");
					String[] strNew = split[1].split(",");
					String[] strOldPos = split[2].split(",");
					String[] strNewPos = split[3].split(",");
					
					ChessPiece piece = new ChessPiece(PieceType.values()[Integer.parseInt(strPiece[0])], Side.values()[Integer.parseInt(strPiece[1])]);
					ChessPiece newPiece = new ChessPiece(PieceType.values()[Integer.parseInt(strNew[0])], Side.values()[Integer.parseInt(strNew[1])]);
					ChessPos oldPos = new ChessPos(Integer.parseInt(strOldPos[0]), Integer.parseInt(strOldPos[1]));
					ChessPos newPos = new ChessPos(Integer.parseInt(strNewPos[0]), Integer.parseInt(strNewPos[1]));
					
					oldPos.getSquare(panel.chess.grid).setPiece(null);
					
					panel.chess.currentAnimation = new MoveAction(panel.chess, new ChessMove(oldPos, newPos), piece, newPiece);
				}
			}
			
			reader.close();
			writer.close();
			socket.close();
			
			disconnected = true;
		} catch(Exception e) {}
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
					
					if(s !=  null)
					{
						if(s.equals("UPDATE"))
						{
							SaveHandler.saveToWriter(new BufferedWriter(writer), panel.chess);
						}
						else {
							writer.println(s);
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
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
}
