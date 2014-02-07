package com.aidancbrady.openchess;

import com.aidancbrady.openchess.tex.Texture;

public class ChessPiece 
{
	public static enum PieceType
	{
		PAWN,
		CASTLE,
		BISHOP,
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
	
	public Texture texture;
	
	public Side side;
	
	public ChessPiece(PieceType t, Side s)
	{
		type = t;
		side = s;
		
		texture = Texture.load(getTexturePath());
	}
	
	public String getTexturePath()
	{
		return "src/resources/piece/" + side.name().toLowerCase() + "_" + type.name().toLowerCase() + ".png";
	}
}
