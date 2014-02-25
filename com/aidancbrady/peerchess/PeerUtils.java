package com.aidancbrady.peerchess;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aidancbrady.peerchess.ChessPiece.PieceType;
import com.aidancbrady.peerchess.ChessPiece.Side;

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
	
	public static boolean isInCheck(Side side, ChessPos pos, ChessSquare[][] grid)
	{
		int pointer = pos.xPos;
		
		Set<ChessPos> knightMoves = getValidKnightMoves(pos);
		
		for(ChessPos iterPos : knightMoves)
		{
			if(iterPos.getSquare(grid).housedPiece != null && iterPos.getSquare(grid).housedPiece.side != side)
			{
				return true;
			}
		}
		
		while(pointer < 7)
		{
			pointer++;
			
			if(grid[pointer][pos.yPos].housedPiece != null && grid[pointer][pos.yPos].housedPiece.side != side)
			{
				ChessPiece piece = grid[pointer][pos.yPos].housedPiece;
				break;
			}
		}
		
		pointer = pos.xPos;
		
		while(pointer > 0)
		{
			pointer--;
			
			if(grid[pointer][pos.yPos].housedPiece != null && grid[pointer][pos.yPos].housedPiece.side != side)
			{
				ChessPiece piece = grid[pointer][pos.yPos].housedPiece;
				break;
			}
		}
		
		pointer = pos.yPos;
		
		while(pointer < 7)
		{
			pointer++;
			
			if(grid[pointer][pos.yPos].housedPiece != null && grid[pointer][pos.yPos].housedPiece.side != side)
			{
				ChessPiece piece = grid[pointer][pos.yPos].housedPiece;
				break;
			}
		}
		
		pointer = pos.yPos;
		
		while(pointer > 0)
		{
			pointer--;
			
			if(grid[pointer][pos.yPos].housedPiece != null && grid[pointer][pos.yPos].housedPiece.side != side)
			{
				ChessPiece piece = grid[pointer][pos.yPos].housedPiece;
				break;
			}
		}
		
		return false;
	}
	
	public static ChessPos findKing(Side side, ChessSquare[][] grid)
	{
		for(ChessSquare[] array : grid)
		{
			for(ChessSquare square : array)
			{
				if(square.housedPiece != null)
				{
					if(square.housedPiece.type == PieceType.KING && square.housedPiece.side == side)
					{
						return square.pos.clone();
					}
				}
			}
		}
		
		return null;
	}
	
	public static Set<ChessPos> getValidKnightMoves(ChessPos pos)
	{
		Set<ChessPos> validDests = new HashSet<ChessPos>();
		
		validDests.add(new ChessPos(pos.xPos+2, pos.yPos+1));
		validDests.add(new ChessPos(pos.xPos+2, pos.yPos-1));
		
		validDests.add(new ChessPos(pos.xPos-2, pos.yPos+1));
		validDests.add(new ChessPos(pos.xPos-2, pos.yPos-1));
		
		validDests.add(new ChessPos(pos.xPos+1, pos.yPos+2));
		validDests.add(new ChessPos(pos.xPos-1, pos.yPos+2));
		
		validDests.add(new ChessPos(pos.xPos+1, pos.yPos-2));
		validDests.add(new ChessPos(pos.xPos-1, pos.yPos-2));
		
		for(Iterator<ChessPos> iter = validDests.iterator(); iter.hasNext();)
		{
			ChessPos iterPos = iter.next();
			
			if(iterPos.xPos < 0 || iterPos.xPos > 7 || iterPos.yPos < 0 || iterPos.yPos > 7)
			{
				iter.remove();
			}
		}
		
		return validDests;
	}
}
