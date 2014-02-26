package com.aidancbrady.peerchess.piece;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessPos;
import com.aidancbrady.peerchess.ChessSquare;
import com.aidancbrady.peerchess.PeerUtils;

public class PieceBishop implements Piece
{
	@Override
	public boolean canMove(ChessSquare[][] grid, ChessMove move)
	{
		ChessPos pos = PeerUtils.findKing(move.getFromSquare(grid).housedPiece.side, grid);
		
		if(PeerUtils.isInCheck(move.getFromSquare(grid).housedPiece.side, pos, move.getFakeGrid(grid)))
		{
			return false;
		}
		
		return move.isValidDiagonal(grid);
	}
}
