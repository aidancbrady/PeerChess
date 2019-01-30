package com.aidancbrady.peerchess.game;

import java.util.ArrayList;
import java.util.List;

import com.aidancbrady.peerchess.game.ChessPiece.Endgame;
import com.aidancbrady.peerchess.game.ChessPiece.Side;

public class ChessGame
{
    private Side side = Side.WHITE;
    private Side turn = Side.WHITE;
    private Endgame endgame = null;
    private Side sideInCheck = null;
    private List<ChessMove> moves = new ArrayList<ChessMove>();
    
    public Side getSide()
    {
        return side;
    }
    
    public void setSide(Side s)
    {
        side = s;
    }
    
    public Side getTurn()
    {
        return turn;
    }
    
    public void setTurn(Side s)
    {
        turn = s;
    }
    
    public Endgame getEndgame()
    {
        return endgame;
    }
    
    public void setEndgame(Endgame e)
    {
        endgame = e;
    }
    
    public Side getSideInCheck()
    {
        return sideInCheck;
    }
    
    public void setSideInCheck(Side s)
    {
        sideInCheck = s;
    }
    
    public List<ChessMove> getMoves()
    {
        return moves;
    }
    
    public void reset()
    {
        side = Side.WHITE;
        turn = Side.WHITE;
        
        endgame = null;
        sideInCheck = null;
        
        moves.clear();
    }
}
