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
}
