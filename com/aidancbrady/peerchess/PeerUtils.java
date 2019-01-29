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
		if(!PeerUtils.isInCheck(side, pos, game.getGrid())) return false;
		
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				if(game.getGrid()[x][y].getPiece() != null && game.getGrid()[x][y].getPiece().getSide() == side)
				{
					ChessPiece piece = game.getGrid()[x][y].getPiece();
					
					for(ChessPos newPos : piece.getType().getPiece().getCurrentPossibleMoves(game, new ChessPos(x, y)))
					{
						ChessMove move = new ChessMove(new ChessPos(x, y), newPos);
						ChessPos kingPos = piece.getType() == PieceType.KING ? newPos : pos;
						
						if(!testCheck(side, kingPos, game.getGrid(), move))
						{
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
			if(iterPos.getSquare(grid).getPiece() != null && iterPos.getSquare(grid).getPiece().getSide() != side && iterPos.getSquare(grid).getPiece().getType() == PieceType.KNIGHT)
			{
				return true;
			}
		}
		
		moves = getValidPawnAttackMoves(pos, side);
		
		for(ChessPos iterPos : moves)
		{
			if(iterPos.getX() != pos.getX())
			{
				if(iterPos.getSquare(grid).getPiece() != null && iterPos.getSquare(grid).getPiece().getSide() != side && iterPos.getSquare(grid).getPiece().getType() == PieceType.PAWN)
				{
					return true;
				}
			}
		}
		
		int xPointer = pos.getX();
		int yPointer = pos.getY();
		
		while(xPointer < 7)
		{
			xPointer++;
			
			if(grid[xPointer][pos.getY()].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][pos.getY()].getPiece();
				
				if(piece.getSide() != side)
				{
					if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1)
					{
						return true;
					}
					else if(piece.getType() == PieceType.CASTLE || piece.getType() == PieceType.QUEEN)
					{
						return true;
					}
				}
				
				break;
			}
		}
		
		xPointer = pos.getX();
		yPointer = pos.getY();
		
		while(xPointer > 0)
		{
			xPointer--;
			
			if(grid[xPointer][pos.getY()].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][pos.getY()].getPiece();
				
				if(piece.getSide() != side)
				{
					if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1)
					{
						return true;
					}
					else if(piece.getType() == PieceType.CASTLE || piece.getType() == PieceType.QUEEN)
					{
						return true;
					}
				}
				
				break;
			}
		}
		
		xPointer = pos.getX();
		yPointer = pos.getY();
		
		while(yPointer < 7)
		{
			yPointer++;
			
			if(grid[pos.getX()][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[pos.getX()][yPointer].getPiece();
				
				if(piece.getSide() != side)
				{
					if(piece.getType() == PieceType.KING && Math.abs(pos.getY()-yPointer) == 1)
					{
						return true;
					}
					else if(piece.getType() == PieceType.CASTLE || piece.getType() == PieceType.QUEEN)
					{
						return true;
					}
				}
				
				break;
			}
		}
		
		yPointer = pos.getY();
		
		while(yPointer > 0)
		{
			yPointer--;
			
			if(grid[pos.getX()][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[pos.getX()][yPointer].getPiece();
				
				if(piece.getSide() != side)
				{
					if(piece.getType() == PieceType.KING && Math.abs(pos.getY()-yPointer) == 1)
					{
						return true;
					}
					else if(piece.getType() == PieceType.CASTLE || piece.getType() == PieceType.QUEEN)
					{
						return true;
					}
				}
				
				break;
			}
		}
		
		xPointer = pos.getX();
		yPointer = pos.getY();
		
		while(xPointer < 7 && yPointer < 7)
		{
			xPointer++;
			yPointer++;
			
			if(grid[xPointer][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].getPiece();
				
				if(piece.getSide() != side)
				{
					if(piece.getType() == PieceType.BISHOP || piece.getType() == PieceType.QUEEN)
					{
						return true;
					}
					else if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1 && Math.abs(pos.getY()-yPointer) == 1)
					{
					    return true;
					}
				}
				
				break;
			}
		}
		
		xPointer = pos.getX();
		yPointer = pos.getY();
		
		while(xPointer > 0 && yPointer > 0)
		{
			xPointer--;
			yPointer--;
			
			if(grid[xPointer][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].getPiece();
				
				if(piece.getSide() != side)
				{
					if(piece.getType() == PieceType.BISHOP || piece.getType() == PieceType.QUEEN)
					{
						return true;
					}
					else if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1 && Math.abs(pos.getY()-yPointer) == 1)
                    {
                        return true;
                    }
				}
				
				break;
			}
		}
		
		xPointer = pos.getX();
		yPointer = pos.getY();
		
		while(xPointer < 7 && yPointer > 0)
		{
			xPointer++;
			yPointer--;
			
			if(grid[xPointer][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].getPiece();
				
				if(piece.getSide() != side)
				{
					if(piece.getType() == PieceType.BISHOP || piece.getType() == PieceType.QUEEN)
					{
						return true;
					}
					else if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1 && Math.abs(pos.getY()-yPointer) == 1)
                    {
                        return true;
                    }
				}
				
				break;
			}
		}
		
		xPointer = pos.getX();
		yPointer = pos.getY();
		
		while(xPointer > 0 && yPointer < 7)
		{
			xPointer--;
			yPointer++;
			
			if(grid[xPointer][yPointer].getPiece() != null)
			{
				ChessPiece piece = grid[xPointer][yPointer].getPiece();
				
				if(piece.getSide() != side)
				{
					if(piece.getType() == PieceType.BISHOP || piece.getType() == PieceType.QUEEN)
					{
						return true;
					}
					else if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1 && Math.abs(pos.getY()-yPointer) == 1)
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
					if(square.getPiece().getType() == PieceType.KING && square.getPiece().getSide() == side)
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
	
	public static List<ChessPos> getValidatedMoves(ChessComponent chess, ChessSquare square)
	{
	    List<ChessPos> ret = new ArrayList<>();
	    
	    if(square.getPiece() == null) return ret;
	    
	    Piece piece = square.getPiece().getType().getPiece();
	    
	    for(ChessPos pos : piece.getCurrentPossibleMoves(chess, square.getPos()))
	    {
	        ChessMove move = new ChessMove(square.getPos(), pos);
	        
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
