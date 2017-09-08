package com.aidancbrady.peerchess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static Map<PieceType, Texture> whiteTextures = new HashMap<>();
    private static Map<PieceType, Texture> blackTextures = new HashMap<>();
	
	private static List<ChessPiece> cachedWhitePieces = new ArrayList<ChessPiece>();
	private static List<ChessPiece> cachedBlackPieces = new ArrayList<ChessPiece>();
	
	static {
	    for(PieceType type : PieceType.values())
        {
            cachedWhitePieces.add(new ChessPiece(type, Side.WHITE));
            cachedBlackPieces.add(new ChessPiece(type, Side.BLACK));
        }
	}
	
	public PieceType type;
	
	public Side side;
	
	public int moves;
	
	public ChessPiece(PieceType t, Side s)
	{
		type = t;
		side = s;
	}
	
	public static List<ChessPiece> getPieceList(Side s)
	{
	    return s == Side.WHITE ? cachedWhitePieces : cachedBlackPieces;
	}
	
	public void move()
	{
	    moves++;
	}
	
	public ChessPiece copyWithMoves(int newMoves)
	{
	    ChessPiece piece = new ChessPiece(type, side);
	    piece.moves = newMoves;
	    return piece;
	}
	
	public Texture getTexture()
	{
	    if(side == Side.WHITE && whiteTextures.containsKey(type))
	    {
	        return whiteTextures.get(type);
	    }
	    else if(side == Side.BLACK && blackTextures.containsKey(type))
	    {
	        return blackTextures.get(type);
	    }
	    
	    Texture tex = Texture.load(type.getTexturePath(side));
	    
	    if(side == Side.WHITE)
	    {
	        whiteTextures.put(type, tex);
	    }
	    else {
	        blackTextures.put(type, tex);
	    }
	    
	    return tex;
	}
	
	@Override
	public String toString()
	{
	    return type.toString();
	}
	
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

        public String getTexturePath(Side side) 
        {
            return "resources/piece/" + side.name().toLowerCase() + "_" + name().toLowerCase() + ".png";
        }

        public Piece getPiece()
        {
            return piece;
        }
    }

    public static enum Side 
    {
        WHITE("White"), BLACK("Black");

        public String name;

        private Side(String s) 
        {
            name = s;
        }

        public Side getOpposite() 
        {
            return this == WHITE ? BLACK : WHITE;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
    
    public static enum Endgame
    {
        WHITE_WIN,
        BLACK_WIN,
        STALEMATE;
        
        public static Endgame get(Side side)
        {
            return side == Side.WHITE ? WHITE_WIN : BLACK_WIN;
        }
    }
}
