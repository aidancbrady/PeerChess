package com.aidancbrady.peerchess.piece;

import java.util.Iterator;
import java.util.Set;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessPos;
import com.aidancbrady.peerchess.ChessSquare;
import com.aidancbrady.peerchess.PeerUtils;

public class PieceKnight implements Piece
{
	@Override
	public boolean validateMove(ChessSquare[][] grid, ChessMove move)
	{
		ChessPos pos = PeerUtils.findKing(move.getFromSquare(grid).housedPiece.side, grid);
		
		if(PeerUtils.isInCheck(move.getFromSquare(grid).housedPiece.side, pos, move.getFakeGrid(grid)))
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
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(ChessSquare[][] grid, ChessPos origPos)
	{
		Set<ChessPos> possibleMoves = PeerUtils.getValidKnightMoves(origPos);
		
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
	
	@Override
	public int getPointValue()
	{
	    return 30;
	}
}
