package com.aidancbrady.peerchess.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import com.aidancbrady.peerchess.MoveAction;
import com.aidancbrady.peerchess.PeerChess;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.file.SaveHandler;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.gui.ChessPanel;

public class PeerConnection extends Thread
{
	private Socket socket;
	
	private PeerEncryptor encryptor;
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	private OutThread out;
	
	private ChessPanel panel;
	
	private String opponentUsername;
	
	private boolean host = false;
	
	private boolean disconnected = false;
	
	public PeerConnection(Socket s, ChessPanel p, boolean h)
	{
		socket = s;
		panel = p;
		
		host = h;
		
		out = new OutThread();
		encryptor = new PeerEncryptor();
	}
	
	@Override
	public void run()
	{
		try {
		    if(!encryptor.init(this))
		    {
		        JOptionPane.showMessageDialog(panel.frame, "Failed to initialize security layer.");
	            panel.frame.openMenu();
	            
	            close();
	            
	            disconnected = true;
		        return;
		    }
		    
		    if(!encryptor.parseHandshake(this))
            {
                JOptionPane.showMessageDialog(panel.frame, "Failed to complete security protocol.");
                panel.frame.openMenu();
                
                close();
                
                disconnected = true;
                return;
            }
		    
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			
			//Start data output thread
			out.start();
			
			if(host)
            {
                write("UPDATE");
            }
			
            write("USER:" + PeerChess.instance().username);
			
			String reading = "";
			
			while((reading = reader.readLine()) != null && !disconnected)
			{
				//Decrypt and trim message
                reading = encryptor.decrypt(reading).trim();
                
                PeerUtils.debug("Received message: " + reading);
				
				if(reading.startsWith("UPDATE"))
				{
					SaveHandler.loadFromReader(reader, panel.chess);
					panel.chess.getGame().setSide(panel.chess.getGame().getSide().getOpposite());
					panel.chess.host = !panel.chess.host;
					panel.updateText();
				}
				else if(reading.startsWith("MSG"))
				{
					String msg = reading.substring(4);
					panel.appendChat(opponentUsername + ": " + msg);
				}
				else if(reading.startsWith("USER"))
				{
					opponentUsername = reading.substring(5);
					panel.updateText();
					
					if(host)
					{
						JOptionPane.showMessageDialog(panel, opponentUsername + " has connected to the game");
					}
					else {
						JOptionPane.showMessageDialog(panel, "You have joined " + opponentUsername + "'s game");
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
					
					newPiece.setMoves(oldPos.getSquare(panel.chess.grid).getPiece().getMoves());
					
					ChessMove move = new ChessMove(oldPos, newPos);
					oldPos.getSquare(panel.chess.grid).getPiece().getType().getPiece().validateMove(panel.chess, move);
					
					panel.chess.currentMove = new MoveAction(panel.chess, move, piece, newPiece);
					panel.updateText();
				}
				else {
				    throw new Exception("invalid request received");
				}
			}
			
			JOptionPane.showMessageDialog(panel.frame, opponentUsername + " has disconnected.");
			panel.frame.openMenu();
			
			close();
			
			disconnected = true;
		} catch(Exception e) {
		    if(!e.getMessage().contains("Socket closed")) // ignore
            {
		        JOptionPane.showMessageDialog(panel.frame, "Connection error: " + e.getMessage());
		        e.printStackTrace();
            }
			
			panel.frame.openMenu();
			
			close();
		}
	}
	
	public class OutThread extends Thread
	{
		public final LinkedList<String> outList = new LinkedList<String>();
		
		@Override
		public void run()
		{
			while(!disconnected)
			{
				try {
					String s = outList.poll();
					
					if(s != null)
					{
					    PeerUtils.debug("Sending message: " + s);
					    
						writer.println(encryptor.encrypt(s));
						
						if(s.equals("UPDATE"))
						{
							SaveHandler.saveToWriter(new BufferedWriter(writer), panel.chess);
						}
						
						writer.flush();
					}
					
					Thread.sleep(1);
				} catch(Exception e) {
				    if(!e.getMessage().contains("sleep interrupted"))
				    {
				        e.printStackTrace();
				    }
				}
			}
			
			PeerUtils.debug("Ending output stream");
		}
	}
	
	protected Socket getSocket()
	{
	    return socket;
	}
	
	public String getOpponent()
	{
	    return opponentUsername;
	}
	
	public void write(String s)
	{
		out.outList.add(s);
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
		
		panel.setConnection(null);
		panel.updateText();
	}
}
