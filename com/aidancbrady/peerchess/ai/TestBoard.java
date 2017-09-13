package com.aidancbrady.peerchess.ai;

import java.util.ArrayList;
import java.util.List;

import com.aidancbrady.peerchess.ChessComponent;
import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;

public class TestBoard implements IChessGame
{
    private ChessSquare[][] grid;
    private double currentEvaluation;
    private List<ChessMove> moves;
    
    public TestBoard(ChessComponent game)
    {
        grid = new ChessSquare[8][8];
        moves = new ArrayList<ChessMove>(game.moves);
        
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                grid[x][y] = new ChessSquare(false, new ChessPos(x, y));
                grid[x][y].setPiece(game.grid[x][y].getPiece());
            }
        }
        
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
                if(grid[x][y].getPiece() != null && grid[x][y].getPiece().side == sideToTest)
                {
                    ChessPiece piece = grid[x][y].getPiece();
                    ChessPos origPos = new ChessPos(x, y);
                    
                    for(ChessPos pos : piece.type.getPiece().getCurrentPossibleMoves(this, origPos))
                    {
                        ChessMove move = new ChessMove(origPos, pos);
                        
                        if(piece.type.getPiece().validateMove(this, move))
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
        
        double value = piece.type.getPiece().getPointValue();
        
        if(piece.type == PieceType.PAWN)
        {
            value += piece.side == Side.WHITE ? Constants.pawnEvalWhite[pos.xPos][pos.yPos] : Constants.pawnEvalBlack[pos.xPos][pos.yPos];
        }
        else if(piece.type == PieceType.BISHOP)
        {
            value += piece.side == Side.WHITE ? Constants.bishopEvalWhite[pos.xPos][pos.yPos] : Constants.bishopEvalBlack[pos.xPos][pos.yPos];
        }
        else if(piece.type == PieceType.KNIGHT)
        {
            value += Constants.knightEval[pos.xPos][pos.yPos];
        }
        else if(piece.type == PieceType.CASTLE)
        {
            value += piece.side == Side.WHITE ? Constants.castleEvalWhite[pos.xPos][pos.yPos] : Constants.castleEvalBlack[pos.xPos][pos.yPos];
        }
        else if(piece.type == PieceType.QUEEN)
        {
            value += Constants.queenEval[pos.xPos][pos.yPos];
        }
        else if(piece.type == PieceType.KING)
        {
            value += piece.side == Side.WHITE ? Constants.kingEvalWhite[pos.xPos][pos.yPos] : Constants.kingEvalBlack[pos.xPos][pos.yPos];
        }
        
        return piece.side == Side.WHITE ? value : -value;
    }
    
    public double applyMove(ChessMove move)
    {
        double beforeScore = getSquareValue(move.fromPos) + getSquareValue(move.toPos);
        
        if(move.fromPosCastle != null)
        {
            beforeScore += getSquareValue(move.fromPosCastle) + getSquareValue(move.toPosCastle);
        }
        
        move.testApplyMove(grid);
        
        if(move.getToSquare(grid).getPiece().type == PieceType.PAWN)
        {
            if(move.toPos.yPos == 0 || move.toPos.yPos == 7)
            {
                ChessPiece oldPiece = move.getToSquare(grid).getPiece();
                move.getToSquare(grid).setPiece(new ChessPiece(PieceType.QUEEN, oldPiece.side));
            }
        }
        
        double afterScore = getSquareValue(move.fromPos) + getSquareValue(move.toPos);
        
        if(move.fromPosCastle != null)
        {
            afterScore += getSquareValue(move.fromPosCastle) + getSquareValue(move.toPosCastle);
        }
        
        double delta = afterScore-beforeScore;
        currentEvaluation += delta;
        
        moves.add(move);
        
        return delta;
    }
    
    public void revertMove(ChessMove move, double delta)
    {
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
