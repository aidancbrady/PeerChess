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
		if(fromPos.xPos == toPos.xPos)
		{
			
		}
		else if(fromPos.yPos == toPos.yPos)
		{
			
		}
		
		return false;
	}
	
	public boolean isValidDiagonal(ChessSquare[][] grid)
	{
		return false;
	}
}
