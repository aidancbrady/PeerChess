package com.aidancbrady.openchess.piece;

import com.aidancbrady.openchess.ChessMove;
import com.aidancbrady.openchess.ChessPiece.Side;
import com.aidancbrady.openchess.ChessPos;
import com.aidancbrady.openchess.ChessSquare;

public class PiecePawn implements Piece
{
	@Override
	public boolean canMove(ChessSquare[][] grid, ChessMove move)
	{
		Side side = move.getFromSquare(grid).housedPiece.side;
		int yStart = side == Side.BLACK ? 1 : 6;
		
		ChessPos left = move.fromPos.translate(1, side == Side.BLACK ? 1 : -1);
		ChessPos right = move.fromPos.translate(-1, side == Side.BLACK ? 1 : -1);
		
		if(move.fromPos.xPos == move.toPos.xPos)
		{
			if(move.getToSquare(grid).housedPiece != null)
			{
				return false;
			}
		}
		
		if(side == Side.BLACK && move.fromPos.yPos-move.toPos.yPos > 0)
		{
			return false;
		}
		else if(side == Side.WHITE && move.fromPos.yPos-move.toPos.yPos < 0)
		{
			return false;
		}
		
		int yAbs = Math.abs(move.fromPos.yPos-move.toPos.yPos);
		
		if(move.fromPos.yPos == yStart && (yAbs != 1 && yAbs != 2))
		{
			return false;
		}
		else if(move.fromPos.yPos != yStart && yAbs != 1)
		{
			return false;
		}
		
		if(move.toPos.equals(left) || move.toPos.equals(right))
		{
			if(move.getToSquare(grid).housedPiece == null || (move.getToSquare(grid).housedPiece != null && move.getToSquare(grid).housedPiece.side == move.getFromSquare(grid).housedPiece.side))
			{
				return false;
			}
		}
		
		return true;
	}
}
