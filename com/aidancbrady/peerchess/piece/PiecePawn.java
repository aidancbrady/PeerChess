package com.aidancbrady.peerchess.piece;

import java.util.HashSet;
import java.util.Set;

import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.PeerUtils;

public class PiecePawn implements Piece
{
	@Override
	public boolean validateMove(ChessSquare[][] grid, ChessMove move)
	{
		ChessPos pos = PeerUtils.findKing(move.getFromSquare(grid).getPiece().side, grid);
		
		if(PeerUtils.isInCheck(move.getFromSquare(grid).getPiece().side, pos, move.getFakeGrid(grid)))
		{
			return false;
		}
		
		Side side = move.getFromSquare(grid).getPiece().side;
		int yStart = side == Side.BLACK ? 1 : 6;
		
		ChessPos left = move.fromPos.translate(1, side == Side.BLACK ? 1 : -1);
		ChessPos right = move.fromPos.translate(-1, side == Side.BLACK ? 1 : -1);
		
		if(move.fromPos.xPos == move.toPos.xPos)
		{
			if(move.getToSquare(grid).getPiece() != null)
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
		
		int xAbs = Math.abs(move.fromPos.xPos-move.toPos.xPos);
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
			if(move.getToSquare(grid).getPiece() == null || (move.getToSquare(grid).getPiece() != null && move.getToSquare(grid).getPiece().side == move.getFromSquare(grid).getPiece().side))
			{
				return false;
			}
		}
		
		return move.equals(left) || move.equals(right) || xAbs <= 1;
	}
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(ChessSquare[][] grid, ChessPos origPos)
	{
		Set<ChessPos> possibleMoves = new HashSet<ChessPos>();
		
		Side side = origPos.getSquare(grid).getPiece().side;
		int yStart = side == Side.BLACK ? 1 : 6;
		
		ChessPos left = origPos.translate(1, side == Side.BLACK ? 1 : -1);
		ChessPos right = origPos.translate(-1, side == Side.BLACK ? 1 : -1);
		
		if(left.isInRange() && left.getSquare(grid).getPiece() != null && left.getSquare(grid).getPiece().side != side)
		{
			possibleMoves.add(left);
		}
		
		if(right.isInRange() && right.getSquare(grid).getPiece() != null && right.getSquare(grid).getPiece().side != side)
		{
			possibleMoves.add(right);
		}
		
		if(origPos.yPos == yStart)
		{
			int forwardY = side == Side.BLACK ? 3 : 4;
			
			if(grid[origPos.xPos][forwardY].getPiece() == null)
			{
				possibleMoves.add(new ChessPos(origPos.xPos, forwardY));
			}
		}
		
		int forwardY = side == Side.BLACK ? origPos.yPos+1 : origPos.yPos-1;
		
		if(forwardY >= 0 && forwardY <= 7 && grid[origPos.xPos][forwardY].getPiece() == null)
		{
			possibleMoves.add(new ChessPos(origPos.xPos, forwardY));
		}
		
		return possibleMoves;
	}
	
	@Override
	public int getPointValue()
	{
	    return 10;
	}
}
