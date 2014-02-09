package com.aidancbrady.openchess;

public class ChessMove 
{
	public ChessPos fromPos;
	public ChessPos toPos;
	
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
		if(getToSquare(grid).housedPiece != null && getToSquare(grid).housedPiece.side == getFromSquare(grid).housedPiece.side)
		{
			return false;
		}
		
		return Math.abs(fromPos.xPos-toPos.xPos) <= 1 && Math.abs(fromPos.yPos-toPos.yPos) <= 1;
	}
	
	public boolean isValidStraight(ChessSquare[][] grid)
	{
		ChessSquare fromSquare = getFromSquare(grid);
		ChessSquare toSquare = getToSquare(grid);
		
		if(toSquare.housedPiece != null)
		{
			if(toSquare.housedPiece.side == fromSquare.housedPiece.side)
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
						if(grid[toPos.xPos][y].housedPiece != null)
						{
							return false;
						}
					}
				}
			}
			else {
				while(y < toPos.yPos)
				{
					y--;
					
					if(y != toPos.yPos)
					{
						if(grid[toPos.xPos][y].housedPiece != null)
						{
							return false;
						}
					}
				}
			}
		}
		else if(fromPos.yPos == toPos.yPos)
		{
			int x = 0;
			
			if(fromPos.xPos < toPos.xPos)
			{
				while(fromPos.xPos < toPos.xPos)
				{
					x++;
					
					if(x != toPos.xPos)
					{
						if(grid[x][toPos.yPos].housedPiece != null)
						{
							return false;
						}
					}
				}
			}
			else {
				while(fromPos.xPos < toPos.xPos)
				{
					x--;
					
					if(x != toPos.xPos)
					{
						if(grid[x][toPos.yPos].housedPiece != null)
						{
							return false;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isValidDiagonal(ChessSquare[][] grid)
	{
		ChessSquare fromSquare = grid[fromPos.xPos][fromPos.yPos];
		ChessSquare toSquare = grid[fromPos.xPos][fromPos.yPos];
		
		if(toSquare.housedPiece != null)
		{
			if(toSquare.housedPiece.side == fromSquare.housedPiece.side)
			{
				return false;
			}
		}
		
		if(Math.abs(fromPos.xPos-toPos.yPos) == Math.abs(fromPos.yPos-toPos.yPos))
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
						if(grid[x][y].housedPiece != null)
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
						if(grid[x][y].housedPiece != null)
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
						if(grid[x][y].housedPiece != null)
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
						if(grid[x][y].housedPiece != null)
						{
							return false;
						}
					}
				}
			}
		}
		
		return false;
	}
}
