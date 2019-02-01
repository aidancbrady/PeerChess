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
	public ChessPiece enPassantTake;
	
	public ChessPiece testFromPiece;
	public ChessPiece testToPiece;
	
	public ChessPiece testFromCastle;
	public ChessPiece testToCastle;
	
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
	
	public void testApplyMove(IChessGame game)
	{
	    testFromPiece = getFromSquare(game.getGrid()).getPiece();
	    testToPiece = getToSquare(game.getGrid()).getPiece();
	    
	    getToSquare(game.getGrid()).setPiece(getFromSquare(game.getGrid()).getPiece());
        getFromSquare(game.getGrid()).setPiece(null);
        
        if(fromPosCastle != null)
        {
            testFromCastle = fromPosCastle.getSquare(game.getGrid()).getPiece();
            testToCastle = toPosCastle.getSquare(game.getGrid()).getPiece();
            
            toPosCastle.getSquare(game.getGrid()).setPiece(fromPosCastle.getSquare(game.getGrid()).getPiece());
            fromPosCastle.getSquare(game.getGrid()).setPiece(null);
        }
        
        if(enPassantTakePos != null)
        {
            enPassantTake = enPassantTakePos.getSquare(game.getGrid()).getPiece();
        }
        
        game.getCache().applyMove(this);
	}
	
	public boolean testTakingKing()
	{
	    return testToPiece != null && testToPiece.getType() == PieceType.KING;
	}
	
	public void testRevertMove(IChessGame game)
	{
	    getFromSquare(game.getGrid()).setPiece(testFromPiece);
	    getToSquare(game.getGrid()).setPiece(testToPiece);
	    
	    if(fromPosCastle != null)
	    {
	        fromPosCastle.getSquare(game.getGrid()).setPiece(testFromCastle);
	        toPosCastle.getSquare(game.getGrid()).setPiece(testToCastle);
	    }
	    
	    if(enPassantTakePos != null)
	    {
	        enPassantTakePos.getSquare(game.getGrid()).setPiece(enPassantTake);
	    }
	    
	    testFromPiece = testToPiece = testFromCastle = testToCastle = enPassantTake = null;
	    
	    game.getCache().revertMove(this);
	}
	
	public boolean doRevertMove(IChessGame game)
	{
	    if(boardPreMove != null)
	    {
	        PeerUtils.applyBoard(boardPreMove, game.getGrid());
	        game.getCache().revertMove(this);
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
