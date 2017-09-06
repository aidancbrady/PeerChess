package com.aidancbrady.peerchess.piece;

import java.util.Iterator;
import java.util.Set;

import com.aidancbrady.peerchess.ChessMove;
import com.aidancbrady.peerchess.ChessPiece;
import com.aidancbrady.peerchess.ChessPos;
import com.aidancbrady.peerchess.ChessSquare;
import com.aidancbrady.peerchess.PeerUtils;

public class PieceKing implements Piece
{
	@Override
	public boolean validateMove(ChessSquare[][] grid, ChessMove move)
	{
		if(move.isValidStep(grid) || isValidCastle(grid, move))
		{
			if(!PeerUtils.isInCheck(move.getFromSquare(grid).housedPiece.side, move.toPos, move.getFakeGrid(grid)))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isValidCastle(ChessSquare[][] grid, ChessMove move)
	{
	    ChessPiece piece = move.fromPos.getSquare(grid).housedPiece;
        
        if(!PeerUtils.isInCheck(piece.side, move.fromPos, grid) && piece.moves == 0)
        {
            ChessPiece test = null;
            
            if(move.toPos.xPos == 2)
            {
                test = grid[0][move.fromPos.yPos].housedPiece;
                
                if(test != null && test.moves == 0)
                {
                    if(grid[1][move.fromPos.yPos].housedPiece == null && grid[2][move.fromPos.yPos].housedPiece == null &&
                            grid[3][move.fromPos.yPos].housedPiece == null)
                    {
                        move.fromPosCastle = new ChessPos(0, move.fromPos.yPos);
                        move.toPosCastle = new ChessPos(3, move.fromPos.yPos);
                        return true;
                    }
                }
            }
            else if(move.toPos.xPos == 6)
            {
                test = grid[7][move.fromPos.yPos].housedPiece;
                
                if(test != null && test.moves == 0)
                {
                    if(grid[6][move.fromPos.yPos].housedPiece == null && grid[5][move.fromPos.yPos].housedPiece == null)
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
			
			if(pos.getSquare(grid).housedPiece != null && pos.getSquare(grid).housedPiece.side == origPos.getSquare(grid).housedPiece.side)
			{
				iter.remove();
			}
		}
		
		ChessPiece piece = origPos.getSquare(grid).housedPiece;
		
		if(!PeerUtils.isInCheck(piece.side, origPos, grid) && piece.moves == 0)
		{
		    ChessPiece test = grid[0][origPos.yPos].housedPiece;
		    
		    if(test != null && test.moves == 0)
		    {
		        if(grid[1][origPos.yPos].housedPiece == null && grid[2][origPos.yPos].housedPiece == null &&
		                grid[3][origPos.yPos].housedPiece == null)
		        {
		            possibleMoves.add(new ChessPos(2, origPos.yPos));
		        }
		    }
		    
		    test = grid[7][origPos.yPos].housedPiece;
		    
		    if(test != null && test.moves == 0)
		    {
		        if(grid[6][origPos.yPos].housedPiece == null && grid[5][origPos.yPos].housedPiece == null)
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
