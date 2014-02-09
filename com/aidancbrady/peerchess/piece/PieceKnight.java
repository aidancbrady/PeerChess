package com.aidancbrady.peerchess.piece;

import java.util.HashSet;
import java.util.Set;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessPos;
import com.aidancbrady.peerchess.ChessSquare;

public class PieceKnight implements Piece
{
	@Override
	public boolean canMove(ChessSquare[][] grid, ChessMove move)
	{
		Set<ChessPos> validDests = new HashSet<ChessPos>();
		
		validDests.add(new ChessPos(move.fromPos.xPos+2, move.fromPos.yPos+1));
		validDests.add(new ChessPos(move.fromPos.xPos+2, move.fromPos.yPos-1));
		
		validDests.add(new ChessPos(move.fromPos.xPos-2, move.fromPos.yPos+1));
		validDests.add(new ChessPos(move.fromPos.xPos-2, move.fromPos.yPos-1));
		
		validDests.add(new ChessPos(move.fromPos.xPos+1, move.fromPos.yPos+2));
		validDests.add(new ChessPos(move.fromPos.xPos-1, move.fromPos.yPos+2));
		
		validDests.add(new ChessPos(move.fromPos.xPos+1, move.fromPos.yPos-2));
		validDests.add(new ChessPos(move.fromPos.xPos-1, move.fromPos.yPos-2));
		
		if(validDests.contains(move.toPos))
		{
			return true;
		}
		
		return false;
	}
}
