package com.aidancbrady.peerchess.piece;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessSquare;

public interface Piece 
{
	public boolean canMove(ChessSquare[][] grid, ChessMove move);
}
