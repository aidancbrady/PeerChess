package com.aidancbrady.peerchess;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;

public final class PeerUtils
{
	public static boolean isValidIP(String s)
	{
		if(s == null || s.isEmpty())
		{
			return false;
		}
		
		s = s.trim().replace(".", ":");
		
		if(s.equals("localhost"))
		{
			return true;
		}
		
		String[] split = s.split(":");
		
		if(split.length != 4)
		{
			return false;
		}
		
		for(String b : split)
		{
			if(!isInteger(b))
			{
				return false;
			}
			
			int i = Integer.parseInt(b);
			
			if(i < 0 || i > 255)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isInteger(String s)
	{
		if(s == null || s.isEmpty())
		{
			return false;
		}
		
		try {
			Integer.parseInt(s);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static Set<ChessPos> getValidPawnAttackMoves(ChessPos pos, Side side)
	{
		Set<ChessPos> validDests = new HashSet<ChessPos>();
		
		if(side == Side.BLACK)
		{
			validDests.add(new ChessPos(pos.getX()+1, pos.getY()+1));
			validDests.add(new ChessPos(pos.getX()-1, pos.getY()+1));
		}
		else if(side == Side.WHITE)
		{
			validDests.add(new ChessPos(pos.getX()+1, pos.getY()-1));
			validDests.add(new ChessPos(pos.getX()-1, pos.getY()-1));
		}
		
		for(Iterator<ChessPos> iter = validDests.iterator(); iter.hasNext();)
		{
			ChessPos iterPos = iter.next();
			
			if(iterPos.getX() < 0 || iterPos.getX() > 7 || iterPos.getY() < 0 || iterPos.getY() > 7)
			{
				iter.remove();
			}
		}
		
		return validDests;
	}
	
	public static Set<ChessPos> getValidStepMoves(ChessPos pos)
	{
		Set<ChessPos> validDests = new HashSet<ChessPos>();
		
		validDests.add(new ChessPos(pos.getX()+1, pos.getY()));
		validDests.add(new ChessPos(pos.getX()-1, pos.getY()));
		validDests.add(new ChessPos(pos.getX(), pos.getY()+1));
		validDests.add(new ChessPos(pos.getX(), pos.getY()-1));
		validDests.add(new ChessPos(pos.getX()-1, pos.getY()-1));
		validDests.add(new ChessPos(pos.getX()-1, pos.getY()+1));
		validDests.add(new ChessPos(pos.getX()+1, pos.getY()+1));
		validDests.add(new ChessPos(pos.getX()+1, pos.getY()-1));
		
		for(Iterator<ChessPos> iter = validDests.iterator(); iter.hasNext();)
		{
			ChessPos iterPos = iter.next();
			
			if(iterPos.getX() < 0 || iterPos.getX() > 7 || iterPos.getY() < 0 || iterPos.getY() > 7)
			{
				iter.remove();
			}
		}
		
		return validDests;
	}
	
	public static Set<ChessPos> getValidKnightMoves(ChessPos pos)
	{
		Set<ChessPos> validDests = new HashSet<ChessPos>();
		
		validDests.add(new ChessPos(pos.getX()+2, pos.getY()+1));
		validDests.add(new ChessPos(pos.getX()+2, pos.getY()-1));
		
		validDests.add(new ChessPos(pos.getX()-2, pos.getY()+1));
		validDests.add(new ChessPos(pos.getX()-2, pos.getY()-1));
		
		validDests.add(new ChessPos(pos.getX()+1, pos.getY()+2));
		validDests.add(new ChessPos(pos.getX()-1, pos.getY()+2));
		
		validDests.add(new ChessPos(pos.getX()+1, pos.getY()-2));
		validDests.add(new ChessPos(pos.getX()-1, pos.getY()-2));
		
		for(Iterator<ChessPos> iter = validDests.iterator(); iter.hasNext();)
		{
			ChessPos iterPos = iter.next();
			
			if(iterPos.getX() < 0 || iterPos.getX() > 7 || iterPos.getY() < 0 || iterPos.getY() > 7)
			{
				iter.remove();
			}
		}
		
		return validDests;
	}
	
	public static void debug(String s)
	{
	    if(PeerChess.instance().isDebug())
	    {
	        System.out.println("[DEBUG] " + s);
	    }
	}
	
    public static double[][] reverseArray(double[][] array)
    {
        double[][] ret = new double[8][8];
        
        for(int i = 0; i < array.length; i++)
        {
            ret[i] = array[array.length-i-1];
        }
        
        return ret;
    }
	
	public static ChessPiece getPawnReplace(Side side, int index)
	{
	    PieceType type = PieceType.values()[(PieceType.values().length-2)-index];
	    return new ChessPiece(type, side);
	}
	
	public static ChessSquare[][] deepCopyBoard(ChessSquare[][] grid)
	{
	    ChessSquare[][] ret = new ChessSquare[8][8];
	    
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                ret[x][y] = grid[x][y].copy();
            }
        }
	    
	    return ret;
	}
	
	public static ChessSquare[][] createEmptyBoard()
	{
	    ChessSquare[][] grid = new ChessSquare[8][8];
	    boolean color = false;
	    
        for(int y = 0; y < 8; y++)
        {           
            for(int x = 0; x < 8; x++)
            {
                grid[x][y] = new ChessSquare(color, new ChessPos(x, y));
                color = !color;
            }
            
            color = !color;
        }
        
        return grid;
	}
	
	public static void applyBoard(ChessSquare[][] from, ChessSquare[][] to)
	{
	    for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                to[x][y].setPiece(from[x][y].getPiece());
            }
        }
	}
	
	public static ChessPos invert(ChessPos pos)
	{
	    return new ChessPos(7-pos.getX(), 7-pos.getY());
	}
}
