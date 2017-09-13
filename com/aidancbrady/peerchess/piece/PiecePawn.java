package com.aidancbrady.peerchess.piece;

import java.util.HashSet;
import java.util.Set;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.PeerUtils;
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
		ChessPos pos = PeerUtils.findKing(move.getFromSquare(game.getGrid()).getPiece().side, game.getGrid());
		
		if(PeerUtils.testCheck(move.getFromSquare(game.getGrid()).getPiece().side, pos, game.getGrid(), move))
		{
			return false;
		}
		
		Side side = move.getFromSquare(game.getGrid()).getPiece().side;
		int yStart = side == Side.BLACK ? 1 : 6;
		
		ChessPos left = move.fromPos.translate(1, side == Side.BLACK ? 1 : -1);
		ChessPos right = move.fromPos.translate(-1, side == Side.BLACK ? 1 : -1);
		
		if(move.fromPos.xPos == move.toPos.xPos)
		{
			if(move.getToSquare(game.getGrid()).getPiece() != null)
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
		
		int beforeY = side == Side.BLACK ? 2 : 5;
		
		if(yAbs == 2 && game.getGrid()[move.fromPos.xPos][beforeY].getPiece() != null)
		{
		    return false;
		}
		
		if(move.toPos.equals(left) || move.toPos.equals(right))
		{
			if(move.getToSquare(game.getGrid()).getPiece() != null && move.getToSquare(game.getGrid()).getPiece().side == move.getFromSquare(game.getGrid()).getPiece().side)
			{
				return false;
			}
			
			if(move.getToSquare(game.getGrid()).getPiece() == null) //en passant
			{
			    ChessPos prevPos = move.toPos.translate(0, side == Side.BLACK ? -1 : 1);
			    ChessPiece prevPiece = prevPos.getSquare(game.getGrid()).getPiece();
			    
			    if(prevPiece == null || prevPiece.side == side || prevPiece.type != PieceType.PAWN)
			    {
			        return false;
			    }
			}
		}
		
		return move.equals(left) || move.equals(right) || xAbs <= 1;
	}
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(IChessGame game, ChessPos origPos)
	{
		Set<ChessPos> possibleMoves = new HashSet<ChessPos>();
		
		Side side = origPos.getSquare(game.getGrid()).getPiece().side;
		int yStart = side == Side.BLACK ? 1 : 6;
		
		ChessPos left = origPos.translate(1, side == Side.BLACK ? 1 : -1);
		ChessPos right = origPos.translate(-1, side == Side.BLACK ? 1 : -1);
		
		if(left.isInRange())
		{
		    if(left.getSquare(game.getGrid()).getPiece() != null && left.getSquare(game.getGrid()).getPiece().side != side)
		    {
		        possibleMoves.add(left);
		    }
		    else if(left.getSquare(game.getGrid()).getPiece() == null) //en passant
		    {
		        ChessPos lower = left.translate(0, side == Side.BLACK ? -1 : 1);
		        ChessPiece lowerPiece = lower.getSquare(game.getGrid()).getPiece();
		        
		        if(lowerPiece != null && lowerPiece.side != side && lowerPiece.type == PieceType.PAWN)
		        {
		            if(!game.getPastMoves().isEmpty())
		            {
    		            ChessMove lastMove = game.getPastMoves().get(game.getPastMoves().size()-1);
    		            
    		            if(lower.equals(lastMove.toPos))
    		            {
    		                possibleMoves.add(lower);
    		            }
		            }
		        }
		    }
		}
		
		if(right.isInRange())
		{
		    if(right.getSquare(game.getGrid()).getPiece() != null && right.getSquare(game.getGrid()).getPiece().side != side)
		    {
		        possibleMoves.add(right);
		    }
		    else if(right.getSquare(game.getGrid()).getPiece() == null) //en passant
		    {
		        ChessPos lower = right.translate(0, side == Side.BLACK ? -1 : 1);
                ChessPiece lowerPiece = lower.getSquare(game.getGrid()).getPiece();
                
                if(lowerPiece != null && lowerPiece.side != side && lowerPiece.type == PieceType.PAWN)
                {
                    if(!game.getPastMoves().isEmpty())
                    {
                        ChessMove lastMove = game.getPastMoves().get(game.getPastMoves().size()-1);
                        
                        if(lower.equals(lastMove.toPos))
                        {
                            possibleMoves.add(lower);
                        }
                    }
                }
		    }
		}
		
		if(origPos.yPos == yStart)
		{
			int forwardY = side == Side.BLACK ? 3 : 4;
			int beforeY = side == Side.BLACK ? 2 : 5;
			
			if(game.getGrid()[origPos.xPos][forwardY].getPiece() == null && game.getGrid()[origPos.xPos][beforeY].getPiece() == null)
			{
				possibleMoves.add(new ChessPos(origPos.xPos, forwardY));
			}
		}
		
		int forwardY = side == Side.BLACK ? origPos.yPos+1 : origPos.yPos-1;
		
		if(forwardY >= 0 && forwardY <= 7 && game.getGrid()[origPos.xPos][forwardY].getPiece() == null)
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
