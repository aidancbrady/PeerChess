package com.aidancbrady.openchess.piece;

import com.aidancbrady.openchess.ChessMove;
import com.aidancbrady.openchess.ChessSquare;

public class PieceCastle implements Piece
{
	@Override
	public boolean canMove(ChessSquare[][] grid, ChessMove move)
	{
		return move.isValidStraight(grid);
	}
}
