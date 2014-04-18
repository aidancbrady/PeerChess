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
	
	public static boolean isCheckMate(Side side, ChessSquare[][] grid)
	{
		ChessPos pos = findKing(side, grid);
		
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				if(grid[x][y].housedPiece != null && grid[x][y].housedPiece.side == side)
				{
					ChessPiece piece = grid[x][y].housedPiece;
					
					for(ChessPos newPos : piece.type.getPiece().getCurrentPossibleMoves(grid, new ChessPos(x, y)))
					{
						ChessMove move = new ChessMove(new ChessPos(x, y), newPos);
						
						if(!isInCheck(side, pos, move.getFakeGrid(grid)))
						{
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	public static boolean isInCheck(Side side, ChessPos pos, ChessSquare[][] grid)
	{
		Set<ChessPos> moves = getValidKnightMoves(pos);
		
		for(ChessPos iterPos : moves)
		{
			if(iterPos.getSquare(grid).housedPiece != null && iterPos.getSquare(grid).housedPiece.side != side && iterPos.getSquare(grid).housedPiece.type == PieceType.KNIGHT)
			{
				return true;
			}
		}
		
		moves = getValidPawnAttackMoves(pos, side);
		
		for(ChessPos iterPos : moves)
		{
			if(iterPos.xPos != pos.xPos)
			{
				if(iterPos.getSquare(grid).housedPiece != null && iterPos.getSquare(grid).housedPiece.side != side && iterPos.getSquare(grid).housedPiece.type == PieceType.PAWN)
				{
					return true;
				}
			}
		}
		
		int xPointer = pos.xPos;
		int yPointer = pos.yPos;
		
		while(xPointer < 7)
		{
			xPointer++;
			
			if(grid[xPointer][pos.yPos].housedPiece != null)
			{
				ChessPiece piece = grid[xPointer][pos.yPos].housedPiece;
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.KING && Math.abs(pos.xPos-xPointer) == 1)
					{
						return true;
					}
					else if(piece.type == PieceType.CASTLE || piece.type == PieceType.QUEEN)
					{
						return true;
					}
				}
				
				break;
			}
		}
		
		xPointer = pos.xPos;
		yPointer = pos.yPos;
		
		while(xPointer > 0)
		{
			xPointer--;
			
			if(grid[xPointer][pos.yPos].housedPiece != null)
			{
				ChessPiece piece = grid[xPointer][pos.yPos].housedPiece;
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.KING && Math.abs(pos.xPos-xPointer) == 1)
					{
						return true;
					}
					else if(piece.type == PieceType.CASTLE || piece.type == PieceType.QUEEN)
					{
						return true;
					}
				}
				
				break;
			}
		}
		
		xPointer = pos.xPos;
		yPointer = pos.yPos;
		
		while(yPointer < 7)
		{
			yPointer++;
			
			if(grid[pos.xPos][yPointer].housedPiece != null)
			{
				ChessPiece piece = grid[pos.xPos][yPointer].housedPiece;
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.KING && Math.abs(pos.yPos-yPointer) == 1)
					{
						return true;
					}
					else if(piece.type == PieceType.CASTLE || piece.type == PieceType.QUEEN)
					{
						return true;
					}
				}
				
				break;
			}
		}
		
		yPointer = pos.yPos;
		
		while(yPointer > 0)
		{
			yPointer--;
			
			if(grid[pos.xPos][yPointer].housedPiece != null)
			{
				ChessPiece piece = grid[pos.xPos][yPointer].housedPiece;
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.KING && Math.abs(pos.yPos-yPointer) == 1)
					{
						return true;
					}
					else if(piece.type == PieceType.CASTLE || piece.type == PieceType.QUEEN)
					{
						return true;
					}
				}
				
				break;
			}
		}
		
		xPointer = pos.xPos;
		yPointer = pos.yPos;
		
		while(xPointer < 7 && yPointer < 7)
		{
			xPointer++;
			yPointer++;
			
			if(grid[xPointer][yPointer].housedPiece != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].housedPiece;
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.BISHOP || piece.type == PieceType.QUEEN)
					{
						return true;
					}
				}
				
				break;
			}
		}
		
		xPointer = pos.xPos;
		yPointer = pos.yPos;
		
		while(xPointer > 0 && yPointer > 0)
		{
			xPointer--;
			yPointer--;
			
			if(grid[xPointer][yPointer].housedPiece != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].housedPiece;
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.BISHOP || piece.type == PieceType.QUEEN)
					{
						return true;
					}
				}
				
				break;
			}
		}
		
		xPointer = pos.xPos;
		yPointer = pos.yPos;
		
		while(xPointer < 7 && yPointer > 0)
		{
			xPointer++;
			yPointer--;
			
			if(grid[xPointer][yPointer].housedPiece != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].housedPiece;
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.BISHOP || piece.type == PieceType.QUEEN)
					{
						return true;
					}
				}
				
				break;
			}
		}
		
		xPointer = pos.xPos;
		yPointer = pos.yPos;
		
		while(xPointer > 0 && yPointer < 7)
		{
			xPointer--;
			yPointer++;
			
			if(grid[xPointer][yPointer].housedPiece != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].housedPiece;
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.BISHOP || piece.type == PieceType.QUEEN)
					{
						return true;
					}
				}
				
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
	
	public static Set<ChessPos> getValidPawnAttackMoves(ChessPos pos, Side side)
	{
		Set<ChessPos> validDests = new HashSet<ChessPos>();
		
		if(side == Side.BLACK)
		{
			validDests.add(new ChessPos(pos.xPos+1, pos.yPos+1));
			validDests.add(new ChessPos(pos.xPos-1, pos.yPos+1));
		}
		else if(side == Side.WHITE)
		{
			validDests.add(new ChessPos(pos.xPos+1, pos.yPos-1));
			validDests.add(new ChessPos(pos.xPos-1, pos.yPos-1));
		}
		
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
	
	public static Set<ChessPos> getValidStepMoves(ChessPos pos)
	{
		Set<ChessPos> validDests = new HashSet<ChessPos>();
		
		validDests.add(new ChessPos(pos.xPos+1, pos.yPos));
		validDests.add(new ChessPos(pos.xPos-1, pos.yPos));
		validDests.add(new ChessPos(pos.xPos, pos.yPos+1));
		validDests.add(new ChessPos(pos.xPos, pos.yPos-1));
		
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
