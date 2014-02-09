package com.aidancbrady.peerchess.piece;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessSquare;

public class PieceBishop implements Piece
{
	@Override
	public boolean canMove(ChessSquare[][] grid, ChessMove move)
	{
		return move.isValidDiagonal(grid);
	}
}
