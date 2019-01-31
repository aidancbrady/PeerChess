package com.aidancbrady.peerchess.piece;

import java.util.HashSet;
import java.util.Set;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.client.Constants;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;

public class PieceBishop implements Piece
{
	@Override
	public boolean validateMove(IChessGame game, ChessMove move)
	{
		ChessPos pos = game.findKing(move.getFromSquare(game.getGrid()).getPiece().getSide());
		
		if(game.testCheck(move.getFromSquare(game.getGrid()).getPiece().getSide(), pos, move))
		{
			return false;
		}
		
		return move.isValidDiagonal(game.getGrid());
	}
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(IChessGame game, ChessPos origPos)
	{
		Set<ChessPos> ret = new HashSet<ChessPos>();
		
		int x = origPos.getX();
		int y = origPos.getY();
		
		while(x < 7 && y > 0)
		{
			x++;
			y--;
			
			ChessPiece piece = game.getGrid()[x][y].getPiece();
			
			if(piece != null && piece.getSide() == origPos.getSquare(game.getGrid()).getPiece().getSide())
			{
				break;
			}
			
			ret.add(new ChessPos(x, y));
			
			if(piece != null)
			{
				break;
			}
		}
		
		x = origPos.getX();
		y = origPos.getY();
		
		while(x > 0 && y < 7)
		{
			x--;
			y++;
			
			ChessPiece piece = game.getGrid()[x][y].getPiece();
			
			if(piece != null && piece.getSide() == origPos.getSquare(game.getGrid()).getPiece().getSide())
			{
				break;
			}
			
			ret.add(new ChessPos(x, y));
			
			if(piece != null)
			{
				break;
			}
		}
		
		x = origPos.getX();
		y = origPos.getY();
		
		while(x < 7 && y < 7)
		{
			x++;
			y++;
			
			ChessPiece piece = game.getGrid()[x][y].getPiece();
			
			if(piece != null && piece.getSide() == origPos.getSquare(game.getGrid()).getPiece().getSide())
			{
				break;
			}
			
			ret.add(new ChessPos(x, y));
			
			if(piece != null)
			{
				break;
			}
		}
		
		x = origPos.getX();
		y = origPos.getY();
		
		while(x > 0 && y > 0)
		{
			x--;
			y--;
			
			ChessPiece piece = game.getGrid()[x][y].getPiece();
			
			if(piece != null && piece.getSide() == origPos.getSquare(game.getGrid()).getPiece().getSide())
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
	
	@Override
	public double[][] getPlacementEvaluation(Side side)
	{
	    return side == Side.WHITE ? Constants.bishopEvalWhite : Constants.bishopEvalBlack;
	}
}
