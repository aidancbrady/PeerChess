package com.aidancbrady.openchess;

public class ChessPiece 
{
	public static enum PieceType
	{
		PAWN,
		CASTLE,
		BISHUP,
		KNIGHT,
		QUEEN,
		KING
	}
	
	public static enum Side
	{
		WHITE,
		BLACK
	}
	
	public PieceType type;
	
	public Side side;
	
	public ChessPiece(PieceType t, Side s)
	{
		type = t;
		side = s;
	}
}
