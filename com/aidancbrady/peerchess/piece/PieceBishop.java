package com.aidancbrady.peerchess.piece;

import java.util.HashSet;
import java.util.Set;

import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;

public class PieceBishop implements Piece
{
	@Override
	public boolean validateMove(ChessSquare[][] grid, ChessMove move)
	{
		ChessPos pos = PeerUtils.findKing(move.getFromSquare(grid).getPiece().side, grid);
		
		if(PeerUtils.isInCheck(move.getFromSquare(grid).getPiece().side, pos, move.getFakeGrid(grid)))
		{
			return false;
		}
		
		return move.isValidDiagonal(grid);
	}
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(ChessSquare[][] grid, ChessPos origPos)
	{
		Set<ChessPos> ret = new HashSet<ChessPos>();
		
		int x = origPos.xPos;
		int y = origPos.yPos;
		
		while(x < 7 && y > 0)
		{
			x++;
			y--;
			
			ChessPiece piece = grid[x][y].getPiece();
			
			if(piece != null && piece.side == origPos.getSquare(grid).getPiece().side)
			{
				break;
			}
			
			ret.add(new ChessPos(x, y));
			
			if(piece != null)
			{
				break;
			}
		}
		
		x = origPos.xPos;
		y = origPos.yPos;
		
		while(x > 0 && y < 7)
		{
			x--;
			y++;
			
			ChessPiece piece = grid[x][y].getPiece();
			
			if(piece != null && piece.side == origPos.getSquare(grid).getPiece().side)
			{
				break;
			}
			
			ret.add(new ChessPos(x, y));
			
			if(piece != null)
			{
				break;
			}
		}
		
		x = origPos.xPos;
		y = origPos.yPos;
		
		while(x < 7 && y < 7)
		{
			x++;
			y++;
			
			ChessPiece piece = grid[x][y].getPiece();
			
			if(piece != null && piece.side == origPos.getSquare(grid).getPiece().side)
			{
				break;
			}
			
			ret.add(new ChessPos(x, y));
			
			if(piece != null)
			{
				break;
			}
		}
		
		x = origPos.xPos;
		y = origPos.yPos;
		
		while(x > 0 && y > 0)
		{
			x--;
			y--;
			
			ChessPiece piece = grid[x][y].getPiece();
			
			if(piece != null && piece.side == origPos.getSquare(grid).getPiece().side)
			{
				break;
			}
			
			ret.add(new ChessPos(x, y));
			
			if(piece != null)
			{
				break;
			}
		}
		
		return ret;
	}
	
	@Override
	public int getPointValue()
	{
	    return 30;
	}
}
