package com.aidancbrady.peerchess.piece;

import java.util.Set;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessPos;
import com.aidancbrady.peerchess.ChessSquare;
import com.aidancbrady.peerchess.PeerUtils;

public class PieceKnight implements Piece
{
	@Override
	public boolean canMove(ChessSquare[][] grid, ChessMove move)
	{
		ChessPos pos = PeerUtils.findKing(move.getFromSquare(grid).housedPiece.side, grid);
		
		if(PeerUtils.isInCheck(move.getFromSquare(grid).housedPiece.side, pos, grid))
		{
			return false;
		}
		
		Set<ChessPos> validDests = PeerUtils.getValidKnightMoves(move.fromPos);
		
		if(validDests.contains(move.toPos))
		{
			return true;
		}
		
		return false;
	}
}
