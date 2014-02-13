package com.aidancbrady.peerchess.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.aidancbrady.peerchess.ChessComponent;
import com.aidancbrady.peerchess.ChessPiece;
import com.aidancbrady.peerchess.ChessPiece.PieceType;
import com.aidancbrady.peerchess.ChessPiece.Side;
import com.aidancbrady.peerchess.ChessSquare;

public final class SaveHandler 
{
	public static File saveDir = new File(getHomeDirectory() + File.separator + "Documents" + File.separator + "PeerChess" + File.separator + "Saves");
	
	public static void init()
	{
		saveDir.mkdirs();
	}
	
	public static boolean saveExists(String name)
	{
		name = name.trim().replace(".chess", "");
		
		for(File file : saveDir.listFiles())
		{
			if(file.getName().equals(name + ".chess"))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean saveGame(ChessComponent chess, String name)
	{		
		try {
			File save = new File(saveDir, getTrimmedName(name) + ".chess");
			
			if(save.exists())
			{
				save.delete();
			}
			
			save.createNewFile();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(save));
			
			if(chess.currentAnimation != null)
			{
				chess.currentAnimation.move();
			}
			
			if(chess.whiteTaken.isEmpty())
			{
				writer.append("null");
			}
			
			for(ChessPiece piece : chess.whiteTaken)
			{
				writer.append(Integer.toString(piece.type.ordinal()));
				
				if(chess.whiteTaken.get(chess.whiteTaken.size()-1) != piece)
				{
					writer.append(":");
				}
			}
			
			writer.newLine();
			
			if(chess.blackTaken.isEmpty())
			{
				writer.append("null");
			}
			
			for(ChessPiece piece : chess.blackTaken)
			{
				writer.append(Integer.toString(piece.type.ordinal()));
				
				if(chess.blackTaken.get(chess.blackTaken.size()-1) != piece)
				{
					writer.append(":");
				}
			}
			
			writer.newLine();
			
			writer.append(Integer.toString(chess.side.ordinal()));
			writer.newLine();
			writer.append(Integer.toString(chess.turn.ordinal()));
			writer.newLine();
			
			for(int y = 0; y < 8; y++)
			{
				StringBuilder builder = new StringBuilder();
				
				for(int x = 0; x < 8; x++)
				{
					ChessSquare square = chess.grid[x][y];
					
					if(square.housedPiece == null)
					{
						builder.append("null");
					}
					else {
						ChessPiece piece = square.housedPiece;
						builder.append(piece.type.ordinal() + "," + piece.side.ordinal());
					}
					
					if(x != 7)
					{
						builder.append(":");
					}
				}
				
				writer.append(builder.toString());
				writer.newLine();
			}
			
			writer.flush();
			writer.close();
			
			return true;
		} catch(Exception e) {
			System.err.println("Error while writing to file:");
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean loadGame(ChessComponent chess, File file)
	{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String whiteLine = reader.readLine();
			
			if(!whiteLine.equals("null"))
			{
				String[] whiteTaken = whiteLine.split(":");
				
				for(String s : whiteTaken)
				{
					PieceType type = PieceType.values()[Integer.parseInt(s)];
					chess.whiteTaken.add(new ChessPiece(type, Side.BLACK));
				}
			}
			
			String blackLine = reader.readLine();
			
			if(!blackLine.equals("null"))
			{
				String[] blackTaken = blackLine.split(":");
				
				for(String s : blackTaken)
				{
					PieceType type = PieceType.values()[Integer.parseInt(s)];
					chess.whiteTaken.add(new ChessPiece(type, Side.WHITE));
				}
			}
			
			chess.setSide(Side.values()[Integer.parseInt(reader.readLine())]);
			chess.turn = Side.values()[Integer.parseInt(reader.readLine())];
			
			for(int y = 0; y < 8; y++)
			{
				String line = reader.readLine();
				String[] segs = line.split(":");
				
				for(int x = 0; x < 8; x++)
				{
					if(segs[x].equals("null"))
					{
						chess.grid[x][y].setPiece(null);
					}
					else {
						String[] data = segs[x].split(",");
						
						PieceType type = PieceType.values()[Integer.parseInt(data[0])];
						Side side = Side.values()[Integer.parseInt(data[1])];
						
						chess.grid[x][y].setPiece(new ChessPiece(type, side));
					}
				}
			}
			
			reader.close();
			
			return true;
		} catch(Exception e) {
			System.err.println("Error while reading from file:");
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getTrimmedName(String s)
	{
		return s.trim().replace(".chess", "");
	}
	
	public static String getHomeDirectory()
	{
		return System.getProperty("user.home");
	}
}
