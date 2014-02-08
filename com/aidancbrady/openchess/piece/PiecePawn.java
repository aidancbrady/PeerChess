package com.aidancbrady.openchess.piece;

import com.aidancbrady.openchess.ChessMove;
import com.aidancbrady.openchess.ChessSquare;

public class PiecePawn implements Piece
{
	@Override
	public boolean canMove(ChessSquare[][] grid, ChessMove move)
	{
		return false;
	}
}
