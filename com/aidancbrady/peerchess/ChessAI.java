package com.aidancbrady.peerchess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aidancbrady.peerchess.ChessPiece.Side;

public class ChessAI 
{
    private ChessComponent chess;
    private Side side = Side.BLACK;
    private Random rand = new Random();
    
    public ChessAI(ChessComponent component)
    {
        chess = component;
    }
    
    public void triggerMove()
    {
        List<ChessMove> possibleMoves = new ArrayList<>();
        
        for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                if(chess.grid[x][y].housedPiece != null && chess.grid[x][y].housedPiece.side == side)
                {
                    ChessPiece piece = chess.grid[x][y].housedPiece;
                    ChessPos origPos = new ChessPos(x, y);
                    
                    for(ChessPos pos : piece.type.getPiece().getCurrentPossibleMoves(chess.grid, origPos))
                    {
                        ChessMove move = new ChessMove(origPos, pos);
                        
                        if(piece.type.getPiece().validateMove(chess.grid, move))
                        {
                            possibleMoves.add(move);
                        }
                    }
                }
            }
        }
        
        ChessMove move = possibleMoves.get(rand.nextInt(possibleMoves.size()));
        ChessPiece piece = move.fromPos.getSquare(chess.grid).housedPiece;
        
        chess.currentAnimation = new MoveAction(chess, move, piece, piece);
        chess.panel.updateText();
    }
    
    public void reset() {}
}
