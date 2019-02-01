package com.aidancbrady.peerchess.piece;

import java.util.HashSet;
import java.util.Set;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.client.Constants;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;

public class PiecePawn implements Piece
{
	@Override
	public boolean validateMove(IChessGame game, ChessMove move)
	{
		ChessPos pos = game.findKing(move.getFromSquare(game.getGrid()).getPiece().getSide());
		
		if(game.testCheck(move.getFromSquare(game.getGrid()).getPiece().getSide(), pos, move))
		{
			return false;
		}
		
		Side side = move.getFromSquare(game.getGrid()).getPiece().getSide();
		int yStart = side == Side.BLACK ? 1 : 6;
		
		ChessPos left = move.fromPos.translate(1, side == Side.BLACK ? 1 : -1);
		ChessPos right = move.fromPos.translate(-1, side == Side.BLACK ? 1 : -1);
		
		if(move.fromPos.getX() == move.toPos.getX())
		{
			if(move.getToSquare(game.getGrid()).getPiece() != null)
			{
				return false;
			}
		}
		
		if(side == Side.BLACK && move.fromPos.getY()-move.toPos.getY() > 0)
		{
			return false;
		}
		else if(side == Side.WHITE && move.fromPos.getY()-move.toPos.getY() < 0)
		{
			return false;
		}
		
		int xAbs = Math.abs(move.fromPos.getX()-move.toPos.getX());
		int yAbs = Math.abs(move.fromPos.getY()-move.toPos.getY());
		
		if(move.fromPos.getY() == yStart && (yAbs != 1 && yAbs != 2))
		{
			return false;
		}
		else if(move.fromPos.getY() != yStart && yAbs != 1)
		{
			return false;
		}
		
		int beforeY = side == Side.BLACK ? 2 : 5;
		
		if(yAbs == 2 && game.getGrid()[move.fromPos.getX()][beforeY].getPiece() != null)
		{
		    return false;
		}
		
		if(yAbs == 2 && xAbs != 0)
		{
		    return false;
		}
		
		if(move.toPos.equals(left) || move.toPos.equals(right))
		{
			if(move.getToSquare(game.getGrid()).getPiece() != null && move.getToSquare(game.getGrid()).getPiece().getSide() == move.getFromSquare(game.getGrid()).getPiece().getSide())
			{
				return false;
			}
			
			if(move.getToSquare(game.getGrid()).getPiece() == null) //en passant
			{
			    ChessPos prevPos = move.toPos.translate(0, side == Side.BLACK ? -1 : 1);
			    ChessPiece prevPiece = prevPos.getSquare(game.getGrid()).getPiece();
			    
			    if(prevPiece == null || prevPiece.getSide() == side || prevPiece.getType() != PieceType.PAWN)
			    {
			        return false;
			    }
			    
			    if(game.getPastMoves().isEmpty() || !game.getPastMoves().get(game.getPastMoves().size()-1).toPos.equals(prevPos))
			    {
			        return false;
			    }
			    
			    move.enPassantTakePos = prevPos.clone();
			}
		}
		
		return move.equals(left) || move.equals(right) || xAbs <= 1;
	}
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(IChessGame game, ChessPos origPos, boolean pruneBlocked)
	{
		Set<ChessPos> possibleMoves = new HashSet<ChessPos>();
		
		Side side = origPos.getSquare(game.getGrid()).getPiece().getSide();
		int yStart = side == Side.BLACK ? 1 : 6;
		
		ChessPos left = origPos.translate(1, side == Side.BLACK ? 1 : -1);
		ChessPos right = origPos.translate(-1, side == Side.BLACK ? 1 : -1);
		
		if(left.isInRange())
		{
		    if(left.getSquare(game.getGrid()).getPiece() != null && left.getSquare(game.getGrid()).getPiece().getSide() != side)
		    {
		        possibleMoves.add(left);
		    }
		    else if(!pruneBlocked || left.getSquare(game.getGrid()).getPiece() == null) //en passant
		    {
		        ChessPos lower = left.translate(0, side == Side.BLACK ? -1 : 1);
		        ChessPiece lowerPiece = lower.getSquare(game.getGrid()).getPiece();
		        
		        if(lowerPiece != null && lowerPiece.getSide() != side && lowerPiece.getType() == PieceType.PAWN)
		        {
		            if(!game.getPastMoves().isEmpty())
		            {
    		            ChessMove lastMove = game.getPastMoves().get(game.getPastMoves().size()-1);
    		            
    		            if(lower.equals(lastMove.toPos))
    		            {
    		                possibleMoves.add(left);
    		            }
		            }
		        }
		    }
		}
		
		if(right.isInRange())
		{
		    if(right.getSquare(game.getGrid()).getPiece() != null && right.getSquare(game.getGrid()).getPiece().getSide() != side)
		    {
		        possibleMoves.add(right);
		    }
		    else if(!pruneBlocked || right.getSquare(game.getGrid()).getPiece() == null) //en passant
		    {
		        ChessPos lower = right.translate(0, side == Side.BLACK ? -1 : 1);
                ChessPiece lowerPiece = lower.getSquare(game.getGrid()).getPiece();
                
                if(lowerPiece != null && lowerPiece.getSide() != side && lowerPiece.getType() == PieceType.PAWN)
                {
                    if(!game.getPastMoves().isEmpty())
                    {
                        ChessMove lastMove = game.getPastMoves().get(game.getPastMoves().size()-1);
                        
                        if(lower.equals(lastMove.toPos))
                        {
                            possibleMoves.add(right);
                        }
                    }
                }
		    }
		}
		
		if(origPos.getY() == yStart)
		{
			int forwardY = side == Side.BLACK ? 3 : 4;
			int beforeY = side == Side.BLACK ? 2 : 5;
			
			if(!pruneBlocked || (game.getGrid()[origPos.getX()][forwardY].getPiece() == null && game.getGrid()[origPos.getX()][beforeY].getPiece() == null))
			{
				possibleMoves.add(new ChessPos(origPos.getX(), forwardY));
			}
		}
		
		int forwardY = side == Side.BLACK ? origPos.getY()+1 : origPos.getY()-1;
		
		if(forwardY >= 0 && forwardY <= 7 && (!pruneBlocked || game.getGrid()[origPos.getX()][forwardY].getPiece() == null))
		{
			possibleMoves.add(new ChessPos(origPos.getX(), forwardY));
		}
		
		return possibleMoves;
	}
	
	@Override
	public int getPointValue()
	{
	    return 10;
	}
	
    @Override
    public double[][] getPlacementEvaluation(Side side)
    {
        return side == Side.WHITE ? Constants.pawnEvalWhite : Constants.pawnEvalBlack;
    }
}
