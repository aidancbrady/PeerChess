package com.aidancbrady.peerchess.piece;

import java.util.Set;

import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;

public interface Piece 
{
	public boolean validateMove(ChessSquare[][] grid, ChessMove move);
	
	public Set<ChessPos> getCurrentPossibleMoves(ChessSquare[][] grid, ChessPos origPos);
	
	public int getPointValue();
}
