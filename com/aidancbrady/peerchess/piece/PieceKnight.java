package com.aidancbrady.peerchess.piece;

import java.util.Iterator;
import java.util.Set;

import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;

public class PieceKnight implements Piece
{
	@Override
	public boolean validateMove(ChessSquare[][] grid, ChessMove move)
	{
		ChessPos pos = PeerUtils.findKing(move.getFromSquare(grid).getPiece().side, grid);
		
		if(PeerUtils.testCheck(move.getFromSquare(grid).getPiece().side, pos, grid, move))
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
			
			if(pos.getSquare(grid).getPiece() != null && pos.getSquare(grid).getPiece().side == origPos.getSquare(grid).getPiece().side)
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
