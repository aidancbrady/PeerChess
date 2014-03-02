package com.aidancbrady.peerchess.piece;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessSquare;
import com.aidancbrady.peerchess.PeerUtils;

public class PieceKing implements Piece
{
	@Override
	public boolean canMove(ChessSquare[][] grid, ChessMove move)
	{
		if(move.isValidStep(grid))
		{
			if(!PeerUtils.isInCheck(move.getFromSquare(grid).housedPiece.side, move.toPos, move.getFakeGrid(grid)))
			{
				return true;
			}
		}
		
		return false;
	}
}
