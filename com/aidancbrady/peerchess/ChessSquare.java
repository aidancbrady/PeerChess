package com.aidancbrady.peerchess;


public class ChessSquare 
{
    private boolean color;
    
    private ChessPiece housedPiece;
    
    private ChessPos pos;   
    
    public ChessSquare(boolean c, ChessPos p)
    {
        color = c;
        pos = p;
    }
    
    public boolean isBlack()
    {
        return color;
    }
    
    public ChessPos getPos()
    {
        return pos;
    }
    
    public void setPiece(ChessPiece piece)
    {
        housedPiece = piece;
    }
    
    public ChessPiece getPiece()
    {
        return housedPiece;
    }
}
