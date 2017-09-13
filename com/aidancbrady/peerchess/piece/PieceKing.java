package com.aidancbrady.peerchess.piece;

import java.util.Iterator;
import java.util.Set;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPos;

public class PieceKing implements Piece
{
	@Override
	public boolean validateMove(IChessGame game, ChessMove move)
	{
		if(move.isValidStep(game.getGrid()) || isValidCastle(game, move))
		{
		    if(!PeerUtils.testCheck(move.getFromSquare(game.getGrid()).getPiece().side, move.toPos, game.getGrid(), move))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isValidCastle(IChessGame game, ChessMove move)
	{
	    ChessPiece piece = move.fromPos.getSquare(game.getGrid()).getPiece();
        
        if(!PeerUtils.isInCheck(piece.side, move.fromPos, game.getGrid()) && piece.moves == 0)
        {
            ChessPiece test = null;
            
            if(move.toPos.xPos == 2)
            {
                if(PeerUtils.isInCheck(piece.side, move.fromPos.translate(-1, 0), game.getGrid()))
                {
                    return false;
                }
                
                test = game.getGrid()[0][move.fromPos.yPos].getPiece();
                
                if(test != null && test.moves == 0)
                {
                    if(game.getGrid()[1][move.fromPos.yPos].getPiece() == null && game.getGrid()[2][move.fromPos.yPos].getPiece() == null &&
                            game.getGrid()[3][move.fromPos.yPos].getPiece() == null)
                    {
                        move.fromPosCastle = new ChessPos(0, move.fromPos.yPos);
                        move.toPosCastle = new ChessPos(3, move.fromPos.yPos);
                        return true;
                    }
                }
            }
            else if(move.toPos.xPos == 6)
            {
                if(PeerUtils.isInCheck(piece.side, move.fromPos.translate(1, 0), game.getGrid()))
                {
                    return false;
                }
                
                test = game.getGrid()[7][move.fromPos.yPos].getPiece();
                
                if(test != null && test.moves == 0)
                {
                    if(game.getGrid()[6][move.fromPos.yPos].getPiece() == null && game.getGrid()[5][move.fromPos.yPos].getPiece() == null)
                    {
                        move.fromPosCastle = new ChessPos(7, move.fromPos.yPos);
                        move.toPosCastle = new ChessPos(5, move.fromPos.yPos);
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
			
			if(pos.getSquare(game.getGrid()).getPiece() != null && pos.getSquare(game.getGrid()).getPiece().side == origPos.getSquare(game.getGrid()).getPiece().side)
			{
				iter.remove();
			}
		}
		
		ChessPiece piece = origPos.getSquare(game.getGrid()).getPiece();
		
		if(!PeerUtils.isInCheck(piece.side, origPos, game.getGrid()) && piece.moves == 0)
		{
		    ChessPiece test = game.getGrid()[0][origPos.yPos].getPiece();
		    
		    if(test != null && test.moves == 0)
		    {
		        if(game.getGrid()[1][origPos.yPos].getPiece() == null && game.getGrid()[2][origPos.yPos].getPiece() == null &&
		                game.getGrid()[3][origPos.yPos].getPiece() == null)
		        {
		            possibleMoves.add(new ChessPos(2, origPos.yPos));
		        }
		    }
		    
		    test = game.getGrid()[7][origPos.yPos].getPiece();
		    
		    if(test != null && test.moves == 0)
		    {
		        if(game.getGrid()[6][origPos.yPos].getPiece() == null && game.getGrid()[5][origPos.yPos].getPiece() == null)
		        {
		            possibleMoves.add(new ChessPos(6, origPos.yPos));
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
}
