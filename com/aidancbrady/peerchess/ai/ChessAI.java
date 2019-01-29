package com.aidancbrady.peerchess.ai;

import java.util.List;

import com.aidancbrady.peerchess.ChessComponent;
import com.aidancbrady.peerchess.MoveAction;
import com.aidancbrady.peerchess.PeerChess;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.DrawTracker;

public class ChessAI 
{
    private ChessComponent chess;
    private boolean terminate = false;
    
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
                
                if(!terminate)
                {
                    ChessPiece finalPiece = piece;
                    
                    if(move.getFromSquare(chess.grid).getPiece().getType() == PieceType.PAWN)
                    {
                        if(move.toPos.getY() == 0 || move.toPos.getY() == 7)
                        {
                            finalPiece = new ChessPiece(PieceType.QUEEN, piece.getSide());
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
        int diff = hint ? 3 : PeerChess.instance().difficulty-1;
        
        for(ChessMove move : possibleMoves)
        {
            double delta = board.applyMove(move);
            double score = board.isCheckmate() ? Integer.MAX_VALUE : minimax_do(diff, board, -10000, 10000, false);
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
        if(depth == 0)
        {
            return getSide() == Side.WHITE ? board.getEvaluation() : -board.getEvaluation();
        }
        
        double bestMoveScore = maximizing ? -9999 : 9999;
        
        List<ChessMove> possibleMoves = board.getPossibleMoves(getSide(), maximizing);
        
        if(possibleMoves.isEmpty() || DrawTracker.isDraw(board.getPastMoves())) // stalemate penalty/bonus
        {
            double eval = getSide() == Side.WHITE ? board.getEvaluation() : -board.getEvaluation();
            return eval < -15 ? board.getEvaluation()+10 : board.getEvaluation()-10;
        }
        
        for(ChessMove move : possibleMoves)
        {
            double delta = board.applyMove(move);
            
            if(move.testTakingKing())
            {
                double eval = board.getEvaluation();
                board.revertMove(move, delta);
                return getSide() == Side.WHITE ? eval : -eval;
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
