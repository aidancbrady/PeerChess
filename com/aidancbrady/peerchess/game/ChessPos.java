package com.aidancbrady.peerchess.game;

public class ChessPos
{
	private int xPos;
	private int yPos;
	
	public ChessPos(int x, int y)
	{
		xPos = x;
		yPos = y;
	}
	
	public int getX()
	{
	    return xPos;
	}
	
	public int getY()
	{
	    return yPos;
	}
	
	public ChessPos translate(int x, int y)
	{
		return new ChessPos(xPos+x, yPos+y);
	}
	
	public ChessSquare getSquare(ChessSquare[][] grid)
	{
		return grid[xPos][yPos];
	}
	
	public boolean isInRange()
	{
		return xPos >= 0 && xPos <= 7 && yPos >= 0 && yPos <= 7;
	}
	
	public String serialize()
	{
	    return xPos + "," + yPos;
	}
	
	public static ChessPos create(String s) throws Exception
	{
	    String[] split = s.split(",");
	    return new ChessPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
	}
	
	@Override
	public ChessPos clone()
	{
		return new ChessPos(xPos, yPos);
	}
	
	@Override
	public int hashCode() 
	{
		int code = 1;
		code = 31 * code + xPos;
		code = 31 * code + yPos;
		return code;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof ChessPos && ((ChessPos)obj).getX() == xPos && ((ChessPos)obj).getY() == yPos;
	}
	
	@Override
	public String toString()
	{
		return "ChessPos[" + xPos + ", " + yPos + "]";
	}
}
