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
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof ChessPos && ((ChessPos)obj).xPos == xPos && ((ChessPos)obj).yPos == yPos;
	}
}
