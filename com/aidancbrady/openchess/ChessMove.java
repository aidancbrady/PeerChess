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
	
	public boolean isValidStraight(ChessSquare[][] grid)
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
		
		if(fromPos.xPos == toPos.xPos)
		{
			int y = 0;
			
			if(fromPos.yPos < toPos.yPos)
			{
				while(fromPos.yPos < toPos.yPos)
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
				while(fromPos.yPos < toPos.yPos)
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
		if(Math.abs(fromPos.xPos-toPos.yPos) == Math.abs(fromPos.yPos-toPos.yPos))
		{
			int xDiff = fromPos.xPos-toPos.xPos;
			int yDiff = fromPos.yPos-toPos.yPos;
			
			if(xDiff > 0 && yDiff > 0)
			{
				
			}
			else if(xDiff < 0 && yDiff < 0)
			{
				
			}
			else if(xDiff > 0 && yDiff < 0)
			{
				
			}
			else if(xDiff < 0 && yDiff > 0)
			{
				
			}
		}
		
		return false;
	}
}
