package com.aidancbrady.peerchess.piece;

import java.util.HashSet;
import java.util.Set;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.client.Constants;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;

public class PieceCastle implements Piece
{
	@Override
	public boolean validateMove(IChessGame game, ChessMove move)
	{
		ChessPos pos = game.findKing(move.getFromSquare(game.getGrid()).getPiece().getSide());
		
		if(game.testCheck(move.getFromSquare(game.getGrid()).getPiece().getSide(), pos, move))
		{
			return false;
		}
		
		return move.isValidStraight(game.getGrid());
	}
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(IChessGame game, ChessPos origPos, boolean pruneBlocked)
	{
		Set<ChessPos> ret = new HashSet<ChessPos>();
		
		int x = origPos.getX();
		int y = origPos.getY();
		
		if(x != 7)
		{
			for(x = x+1; x <= 7; x++)
			{
				ChessPiece piece = game.getGrid()[x][y].getPiece();
				
				if(pruneBlocked && piece != null && piece.getSide() == origPos.getSquare(game.getGrid()).getPiece().getSide())
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
		
		x = origPos.getX();
		y = origPos.getY();
		
		if(x != 0)
		{
			for(x = x-1; x >= 0; x--)
			{
				ChessPiece piece = game.getGrid()[x][y].getPiece();
				
				if(pruneBlocked && piece != null && piece.getSide() == origPos.getSquare(game.getGrid()).getPiece().getSide())
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
		
		x = origPos.getX();
		y = origPos.getY();
		
		if(y != 7)
		{
			for(y = y+1; y <= 7; y++)
			{
				ChessPiece piece = game.getGrid()[x][y].getPiece();
				
				if(pruneBlocked && piece != null && piece.getSide() == origPos.getSquare(game.getGrid()).getPiece().getSide())
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
		
		x = origPos.getX();
		y = origPos.getY();
		
		if(y != 0)
		{
			for(y = y-1; y >= 0; y--)
			{
				ChessPiece piece = game.getGrid()[x][y].getPiece();
				
				if(pruneBlocked && piece != null && piece.getSide() == origPos.getSquare(game.getGrid()).getPiece().getSide())
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
	
	@Override
	public int getPointValue()
	{
	    return 30;
	}
	
    @Override
    public double[][] getPlacementEvaluation(Side side)
    {
        return side == Side.WHITE ? Constants.castleEvalWhite : Constants.castleEvalBlack;
    }
}
