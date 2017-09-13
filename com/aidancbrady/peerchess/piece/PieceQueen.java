package com.aidancbrady.peerchess.piece;

import java.util.HashSet;
import java.util.Set;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPos;

public class PieceQueen implements Piece
{
	@Override
	public boolean validateMove(IChessGame game, ChessMove move)
	{
		ChessPos pos = PeerUtils.findKing(move.getFromSquare(game.getGrid()).getPiece().side, game.getGrid());
		
		if(PeerUtils.testCheck(move.getFromSquare(game.getGrid()).getPiece().side, pos, game.getGrid(), move))
		{
			return false;
		}
		
		return move.isValidStraight(game.getGrid()) || move.isValidDiagonal(game.getGrid());
	}
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(IChessGame game, ChessPos origPos)
	{
		Set<ChessPos> ret = new HashSet<ChessPos>();
		
		int x = origPos.xPos;
		int y = origPos.yPos;
		
		if(x != 7)
		{
			for(x = x+1; x <= 7; x++)
			{
				ChessPiece piece = game.getGrid()[x][y].getPiece();
				
				if(piece != null && piece.side == origPos.getSquare(game.getGrid()).getPiece().side)
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
				ChessPiece piece = game.getGrid()[x][y].getPiece();
				
				if(piece != null && piece.side == origPos.getSquare(game.getGrid()).getPiece().side)
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
				ChessPiece piece = game.getGrid()[x][y].getPiece();
				
				if(piece != null && piece.side == origPos.getSquare(game.getGrid()).getPiece().side)
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
				ChessPiece piece = game.getGrid()[x][y].getPiece();
				
				if(piece != null && piece.side == origPos.getSquare(game.getGrid()).getPiece().side)
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
		
		while(x < 7 && y > 0)
		{
			x++;
			y--;
			
			ChessPiece piece = game.getGrid()[x][y].getPiece();
			
			if(piece != null && piece.side == origPos.getSquare(game.getGrid()).getPiece().side)
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
			
			ChessPiece piece = game.getGrid()[x][y].getPiece();
			
			if(piece != null && piece.side == origPos.getSquare(game.getGrid()).getPiece().side)
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
			
			ChessPiece piece = game.getGrid()[x][y].getPiece();
			
			if(piece != null && piece.side == origPos.getSquare(game.getGrid()).getPiece().side)
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
			
			ChessPiece piece = game.getGrid()[x][y].getPiece();
			
			if(piece != null && piece.side == origPos.getSquare(game.getGrid()).getPiece().side)
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
	    return 90;
	}
}
