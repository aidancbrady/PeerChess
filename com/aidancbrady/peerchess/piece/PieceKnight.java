package com.aidancbrady.peerchess.piece;

import java.util.Iterator;
import java.util.Set;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.client.Constants;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;

public class PieceKnight implements Piece
{
	@Override
	public boolean validateMove(IChessGame game, ChessMove move)
	{
		ChessPos pos = PeerUtils.findKing(move.getFromSquare(game.getGrid()).getPiece().getSide(), game.getGrid());
		
		if(PeerUtils.testCheck(move.getFromSquare(game.getGrid()).getPiece().getSide(), pos, game.getGrid(), move))
		{
			return false;
		}
		
		Set<ChessPos> validDests = PeerUtils.getValidKnightMoves(move.fromPos);
		
		if(validDests.contains(move.toPos))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public Set<ChessPos> getCurrentPossibleMoves(IChessGame game, ChessPos origPos)
	{
		Set<ChessPos> possibleMoves = PeerUtils.getValidKnightMoves(origPos);
		
		for(Iterator<ChessPos> iter = possibleMoves.iterator(); iter.hasNext();)
		{
			ChessPos pos = iter.next();
			
			if(pos.getSquare(game.getGrid()).getPiece() != null && pos.getSquare(game.getGrid()).getPiece().getSide() == origPos.getSquare(game.getGrid()).getPiece().getSide())
			{
				iter.remove();
			}
		}
		
		return possibleMoves;
	}
	
	@Override
	public int getPointValue()
	{
	    return 30;
	}
	
    @Override
    public double[][] getPlacementEvaluation(Side side)
    {
        return Constants.knightEval;
    }
}
