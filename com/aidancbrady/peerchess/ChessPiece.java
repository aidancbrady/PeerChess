package com.aidancbrady.peerchess;

import com.aidancbrady.peerchess.piece.Piece;
import com.aidancbrady.peerchess.piece.PieceBishop;
import com.aidancbrady.peerchess.piece.PieceCastle;
import com.aidancbrady.peerchess.piece.PieceKing;
import com.aidancbrady.peerchess.piece.PieceKnight;
import com.aidancbrady.peerchess.piece.PiecePawn;
import com.aidancbrady.peerchess.piece.PieceQueen;
import com.aidancbrady.peerchess.tex.Texture;

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
		return "resources/piece/" + side.name().toLowerCase() + "_" + type.name().toLowerCase() + ".png";
	}
}
