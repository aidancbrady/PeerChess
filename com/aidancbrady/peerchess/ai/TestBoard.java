package com.aidancbrady.peerchess.ai;

import java.util.ArrayList;
import java.util.List;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.gui.ChessComponent;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;
import com.aidancbrady.peerchess.game.DrawTracker;

public class TestBoard implements IChessGame
{
    private ChessSquare[][] grid;
    private double currentEvaluation;
    private List<ChessMove> moves;
    private boolean checkmate;
    
    public TestBoard(ChessComponent game)
    {
        grid = PeerUtils.deepCopyBoard(game.getGame().getGrid());
        moves = new ArrayList<ChessMove>(game.getGame().getMoves());
        currentEvaluation = evaluateBoard();
    }
    
    public double getEvaluation()
    {
        return currentEvaluation;
    }
    
    private double evaluateBoard()
    {
        double total = 0;
        
        for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                total += getSquareValue(new ChessPos(x, y));
            }
        }
        
        return total;
    }
    
    public List<ChessMove> getPossibleMoves(Side side, boolean maximizing)
    {
        Side sideToTest = maximizing ? side : side.getOpposite();
        List<ChessMove> possibleMoves = new ArrayList<>();
        
        for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                if(grid[x][y].getPiece() != null && grid[x][y].getPiece().getSide() == sideToTest)
                {
                    ChessPiece piece = grid[x][y].getPiece();
                    ChessPos origPos = new ChessPos(x, y);
                    
                    for(ChessPos pos : piece.getType().getPiece().getCurrentPossibleMoves(this, origPos))
                    {
                        ChessMove move = new ChessMove(origPos, pos);
                        
                        if(piece.getType().getPiece().validateMove(this, move))
                        {
                            possibleMoves.add(move);
                        }
                    }
                }
            }
        }
        
        return possibleMoves;
    }
    
    public double getSquareValue(ChessPos pos)
    {
        ChessPiece piece = pos.getSquare(grid).getPiece();
        
        if(piece == null)
        {
            return 0;
        }
        
        double value = piece.getType().getPiece().getPointValue() + piece.getType().getPiece().getPlacementEvaluation(piece.getSide())[pos.getX()][pos.getY()];
        
        return piece.getSide() == Side.WHITE ? value : -value;
    }
    
    public double applyMove(ChessMove move)
    {
        double beforeScore = getSquareValue(move.fromPos) + getSquareValue(move.toPos);
        
        if(move.fromPosCastle != null)
        {
            beforeScore += getSquareValue(move.fromPosCastle) + getSquareValue(move.toPosCastle);
        }
        
        move.testApplyMove(grid);
        
        if(move.getToSquare(grid).getPiece().getType() == PieceType.PAWN)
        {
            if(move.toPos.getY() == 0 || move.toPos.getY() == 7)
            {
                ChessPiece oldPiece = move.getToSquare(grid).getPiece();
                move.getToSquare(grid).setPiece(new ChessPiece(PieceType.QUEEN, oldPiece.getSide()));
            }
        }
        
        double afterScore = getSquareValue(move.fromPos) + getSquareValue(move.toPos);
        
        if(move.fromPosCastle != null)
        {
            afterScore += getSquareValue(move.fromPosCastle) + getSquareValue(move.toPosCastle);
        }
        
        if(DrawTracker.isDraw(getPastMoves())) // draw
        {
            double eval = move.testFromPiece.getSide() == Side.WHITE ? getEvaluation() : -getEvaluation();
            afterScore += eval < -50 ? 20 : -20;
        }
        
        if(PeerUtils.isCheckMate(move.testFromPiece.getSide().getOpposite(), this, false))
        {
            ChessPos pos = PeerUtils.findKing(move.testFromPiece.getSide().getOpposite(), grid);
            
            if(PeerUtils.isInCheck(move.testFromPiece.getSide().getOpposite(), pos, grid)) // checkmate
            {
                afterScore += move.testFromPiece.getSide() == Side.WHITE ? 1000 : -1000;
                checkmate = true;
            }
            else { // stalemate
                double eval = move.testFromPiece.getSide() == Side.WHITE ? getEvaluation() : -getEvaluation();
                afterScore += eval < -50 ? 20 : -20;
            }
        }
        
        double delta = afterScore-beforeScore;
        currentEvaluation += delta;
        
        moves.add(move);
        
        return delta;
    }
    
    public boolean isCheckmate()
    {
        return checkmate;
    }
    
    public void revertMove(ChessMove move, double delta)
    {
        checkmate = false;
        move.testRevertMove(grid);
        moves.remove(moves.size()-1);
        currentEvaluation -= delta;
    }
    
    @Override
    public ChessSquare[][] getGrid()
    {
        return grid;
    }
    
    @Override
    public List<ChessMove> getPastMoves()
    {
        return moves;
    }
}
