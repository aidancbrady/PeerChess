package com.aidancbrady.peerchess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;
import com.aidancbrady.peerchess.piece.Piece;

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
	
	public static boolean isCheckMate(Side side, IChessGame game)
	{
		ChessPos pos = findKing(side, game.getGrid());
		
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				if(game.getGrid()[x][y].getPiece() != null && game.getGrid()[x][y].getPiece().side == side)
				{
					ChessPiece piece = game.getGrid()[x][y].getPiece();
					
					for(ChessPos newPos : piece.type.getPiece().getCurrentPossibleMoves(game, new ChessPos(x, y)))
					{
						ChessMove move = new ChessMove(new ChessPos(x, y), newPos);
						ChessPos kingPos = piece.type == PieceType.KING ? newPos : pos;
						
						if(!testCheck(side, kingPos, game.getGrid(), move))
						{
						    PeerUtils.debug(side + " is not in checkmate.");
						    PeerUtils.debug("Valid move: " + piece + " at " + move.fromPos + " to " + move.toPos);
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	public static boolean testCheck(Side side, ChessPos pos, ChessSquare[][] grid, ChessMove move)
	{
	    move.testApplyMove(grid);
	    boolean inCheck = isInCheck(side, pos, grid);
	    move.testRevertMove(grid);
	    return inCheck;
	}
	
	public static boolean isInCheck(Side side, ChessPos pos, ChessSquare[][] grid)
	{
		Set<ChessPos> moves = getValidKnightMoves(pos);
		
		for(ChessPos iterPos : moves)
		{
			if(iterPos.getSquare(grid).getPiece() != null && iterPos.getSquare(grid).getPiece().side != side && iterPos.getSquare(grid).getPiece().type == PieceType.KNIGHT)
			{
				return true;
			}
		}
		
		moves = getValidPawnAttackMoves(pos, side);
		
		for(ChessPos iterPos : moves)
		{
			if(iterPos.xPos != pos.xPos)
			{
				if(iterPos.getSquare(grid).getPiece() != null && iterPos.getSquare(grid).getPiece().side != side && iterPos.getSquare(grid).getPiece().type == PieceType.PAWN)
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
			
			if(grid[xPointer][pos.yPos].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][pos.yPos].getPiece();
				
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
			
			if(grid[xPointer][pos.yPos].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][pos.yPos].getPiece();
				
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
			
			if(grid[pos.xPos][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[pos.xPos][yPointer].getPiece();
				
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
			
			if(grid[pos.xPos][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[pos.xPos][yPointer].getPiece();
				
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
			
			if(grid[xPointer][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].getPiece();
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.BISHOP || piece.type == PieceType.QUEEN)
					{
						return true;
					}
					else if(piece.type == PieceType.KING && Math.abs(pos.xPos-xPointer) == 1 && Math.abs(pos.yPos-yPointer) == 1)
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
			
			if(grid[xPointer][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].getPiece();
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.BISHOP || piece.type == PieceType.QUEEN)
					{
						return true;
					}
					else if(piece.type == PieceType.KING && Math.abs(pos.xPos-xPointer) == 1 && Math.abs(pos.yPos-yPointer) == 1)
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
			
			if(grid[xPointer][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].getPiece();
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.BISHOP || piece.type == PieceType.QUEEN)
					{
						return true;
					}
					else if(piece.type == PieceType.KING && Math.abs(pos.xPos-xPointer) == 1 && Math.abs(pos.yPos-yPointer) == 1)
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
			
			if(grid[xPointer][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].getPiece();
				
				if(piece.side != side)
				{
					if(piece.type == PieceType.BISHOP || piece.type == PieceType.QUEEN)
					{
						return true;
					}
					else if(piece.type == PieceType.KING && Math.abs(pos.xPos-xPointer) == 1 && Math.abs(pos.yPos-yPointer) == 1)
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
				if(square.getPiece() != null)
				{
					if(square.getPiece().type == PieceType.KING && square.getPiece().side == side)
					{
						return square.getPos().clone();
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
		validDests.add(new ChessPos(pos.xPos-1, pos.yPos-1));
		validDests.add(new ChessPos(pos.xPos-1, pos.yPos+1));
		validDests.add(new ChessPos(pos.xPos+1, pos.yPos+1));
		validDests.add(new ChessPos(pos.xPos+1, pos.yPos-1));
		
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
	
	public static void debug(String s)
	{
	    if(PeerChess.instance().debug)
	    {
	        System.out.println("[DEBUG] " + s);
	    }
	}
	
	public static List<ChessPos> getValidatedMoves(ChessComponent chess)
	{
	    List<ChessPos> ret = new ArrayList<>();
	    
	    Piece piece = chess.getSelectedPiece().type.getPiece();
	    
	    for(ChessPos pos : piece.getCurrentPossibleMoves(chess, chess.selected.getPos()))
	    {
	        ChessMove move = new ChessMove(chess.selected.getPos(), pos);
	        
	        if(piece.validateMove(chess, move))
	        {
	            ret.add(pos);
	        }
	    }
	    
	    return ret;
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
}
