package com.aidancbrady.peerchess.piece;

import java.util.Iterator;
import java.util.Set;

import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;

public class PieceKing implements Piece
{
	@Override
	public boolean validateMove(ChessSquare[][] grid, ChessMove move)
	{
		if(move.isValidStep(grid) || isValidCastle(grid, move))
		{
			if(!PeerUtils.isInCheck(move.getFromSquare(grid).getPiece().side, move.toPos, move.getFakeGrid(grid)))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isValidCastle(ChessSquare[][] grid, ChessMove move)
	{
	    ChessPiece piece = move.fromPos.getSquare(grid).getPiece();
        
        if(!PeerUtils.isInCheck(piece.side, move.fromPos, grid) && piece.moves == 0)
        {
            ChessPiece test = null;
            
            if(move.toPos.xPos == 2)
            {
                test = grid[0][move.fromPos.yPos].getPiece();
                
                if(test != null && test.moves == 0)
                {
                    if(grid[1][move.fromPos.yPos].getPiece() == null && grid[2][move.fromPos.yPos].getPiece() == null &&
                            grid[3][move.fromPos.yPos].getPiece() == null)
                    {
                        move.fromPosCastle = new ChessPos(0, move.fromPos.yPos);
                        move.toPosCastle = new ChessPos(3, move.fromPos.yPos);
                        return true;
                    }
                }
            }
            else if(move.toPos.xPos == 6)
            {
                test = grid[7][move.fromPos.yPos].getPiece();
                
                if(test != null && test.moves == 0)
                {
                    if(grid[6][move.fromPos.yPos].getPiece() == null && grid[5][move.fromPos.yPos].getPiece() == null)
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
	public Set<ChessPos> getCurrentPossibleMoves(ChessSquare[][] grid, ChessPos origPos)
	{
		Set<ChessPos> possibleMoves = PeerUtils.getValidStepMoves(origPos);
		
		for(Iterator<ChessPos> iter = possibleMoves.iterator(); iter.hasNext();)
		{
			ChessPos pos = iter.next();
			
			if(pos.getSquare(grid).getPiece() != null && pos.getSquare(grid).getPiece().side == origPos.getSquare(grid).getPiece().side)
			{
				iter.remove();
			}
		}
		
		ChessPiece piece = origPos.getSquare(grid).getPiece();
		
		if(!PeerUtils.isInCheck(piece.side, origPos, grid) && piece.moves == 0)
		{
		    ChessPiece test = grid[0][origPos.yPos].getPiece();
		    
		    if(test != null && test.moves == 0)
		    {
		        if(grid[1][origPos.yPos].getPiece() == null && grid[2][origPos.yPos].getPiece() == null &&
		                grid[3][origPos.yPos].getPiece() == null)
		        {
		            possibleMoves.add(new ChessPos(2, origPos.yPos));
		        }
		    }
		    
		    test = grid[7][origPos.yPos].getPiece();
		    
		    if(test != null && test.moves == 0)
		    {
		        if(grid[6][origPos.yPos].getPiece() == null && grid[5][origPos.yPos].getPiece() == null)
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
