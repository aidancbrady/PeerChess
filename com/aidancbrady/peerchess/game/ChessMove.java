package com.aidancbrady.peerchess.game;

public class ChessMove 
{
	public ChessPos fromPos;
	public ChessPos toPos;
	
	public ChessPos fromPosCastle;
	public ChessPos toPosCastle;
	
	public ChessMove(ChessPos from, ChessPos to)
	{
		fromPos = from;
		toPos = to;
	}
	
	public void setCastle(ChessPos from, ChessPos to)
	{
	    fromPosCastle = from;
	    toPosCastle = to;
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
	
	public ChessSquare[][] getFakeGrid(ChessSquare[][] grid)
	{
		ChessSquare[][] fakeGrid = new ChessSquare[8][8];
		
		for(int x = 0; x < 8; x++)
		{
			for(int y = 0; y < 8; y++)
			{
				fakeGrid[x][y] = new ChessSquare(false, new ChessPos(x, y));
				fakeGrid[x][y].setPiece(grid[x][y].getPiece());
			}
		}
		
		getFromSquare(fakeGrid).setPiece(null);
		getToSquare(fakeGrid).setPiece(getFromSquare(grid).getPiece());
		
		if(fromPosCastle != null)
		{
		    fromPosCastle.getSquare(fakeGrid).setPiece(null);
		    toPosCastle.getSquare(fakeGrid).setPiece(fromPosCastle.getSquare(grid).getPiece());
		}
		
		return fakeGrid;
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