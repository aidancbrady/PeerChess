package com.aidancbrady.peerchess.game;

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
		if(getToSquare(grid).getPiece() != null && getToSquare(grid).getPiece().side == getFromSquare(grid).getPiece().side)
		{
			return false;
		}
		
		return Math.abs(fromPos.xPos-toPos.xPos) <= 1 && Math.abs(fromPos.yPos-toPos.yPos) <= 1;
	}
	
	public boolean isValidStraight(ChessSquare[][] grid)
	{
		ChessSquare fromSquare = getFromSquare(grid);
		ChessSquare toSquare = getToSquare(grid);
		
		if(toSquare.getPiece() != null)
		{
			if(toSquare.getPiece().side == fromSquare.getPiece().side)
			{
				return false;
			}
		}
		
		if(fromPos.xPos == toPos.xPos)
		{
			int y = fromPos.yPos;
			
			if(fromPos.yPos < toPos.yPos)
			{
				while(y < toPos.yPos)
				{
					y++;
					
					if(y != toPos.yPos)
					{
						if(grid[toPos.xPos][y].getPiece() != null)
						{
							return false;
						}
					}
				}
			}
			else {
				while(y > toPos.yPos)
				{
					y--;
					
					if(y != toPos.yPos)
					{
						if(grid[toPos.xPos][y].getPiece() != null)
						{
							return false;
						}
					}
				}
			}
		}
		else if(fromPos.yPos == toPos.yPos)
		{
			int x = fromPos.xPos;
			
			if(fromPos.xPos < toPos.xPos)
			{
				while(x < toPos.xPos)
				{
					x++;
					
					if(x != toPos.xPos)
					{
						if(grid[x][toPos.yPos].getPiece() != null)
						{
							return false;
						}
					}
				}
			}
			else {
				while(x > toPos.xPos)
				{
					x--;
					
					if(x != toPos.xPos)
					{
						if(grid[x][toPos.yPos].getPiece() != null)
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
			if(toSquare.getPiece().side == fromSquare.getPiece().side)
			{
				return false;
			}
		}
		
		if(Math.abs(fromPos.xPos-toPos.xPos) == Math.abs(fromPos.yPos-toPos.yPos))
		{
			int xDiff = fromPos.xPos-toPos.xPos;
			int yDiff = fromPos.yPos-toPos.yPos;
			
			int x = fromPos.xPos;
			int y = fromPos.yPos;
			
			if(xDiff > 0 && yDiff > 0)
			{
				while(x > toPos.xPos && y > toPos.yPos)
				{
					x--;
					y--;
					
					if(x != toPos.xPos && y != toPos.yPos)
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
				while(x < toPos.xPos && y < toPos.yPos)
				{
					x++;
					y++;
					
					if(x != toPos.xPos && y != toPos.yPos)
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
				while(x > toPos.xPos && y < toPos.yPos)
				{
					x--;
					y++;
					
					if(x != toPos.xPos && y != toPos.yPos)
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
				while(x < toPos.xPos && y > toPos.yPos)
				{
					x++;
					y--;
					
					if(x != toPos.xPos && y != toPos.yPos)
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
	    return testToPiece != null && testToPiece.type == PieceType.KING;
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
	
	public String serialize()
	{
	    String ret = fromPos.serialize() + " " + toPos.serialize();
	    
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
	    
	    if(split.length == 4)
	    {
	        ret.fromPosCastle = ChessPos.create(split[2]);
	        ret.toPosCastle = ChessPos.create(split[3]);
	    }
	    
	    if(split.length == 3)
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
