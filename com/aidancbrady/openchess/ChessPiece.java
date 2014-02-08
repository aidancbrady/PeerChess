package com.aidancbrady.openchess;

import com.aidancbrady.openchess.piece.Piece;
import com.aidancbrady.openchess.piece.PieceBishop;
import com.aidancbrady.openchess.piece.PieceCastle;
import com.aidancbrady.openchess.piece.PieceKing;
import com.aidancbrady.openchess.piece.PieceKnight;
import com.aidancbrady.openchess.piece.PiecePawn;
import com.aidancbrady.openchess.piece.PieceQueen;
import com.aidancbrady.openchess.tex.Texture;

public class ChessPiece 
{
	public static enum PieceType
	{
		PAWN(new PiecePawn()),
		CASTLE(new PieceCastle()),
		BISHOP(new PieceBishop()),
		KNIGHT(new PieceKnight()),
		QUEEN(new PieceQueen()),
		KING(new PieceKing());
		
		private Piece piece;
		
		private PieceType(Piece p)
		{
			piece = p;
		}
		
		public Piece getPiece()
		{
			return piece;
		}
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
