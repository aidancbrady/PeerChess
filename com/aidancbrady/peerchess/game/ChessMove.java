package com.aidancbrady.peerchess.game;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.file.SaveHandler;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;

public class ChessMove 
{
	public ChessPos fromPos;
	public ChessPos toPos;
	
	public ChessPos fromPosCastle;
	public ChessPos toPosCastle;
	
	public ChessPos enPassantTakePos;
	
	public ChessPiece testFromPiece;
	public ChessPiece testToPiece;
	
	public ChessPiece testFromCastle;
	public ChessPiece testToCastle;
	
	public ChessPiece enPassantTake;
	
	public ChessSquare[][] boardPreMove;
	
	public ChessMove(ChessPos from, ChessPos to)
	{
		fromPos = from;
		toPos = to;
	}
	
	public ChessSquare getFromSquare(ChessSquare[][] grid)
	{
		return fromPos.getSquare(grid);
	}
	
	public ChessSquare getToSquare(ChessSquare[][] grid)
	{
		return toPos.getSquare(grid);
	}
	
	public boolean isValidStep(ChessSquare[][] grid)
	{
		if(getToSquare(grid).getPiece() != null && getToSquare(grid).getPiece().getSide() == getFromSquare(grid).getPiece().getSide())
		{
			return false;
		}
		
		return Math.abs(fromPos.getX()-toPos.getX()) <= 1 && Math.abs(fromPos.getY()-toPos.getY()) <= 1;
	}
	
	public boolean isValidStraight(ChessSquare[][] grid)
	{
		ChessSquare fromSquare = getFromSquare(grid);
		ChessSquare toSquare = getToSquare(grid);
		
		if(toSquare.getPiece() != null)
		{
			if(toSquare.getPiece().getSide() == fromSquare.getPiece().getSide())
			{
				return false;
			}
		}
		
		if(fromPos.getX() == toPos.getX())
		{
			int y = fromPos.getY();
			
			if(fromPos.getY() < toPos.getY())
			{
				while(y < toPos.getY())
				{
					y++;
					
					if(y != toPos.getY())
					{
						if(grid[toPos.getX()][y].getPiece() != null)
						{
							return false;
						}
					}
				}
			}
			else {
				while(y > toPos.getY())
				{
					y--;
					
					if(y != toPos.getY())
					{
						if(grid[toPos.getX()][y].getPiece() != null)
						{
							return false;
						}
					}
				}
			}
		}
		else if(fromPos.getY() == toPos.getY())
		{
			int x = fromPos.getX();
			
			if(fromPos.getX() < toPos.getX())
			{
				while(x < toPos.getX())
				{
					x++;
					
					if(x != toPos.getX())
					{
						if(grid[x][toPos.getY()].getPiece() != null)
						{
							return false;
						}
					}
				}
			}
			else {
				while(x > toPos.getX())
				{
					x--;
					
					if(x != toPos.getX())
					{
						if(grid[x][toPos.getY()].getPiece() != null)
						{
							return false;
						}
					}
				}
			}
		}
		else {
			return false;
		}
		
		return true;
	}
	
	public boolean isValidDiagonal(ChessSquare[][] grid)
	{
		ChessSquare fromSquare = getFromSquare(grid);
		ChessSquare toSquare = getToSquare(grid);
		
		if(toSquare.getPiece() != null)
		{
			if(toSquare.getPiece().getSide() == fromSquare.getPiece().getSide())
			{
				return false;
			}
		}
		
		if(Math.abs(fromPos.getX()-toPos.getX()) == Math.abs(fromPos.getY()-toPos.getY()))
		{
			int xDiff = fromPos.getX()-toPos.getX();
			int yDiff = fromPos.getY()-toPos.getY();
			
			int x = fromPos.getX();
			int y = fromPos.getY();
			
			if(xDiff > 0 && yDiff > 0)
			{
				while(x > toPos.getX() && y > toPos.getY())
				{
					x--;
					y--;
					
					if(x != toPos.getX() && y != toPos.getY())
					{
						if(grid[x][y].getPiece() != null)
						{
							return false;
						}
					}
				}
			}
			else if(xDiff < 0 && yDiff < 0)
			{
				while(x < toPos.getX() && y < toPos.getY())
				{
					x++;
					y++;
					
					if(x != toPos.getX() && y != toPos.getY())
					{
						if(grid[x][y].getPiece() != null)
						{
							return false;
						}
					}
				}
			}
			else if(xDiff > 0 && yDiff < 0)
			{
				while(x > toPos.getX() && y < toPos.getY())
				{
					x--;
					y++;
					
					if(x != toPos.getX() && y != toPos.getY())
					{
						if(grid[x][y].getPiece() != null)
						{
							return false;
						}
					}
				}
			}
			else if(xDiff < 0 && yDiff > 0)
			{
				while(x < toPos.getX() && y > toPos.getY())
				{
					x++;
					y--;
					
					if(x != toPos.getX() && y != toPos.getY())
					{
						if(grid[x][y].getPiece() != null)
						{
							return false;
						}
					}
				}
			}
		}
		else {
			return false;
		}
		
		return true;
	}
	
