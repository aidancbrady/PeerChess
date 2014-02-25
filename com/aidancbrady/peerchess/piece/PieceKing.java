package com.aidancbrady.peerchess.piece;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessSquare;

public class PieceKing implements Piece
{
	@Override
	public boolean canMove(ChessSquare[][] grid, ChessMove move)
	{
		if(move.isValidStep(grid))
		{
			
		}
		
		return false;
	}
}
