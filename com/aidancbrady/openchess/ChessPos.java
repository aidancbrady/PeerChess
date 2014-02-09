package com.aidancbrady.openchess;

public class ChessPos 
{
	public int xPos;
	public int yPos;
	
	public ChessPos(int x, int y)
	{
		xPos = x;
		yPos = y;
	}
	
	public ChessPos translate(int x, int y)
	{
		return new ChessPos(xPos+x, yPos+y);
	}
	
	public ChessSquare getSquare(ChessSquare[][] grid)
	{
		return grid[xPos][yPos];
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof ChessPos && ((ChessPos)obj).xPos == xPos && ((ChessPos)obj).yPos == yPos;
	}
	
	@Override
	public String toString()
	{
		return "ChessPos[" + xPos + ", " + yPos + "]";
	}
}
