package com.aidancbrady.peerchess.game;

import com.aidancbrady.peerchess.ChessComponent;
import com.aidancbrady.peerchess.game.ChessPiece.Endgame;
import com.aidancbrady.peerchess.game.ChessPiece.Side;

public class ChessGame
{
    public Side side = Side.WHITE;
    public Side turn = Side.WHITE;
    public Endgame endgame = null;
    
    public Side sideInCheck = null;
    
    public ChessComponent component;
    
    public ChessGame(ChessComponent c)
    {
        component = c;
    }
    
    public void setSide(Side s)
    {
        side = s;
    }
    
    public void reset()
    {
        side = Side.WHITE;
        turn = Side.WHITE;
        
        endgame = null;
        sideInCheck = null;
    }
}
