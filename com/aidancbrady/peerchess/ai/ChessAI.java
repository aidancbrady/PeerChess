package com.aidancbrady.peerchess.ai;

import java.util.List;

import com.aidancbrady.peerchess.ChessComponent;
import com.aidancbrady.peerchess.MoveAction;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;

public class ChessAI 
{
    private ChessComponent chess;
    public int evaluations = 0;
    public boolean terminate = false;
    
    public ChessAI(ChessComponent component)
    {
        chess = component;
    }
    
    public void triggerMove()
    {
        terminate = false; // thread safety
        ChessMove move = minimax();
        ChessPiece piece = move.fromPos.getSquare(chess.grid).getPiece();
        evaluations = 0;
        
        if(!terminate)
        {
            ChessPiece finalPiece = piece;
            
            if(move.getFromSquare(chess.grid).getPiece().type == PieceType.PAWN)
            {
                if(move.toPos.yPos == 0 || move.toPos.yPos == 7)
                {
                    finalPiece = new ChessPiece(PieceType.QUEEN, piece.side);
                }
            }
            
            chess.currentMove = new MoveAction(chess, move, piece, finalPiece);
            chess.panel.updateText();
        }
    }
    
    public ChessMove minimax()
    {
        TestBoard board = new TestBoard(chess.grid);
        List<ChessMove> possibleMoves = board.getPossibleMoves(getSide(), true);
        double bestMoveScore = -9999;
        ChessMove bestMove = null;
        
        for(ChessMove move : possibleMoves)
        {
            double delta = board.applyMove(move);
            double score = minimax_do(Constants.MAX_DEPTH-1, board, -10000, 10000, false);
            board.revertMove(move, delta);
            
            if(score >= bestMoveScore)
            {
                bestMoveScore = score;
                bestMove = move;
            }
        }
        
        return bestMove;
    }
    
    public double minimax_do(int depth, TestBoard board, double alpha, double beta, boolean maximizing)
    {
        evaluations++;
        
        if(depth == 0)
        {
            return getSide() == Side.WHITE ? board.getEvaluation() : -board.getEvaluation();
        }
        
        double bestMoveScore = maximizing ? -9999 : 9999;
        
        List<ChessMove> possibleMoves = board.getPossibleMoves(getSide(), maximizing);
        
        for(ChessMove move : possibleMoves)
        {
            double delta = board.applyMove(move);
            
            if(move.testTakingKing())
            {
                board.revertMove(move, delta);
                return getSide() == Side.WHITE ? board.getEvaluation() : -board.getEvaluation();
            }
            
            if(maximizing)
            {
                bestMoveScore = Math.max(bestMoveScore, minimax_do(depth - 1, board, alpha, beta, !maximizing));
                alpha = Math.max(alpha, bestMoveScore);
            }
            else {
                bestMoveScore = Math.min(bestMoveScore, minimax_do(depth - 1, board, alpha, beta, !maximizing));
                beta = Math.min(beta, bestMoveScore);
            }
            
            board.revertMove(move, delta);
            
            if(beta <= alpha)
            {
                return bestMoveScore;
            }
        }
        
        return bestMoveScore;
    }
    
    public static ChessSquare[][] cloneGrid(ChessSquare[][] grid)
    {
        ChessSquare[][] fakeGrid = new ChessSquare[8][8];
        
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                fakeGrid[x][y] = new ChessSquare(false, new ChessPos(x, y));
                fakeGrid[x][y].setPiece(grid[x][y].getPiece());
            }
        }
        
        return fakeGrid;
    }
    
    public void updateGrid(ChessSquare[][] grid, ChessMove move)
    {
        if(move.getToSquare(grid).getPiece().type == PieceType.PAWN)
        {
            if(move.toPos.yPos == 0 || move.toPos.yPos == 7)
            {
                ChessPiece oldPiece = move.getToSquare(grid).getPiece();
                move.getToSquare(grid).setPiece(new ChessPiece(PieceType.QUEEN, oldPiece.side));
            }
        }
    }
    
    public Side getSide()
    {
        return chess.turn;
    }
    
    public void reset() 
    {
        terminate = true;
    }
}
