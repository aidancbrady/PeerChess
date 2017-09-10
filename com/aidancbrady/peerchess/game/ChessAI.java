package com.aidancbrady.peerchess.game;

import java.util.ArrayList;
import java.util.List;

import com.aidancbrady.peerchess.ChessComponent;
import com.aidancbrady.peerchess.MoveAction;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;

public class ChessAI 
{
    private ChessComponent chess;
    private int MAX_DEPTH = 4;
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
        List<ChessMove> possibleMoves = getPossibleMoves(chess.grid, true);
        double bestMoveScore = -9999;
        ChessMove bestMove = null;
        
        for(ChessMove move : possibleMoves)
        {
            ChessSquare[][] grid = getFakeGrid(chess.grid, move);
            double score = minimax_do(MAX_DEPTH-1, grid, -10000, 10000, false);
            
            if(score >= bestMoveScore)
            {
                bestMoveScore = score;
                bestMove = move;
            }
        }
        
        return bestMove;
    }
    
    public double minimax_do(int depth, ChessSquare[][] grid, double alpha, double beta, boolean maximizing)
    {
        evaluations++;
        
        if(depth == 0)
        {
            return getSide() == Side.WHITE ? evaluateBoard(grid) : -evaluateBoard(grid);
        }
        
        double bestMoveScore = maximizing ? -9999 : 9999;
        
        List<ChessMove> possibleMoves = getPossibleMoves(grid, maximizing);
        
        for(ChessMove move : possibleMoves)
        {
            ChessSquare[][] newGrid = getFakeGrid(grid, move);
            
            if(maximizing)
            {
                bestMoveScore = Math.max(bestMoveScore, minimax_do(depth - 1, newGrid, alpha, beta, !maximizing));
                alpha = Math.max(alpha, bestMoveScore);
            }
            else {
                bestMoveScore = Math.min(bestMoveScore, minimax_do(depth - 1, newGrid, alpha, beta, !maximizing));
                beta = Math.min(beta, bestMoveScore);
            }
            
            if(beta <= alpha)
            {
                return bestMoveScore;
            }
        }
        
        return bestMoveScore;
    }
    
    public ChessSquare[][] getFakeGrid(ChessSquare[][] grid, ChessMove move)
    {
        ChessSquare[][] ret = move.getFakeGrid(grid);
        
        if(move.getFromSquare(grid).getPiece().type == PieceType.PAWN)
        {
            if(move.toPos.yPos == 0 || move.toPos.yPos == 7)
            {
                ChessPiece oldPiece = move.getFromSquare(grid).getPiece();
                move.getToSquare(ret).setPiece(new ChessPiece(PieceType.QUEEN, oldPiece.side));
            }
        }
        
        return ret;
    }
    
    public double evaluateBoard(ChessSquare[][] grid)
    {
        double total = 0;
        
        for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                total += getSquareValue(grid, new ChessPos(x, y));
            }
        }
        
        return total;
    }
    
    public double getSquareValue(ChessSquare[][] grid, ChessPos pos)
    {
        ChessPiece piece = pos.getSquare(grid).getPiece();
        
        if(piece == null)
        {
            return 0;
        }
        
        double value = piece.type.getPiece().getPointValue();
        
        if(piece.type == PieceType.PAWN)
        {
            value += piece.side == Side.WHITE ? pawnEvalWhite[pos.xPos][pos.yPos] : pawnEvalBlack[pos.xPos][pos.yPos];
        }
        else if(piece.type == PieceType.BISHOP)
        {
            value += piece.side == Side.WHITE ? bishopEvalWhite[pos.xPos][pos.yPos] : bishopEvalBlack[pos.xPos][pos.yPos];
        }
        else if(piece.type == PieceType.KNIGHT)
        {
            value += knightEval[pos.xPos][pos.yPos];
        }
        else if(piece.type == PieceType.CASTLE)
        {
            value += piece.side == Side.WHITE ? castleEvalWhite[pos.xPos][pos.yPos] : castleEvalBlack[pos.xPos][pos.yPos];
        }
        else if(piece.type == PieceType.QUEEN)
        {
            value += queenEval[pos.xPos][pos.yPos];
        }
        else if(piece.type == PieceType.KING)
        {
            value += piece.side == Side.WHITE ? kingEvalWhite[pos.xPos][pos.yPos] : kingEvalBlack[pos.xPos][pos.yPos];
        }
        
        return piece.side == Side.WHITE ? value : -value;
    }
    
    public List<ChessMove> getPossibleMoves(ChessSquare[][] grid, boolean maximizing)
    {
        Side sideToTest = maximizing ? getSide() : getSide().getOpposite();
        List<ChessMove> possibleMoves = new ArrayList<>();
        
        for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                if(grid[x][y].getPiece() != null && grid[x][y].getPiece().side == sideToTest)
                {
                    ChessPiece piece = grid[x][y].getPiece();
                    ChessPos origPos = new ChessPos(x, y);
                    
                    for(ChessPos pos : piece.type.getPiece().getCurrentPossibleMoves(grid, origPos))
                    {
                        ChessMove move = new ChessMove(origPos, pos);
                        
                        if(piece.type.getPiece().validateMove(grid, move))
                        {
                            possibleMoves.add(move);
                        }
                    }
                }
            }
        }
        
        return possibleMoves;
    }
    
    public Side getSide()
    {
        return chess.turn;
    }
    
    public static double[][] reverseArray(double[][] array)
    {
        double[][] ret = new double[8][8];
        
        for(int i = 0; i < array.length; i++)
        {
            ret[i] = array[array.length-i-1];
        }
        
        return ret;
    }
    
    public void reset() 
    {
        terminate = true;
    }
    
    public static double[][] pawnEvalWhite = {
        {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
        {5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0},
        {1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0,  1.0},
        {0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5,  0.5},
        {0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0,  0.0},
        {0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5,  0.5},
        {0.5,  1.0, 1.0,  -2.0, -2.0,  1.0,  1.0,  0.5},
        {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0}
    };

    public static double[][] pawnEvalBlack = reverseArray(pawnEvalWhite);

    public static double[][] knightEval = {
        {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
        {-4.0, -2.0,  0.0,  0.0,  0.0,  0.0, -2.0, -4.0},
        {-3.0,  0.0,  1.0,  1.5,  1.5,  1.0,  0.0, -3.0},
        {-3.0,  0.5,  1.5,  2.0,  2.0,  1.5,  0.5, -3.0},
        {-3.0,  0.0,  1.5,  2.0,  2.0,  1.5,  0.0, -3.0},
        {-3.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -3.0},
        {-4.0, -2.0,  0.0,  0.5,  0.5,  0.0, -2.0, -4.0},
        {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}
    };

    public static double[][] bishopEvalWhite = {
        {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
        {-1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
        {-1.0,  0.0,  0.5,  1.0,  1.0,  0.5,  0.0, -1.0},
        {-1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5, -1.0},
        {-1.0,  0.0,  1.0,  1.0,  1.0,  1.0,  0.0, -1.0},
        {-1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0, -1.0},
        {-1.0,  0.5,  0.0,  0.0,  0.0,  0.0,  0.5, -1.0},
        {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}
    };

    public static double[][] bishopEvalBlack = reverseArray(bishopEvalWhite);

    public static double[][] castleEvalWhite = {
        { 0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
        { 0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5},
        {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
        {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
        {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
        {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
        {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
        { 0.0,   0.0, 0.0,  0.5,  0.5,  0.0,  0.0,  0.0}
    };

    public static double[][] castleEvalBlack = reverseArray(castleEvalWhite);

    public static double[][] queenEval = {
        {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
        {-1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
        {-1.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
        {-0.5,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
        { 0.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
        {-1.0,  0.5,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
        {-1.0,  0.0,  0.5,  0.0,  0.0,  0.0,  0.0, -1.0},
        {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}
    };

    public static double[][] kingEvalWhite = {
        {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
        {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
        {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
        {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
        {-2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
        {-1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
        { 2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0},
        { 2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0}
    };

    public static double[][] kingEvalBlack = reverseArray(kingEvalWhite);
}