	public void testApplyMove(ChessSquare[][] grid)
	{
	    testFromPiece = getFromSquare(grid).getPiece();
	    testToPiece = getToSquare(grid).getPiece();
	    
	    getToSquare(grid).setPiece(getFromSquare(grid).getPiece());
        getFromSquare(grid).setPiece(null);
        
        if(fromPosCastle != null)
        {
            testFromCastle = fromPosCastle.getSquare(grid).getPiece();
            testToCastle = toPosCastle.getSquare(grid).getPiece();
            
            toPosCastle.getSquare(grid).setPiece(fromPosCastle.getSquare(grid).getPiece());
            fromPosCastle.getSquare(grid).setPiece(null);
        }
        
        if(enPassantTakePos != null)
        {
            enPassantTake = enPassantTakePos.getSquare(grid).getPiece();
        }
	}
	
	public boolean testTakingKing()
	{
	    return testToPiece != null && testToPiece.getType() == PieceType.KING;
	}
	
	public void testRevertMove(ChessSquare[][] grid)
	{
	    getFromSquare(grid).setPiece(testFromPiece);
	    getToSquare(grid).setPiece(testToPiece);
	    
	    if(fromPosCastle != null)
	    {
	        fromPosCastle.getSquare(grid).setPiece(testFromCastle);
	        toPosCastle.getSquare(grid).setPiece(testToCastle);
	    }
	    
	    if(enPassantTakePos != null)
	    {
	        enPassantTakePos.getSquare(grid).setPiece(enPassantTake);
	    }
	    
	    testFromPiece = testToPiece = testFromCastle = testToCastle = enPassantTake = null;
	}
	
	public boolean doRevertMove(IChessGame game)
	{
	    if(boardPreMove != null)
	    {
	        PeerUtils.applyBoard(boardPreMove, game.getGrid());
	        return true;
	    }
	    
	    return false;
	}
	
	public boolean didTakeMaterial()
	{
	    if(enPassantTakePos != null) return true;
	    return boardPreMove != null ? boardPreMove[toPos.getX()][toPos.getY()].getPiece() != null : testToPiece != null;
	}
	
	public boolean isPawnMove()
	{
	    return boardPreMove != null ? boardPreMove[fromPos.getX()][fromPos.getY()].getPiece().getType() == PieceType.PAWN : testFromPiece.getType() == PieceType.PAWN;
	}
	
	public String serialize()
	{
	    String ret = fromPos.serialize() + " " + toPos.serialize() + " " + SaveHandler.saveChessBoard(boardPreMove);
	    
	    if(fromPosCastle != null)
	    {
	        ret += " " + fromPosCastle.serialize() + " " + toPosCastle.serialize();
	    }
	    
	    if(enPassantTakePos != null)
	    {
	        ret += " " + enPassantTakePos.serialize();
	    }
	    
	    return ret;
	}
	
	public static ChessMove create(String s) throws Exception
	{
	    String[] split = s.split(" ");
	    
	    ChessMove ret = new ChessMove(ChessPos.create(split[0]), ChessPos.create(split[1]));
	    ret.boardPreMove = SaveHandler.loadChessBoard(split[2]);
	    
	    if(split.length == 5)
	    {
	        ret.fromPosCastle = ChessPos.create(split[3]);
	        ret.toPosCastle = ChessPos.create(split[4]);
	    }
	    
	    if(split.length == 4)
	    {
	        ret.enPassantTakePos = ChessPos.create(split[2]);
	    }
	    
	    return ret;
	}

    @Override
    public int hashCode() 
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + fromPos.hashCode();
        result = prime * result + toPos.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        
        ChessMove other = (ChessMove)obj;
        return other.fromPos.equals(fromPos) && other.toPos.equals(toPos);
    }
}
