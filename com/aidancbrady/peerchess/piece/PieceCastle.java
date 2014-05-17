package com.aidancbrady.peerchess.piece;

import java.util.HashSet;
import java.util.Set;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessPiece;
import com.aidancbrady.peerchess.ChessPos;
import com.aidancbrady.peerchess.ChessSquare;
import com.aidancbrady.peerchess.PeerUtils;

public class PieceCastle implements Piece
{
	@Override
	public boolean canMove(ChessSquare[][] grid, ChessMove move)
	{
		ChessPos pos = PeerUtils.findKing(move.getFromSquare(grid).housedPiece.side, grid);
		
		if(PeerUtils.isInCheck(move.getFromSquare(grid).housedPiece.side, pos, move.getFakeGrid(grid)))
		{
			return false;
		}
		
		return move.isValidStraight(grid);
	}
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(ChessSquare[][] grid, ChessPos origPos)
	{
		Set<ChessPos> ret = new HashSet<ChessPos>();
		
		int x = origPos.xPos;
		int y = origPos.yPos;
		
		if(x != 7)
		{
			for(x = x+1; x <= 7; x++)
			{
				ChessPiece piece = grid[x][y].housedPiece;
				
				if(piece != null && piece.side == origPos.getSquare(grid).housedPiece.side)
				{
					break;
				}
				
				ret.add(new ChessPos(x, y));
				
				if(piece != null)
				{
					break;
				}
			}
		}
		
		x = origPos.xPos;
		y = origPos.yPos;
		
		if(x != 0)
		{
			for(x = x-1; x >= 0; x--)
			{
				ChessPiece piece = grid[x][y].housedPiece;
				
				if(piece != null && piece.side == origPos.getSquare(grid).housedPiece.side)
				{
					break;
				}
				
				ret.add(new ChessPos(x, y));
				
				if(piece != null)
				{
					break;
				}
			}
		}
		
		x = origPos.xPos;
		y = origPos.yPos;
		
		if(y != 7)
		{
			for(y = y+1; y <= 7; y++)
			{
				ChessPiece piece = grid[x][y].housedPiece;
				
				if(piece != null && piece.side == origPos.getSquare(grid).housedPiece.side)
				{
					break;
				}
				
				ret.add(new ChessPos(x, y));
				
				if(piece != null)
				{
					break;
				}
			}
		}
		
		x = origPos.xPos;
		y = origPos.yPos;
		
		if(y != 0)
		{
			for(y = y-1; y >= 0; y--)
			{
				ChessPiece piece = grid[x][y].housedPiece;
				
				if(piece != null && piece.side == origPos.getSquare(grid).housedPiece.side)
				{
					break;
				}
				
				ret.add(new ChessPos(x, y));
				
				if(piece != null)
				{
					break;
				}
			}
		}
		
		return ret;
	}
}
