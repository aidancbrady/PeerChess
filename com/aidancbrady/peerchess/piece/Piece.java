package com.aidancbrady.peerchess.piece;

import java.util.Set;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPos;

public interface Piece 
{
	public boolean validateMove(IChessGame game, ChessMove move);
	
	public Set<ChessPos> getCurrentPossibleMoves(IChessGame game, ChessPos origPos);
	
	public int getPointValue();
}
