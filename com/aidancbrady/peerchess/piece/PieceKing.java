package com.aidancbrady.peerchess.piece;

import java.util.Iterator;
import java.util.Set;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.client.Constants;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;

public class PieceKing implements Piece
{
	@Override
	public boolean validateMove(IChessGame game, ChessMove move)
	{
		if(move.isValidStep(game.getGrid()) || isValidCastle(game, move))
		{
		    if(!game.testCheck(move.getFromSquare(game.getGrid()).getPiece().getSide(), move.toPos, move))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isValidCastle(IChessGame game, ChessMove move)
	{
	    ChessPiece piece = move.fromPos.getSquare(game.getGrid()).getPiece();
        
        if(!game.isInCheck(piece.getSide(), move.fromPos) && piece.getMoves() == 0)
        {
            ChessPiece test = null;
            
            if(move.toPos.getX() == 2)
            {
                if(game.isInCheck(piece.getSide(), move.fromPos.translate(-1, 0)))
                {
                    return false;
                }
                
                test = game.getGrid()[0][move.fromPos.getY()].getPiece();
                
                if(test != null && test.getMoves() == 0)
                {
                    if(game.getGrid()[1][move.fromPos.getY()].getPiece() == null && game.getGrid()[2][move.fromPos.getY()].getPiece() == null &&
                            game.getGrid()[3][move.fromPos.getY()].getPiece() == null)
                    {
                        move.fromPosCastle = new ChessPos(0, move.fromPos.getY());
                        move.toPosCastle = new ChessPos(3, move.fromPos.getY());
                        return true;
                    }
                }
            }
            else if(move.toPos.getX() == 6)
            {
                if(game.isInCheck(piece.getSide(), move.fromPos.translate(1, 0)))
                {
                    return false;
                }
                
                test = game.getGrid()[7][move.fromPos.getY()].getPiece();
                
                if(test != null && test.getMoves() == 0)
                {
                    if(game.getGrid()[6][move.fromPos.getY()].getPiece() == null && game.getGrid()[5][move.fromPos.getY()].getPiece() == null)
                    {
                        move.fromPosCastle = new ChessPos(7, move.fromPos.getY());
                        move.toPosCastle = new ChessPos(5, move.fromPos.getY());
                        return true;
                    }
                }
            }
        }
        
        return false;
	}
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(IChessGame game, ChessPos origPos)
	{
		Set<ChessPos> possibleMoves = PeerUtils.getValidStepMoves(origPos);
		
		for(Iterator<ChessPos> iter = possibleMoves.iterator(); iter.hasNext();)
		{
			ChessPos pos = iter.next();
			
			if(pos.getSquare(game.getGrid()).getPiece() != null && pos.getSquare(game.getGrid()).getPiece().getSide() == origPos.getSquare(game.getGrid()).getPiece().getSide())
			{
				iter.remove();
			}
		}
		
		ChessPiece piece = origPos.getSquare(game.getGrid()).getPiece();
		
		if(!game.isInCheck(piece.getSide(), origPos) && piece.getMoves() == 0)
		{
		    ChessPiece test = game.getGrid()[0][origPos.getY()].getPiece();
		    
		    if(test != null && test.getMoves() == 0)
		    {
		        if(game.getGrid()[1][origPos.getY()].getPiece() == null && game.getGrid()[2][origPos.getY()].getPiece() == null &&
		                game.getGrid()[3][origPos.getY()].getPiece() == null)
		        {
		            possibleMoves.add(new ChessPos(2, origPos.getY()));
		        }
		    }
		    
		    test = game.getGrid()[7][origPos.getY()].getPiece();
		    
		    if(test != null && test.getMoves() == 0)
		    {
		        if(game.getGrid()[6][origPos.getY()].getPiece() == null && game.getGrid()[5][origPos.getY()].getPiece() == null)
		        {
		            possibleMoves.add(new ChessPos(6, origPos.getY()));
		        }
		    }
		}
		
		return possibleMoves;
	}
	
	@Override
	public int getPointValue()
	{
	    return 900;
	}
	
    @Override
    public double[][] getPlacementEvaluation(Side side)
    {
        return side == Side.WHITE ? Constants.kingEvalWhite : Constants.kingEvalBlack;
    }
}
