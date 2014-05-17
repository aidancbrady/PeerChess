package com.aidancbrady.peerchess.piece;

import java.util.Iterator;
import java.util.Set;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessPos;
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
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(ChessSquare[][] grid, ChessPos origPos)
	{
		Set<ChessPos> possibleMoves = PeerUtils.getValidStepMoves(origPos);
		
		for(Iterator<ChessPos> iter = possibleMoves.iterator(); iter.hasNext();)
		{
			ChessPos pos = iter.next();
			
			if(pos.getSquare(grid).housedPiece != null && pos.getSquare(grid).housedPiece.side == origPos.getSquare(grid).housedPiece.side)
			{
				iter.remove();
			}
		}
		
		return possibleMoves;
	}
}
