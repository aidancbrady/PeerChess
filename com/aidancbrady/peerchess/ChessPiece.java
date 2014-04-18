package com.aidancbrady.peerchess;

import java.util.ArrayList;
import java.util.List;

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
		WHITE("White"),
		BLACK("Black");
		
		public String name;
		
		private Side(String s)
		{
			name = s;
		}
		
		public Side getOpposite()
		{
			return this == WHITE ? BLACK : WHITE;
		}
	}
	
	private static List<ChessPiece> cachedWhitePieces = new ArrayList<ChessPiece>();
	private static List<ChessPiece> cachedBlackPieces = new ArrayList<ChessPiece>();
	
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
	
	public static List<ChessPiece> getPieceList(Side s)
	{
		if(s == Side.BLACK && cachedBlackPieces != null)
		{
			return cachedBlackPieces;
		}
		else if(s == Side.WHITE && cachedWhitePieces != null)
		{
			return cachedWhitePieces;
		}
		
		List<ChessPiece> ret = new ArrayList<ChessPiece>();
		
		for(PieceType type : PieceType.values())
		{
			ret.add(new ChessPiece(type, s));
		}
		
		if(s == Side.BLACK)
		{
			cachedBlackPieces = ret;
		}
		else if(s == Side.WHITE)
		{
			cachedWhitePieces = ret;
		}
		
		return ret;
	}
}
