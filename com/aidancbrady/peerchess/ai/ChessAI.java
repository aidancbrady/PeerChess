package com.aidancbrady.peerchess.ai;

import java.util.List;

import com.aidancbrady.peerchess.ChessComponent;
import com.aidancbrady.peerchess.MoveAction;
import com.aidancbrady.peerchess.PeerChess;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;

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
        
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                ChessMove move = minimax(false);
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
        }).start();
    }
    
    public ChessMove minimax(boolean hint)
    {
        TestBoard board = new TestBoard(chess);
        List<ChessMove> possibleMoves = board.getPossibleMoves(getSide(), true);
        double bestMoveScore = -9999;
        ChessMove bestMove = null;
        int diff = hint ? 4 : PeerChess.instance().difficulty-1;
        
        for(ChessMove move : possibleMoves)
        {
            double delta = board.applyMove(move);
            double score = minimax_do(diff, board, -10000, 10000, false);
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
    
    public Side getSide()
    {
        return chess.getGame().turn;
    }
    
    public void reset() 
    {
        terminate = true;
    }
}
