package com.aidancbrady.peerchess.game;

import java.util.List;

public class DrawTracker
{
    public static boolean isDraw(List<ChessMove> moves)
    {
        int fiftyMoveCount = 0;
        
        for(int i = 0; i < moves.size(); i++)
        {
            ChessMove move = moves.get(i);
            
            if(move.didTakeMaterial() || move.isPawnMove())
            {
                fiftyMoveCount = 0;
            }
            else {
                fiftyMoveCount++;
            }
            
            if(i >= 5)
            {
                if(isFlipped(move, moves.get(i-2)) && isEqual(move, moves.get(i-4)))
                {
                    ChessMove opponentMove = moves.get(i-1);
                    
                    if(isFlipped(opponentMove, moves.get(i-3)) && isEqual(opponentMove, moves.get(i-5)))
                    {
                        return true;
                    }
                }
            }
            
            if(fiftyMoveCount >= 50)
            {
                return true;
            }
        }
        
        return false;
    }
    
    private static boolean isFlipped(ChessMove move1, ChessMove move2)
    {
        return move1.fromPos.equals(move2.toPos) && move1.toPos.equals(move2.fromPos);
    }
    
    private static boolean isEqual(ChessMove move1, ChessMove move2)
    {
        return move1.fromPos.equals(move2.fromPos) && move1.toPos.equals(move2.toPos);
    }
}
