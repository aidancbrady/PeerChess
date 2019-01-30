package com.aidancbrady.peerchess.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.aidancbrady.peerchess.ChessComponent;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.Endgame;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessSquare;

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
			
			saveToWriter(writer, chess);
			
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
			
			loadFromReader(reader, chess);
			
			reader.close();
			
			return true;
		} catch(Exception e) {
			System.err.println("Error while reading from file:");
			e.printStackTrace();
			return false;
		}
	}
	
	public static void saveToWriter(BufferedWriter writer, ChessComponent chess) throws IOException
	{
		if(chess.isMoving())
		{
			chess.currentMove.move();
		}
		
		writer.append(Integer.toString(chess.getGame().getSide().ordinal()));
		writer.newLine();
		writer.append(Integer.toString(chess.getGame().getTurn().ordinal()));
		writer.newLine();
		writer.append(Boolean.toString(chess.multiplayer));
		writer.newLine();
		writer.append(Boolean.toString(chess.host));
        writer.newLine();
		writer.append(chess.getGame().getEndgame() != null ? Integer.toString(chess.getGame().getEndgame().ordinal()) : "-1");
		writer.newLine();
		writer.append(chess.getGame().getSideInCheck() != null ? Integer.toString(chess.getGame().getSideInCheck().ordinal()) : "-1");
		writer.newLine();
		
		writer.append(Integer.toString(chess.getGame().getMoves().size()));
		writer.newLine();
		
		for(ChessMove move : chess.getGame().getMoves())
		{
		    writer.append(move.serialize());
		    writer.newLine();
		}
		
		writer.append(saveChessBoard(chess.grid));
		writer.newLine();
		writer.flush();
	}
	
	public static void loadFromReader(BufferedReader reader, ChessComponent chess) throws IOException
	{
		chess.getGame().setSide(Side.values()[Integer.parseInt(reader.readLine())]);
		chess.getGame().setTurn(Side.values()[Integer.parseInt(reader.readLine())]);
		chess.multiplayer = Boolean.parseBoolean(reader.readLine());
		chess.host = Boolean.parseBoolean(reader.readLine());
		
		int check = Integer.parseInt(reader.readLine());
		chess.getGame().setEndgame(check == -1 ? null : Endgame.values()[check]);
		
		int s = Integer.parseInt(reader.readLine());
		chess.getGame().setSideInCheck(s == -1 ? null : Side.values()[s]);
		
		try {
    		int moveCount = Integer.parseInt(reader.readLine());
    		
    		for(int i = 0; i < moveCount; i++)
    		{
    		    chess.getGame().getMoves().add(ChessMove.create(reader.readLine()));
    		}
		} catch(Exception e) {
		    throw new IOException("Failed to read move history.", e);
		}
		
		PeerUtils.applyBoard(loadChessBoard(reader.readLine()), chess.grid);
	}
	
	public static String saveChessBoard(ChessSquare[][] grid)
	{
	    StringBuilder builder = new StringBuilder();
	    
	    for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                ChessSquare square = grid[x][y];
                
                if(square.getPiece() == null)
                {
                    builder.append("null");
                }
                else {
                    ChessPiece piece = square.getPiece();
                    builder.append(piece.getType().ordinal() + "," + piece.getSide().ordinal() + "," + piece.getMoves());
                }
                
                if(y != 7 || x != 7)
                {
                    builder.append(":");
                }
            }
        }
	    
	    return builder.toString();
	}
	
	public static ChessSquare[][] loadChessBoard(String s)
	{
	    ChessSquare[][] grid = PeerUtils.createEmptyBoard();
	    String[] split = s.split(":");
	    
	    for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                if(split[y*8 + x].equals("null"))
                {
                    grid[x][y].setPiece(null);
                }
                else {
                    String[] data = split[y*8 + x].split(",");
                    
                    PieceType type = PieceType.values()[Integer.parseInt(data[0])];
                    Side side = Side.values()[Integer.parseInt(data[1])];
                    ChessPiece piece = new ChessPiece(type, side);
                    piece.setMoves(Integer.parseInt(data[2]));
                    
                    grid[x][y].setPiece(piece);
                }
            }
        }
	    
	    return grid;
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
