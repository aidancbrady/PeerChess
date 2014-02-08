package com.aidancbrady.openchess.piece;

import com.aidancbrady.openchess.ChessMove;
import com.aidancbrady.openchess.ChessSquare;

public interface Piece 
{
	public boolean canMove(ChessSquare[][] grid, ChessMove move);
}
