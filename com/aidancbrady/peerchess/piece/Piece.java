package com.aidancbrady.peerchess.piece;

import java.util.Set;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessPos;
import com.aidancbrady.peerchess.ChessSquare;

public interface Piece 
{
	public boolean canMove(ChessSquare[][] grid, ChessMove move);
	
	public Set<ChessPos> getCurrentPossibleMoves(ChessSquare[][] grid, ChessPos origPos);
}
