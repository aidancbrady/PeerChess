package com.aidancbrady.peerchess;

import java.util.List;

import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessSquare;

public interface IChessGame 
{
    public ChessSquare[][] getGrid();
    
    public List<ChessMove> getPastMoves();
}
