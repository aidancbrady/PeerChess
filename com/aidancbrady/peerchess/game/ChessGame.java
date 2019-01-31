package com.aidancbrady.peerchess.game;

import java.util.ArrayList;
import java.util.List;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.ai.ChessAI;
import com.aidancbrady.peerchess.game.ChessPiece.Endgame;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.gui.ChessComponent;

public class ChessGame implements IChessGame
{
    private Side side = Side.WHITE;
    private Side turn = Side.WHITE;
    private Endgame endgame = null;
    private Side sideInCheck = null;
    private List<ChessMove> moves = new ArrayList<ChessMove>();
    private ChessAI chessAI;
    
    private ChessSquare[][] grid = PeerUtils.createEmptyBoard();
    
    private ChessComponent component;
    
    public ChessGame(ChessComponent c)
    {
        component = c;
        chessAI = new ChessAI(component);
    }
    
    public Side getSide()
    {
        return side;
    }
    
    public void setSide(Side s)
    {
        side = s;
    }
    
    public Side getTurn()
    {
        return turn;
    }
    
    public void setTurn(Side s)
    {
        turn = s;
    }
    
    public Endgame getEndgame()
    {
        return endgame;
    }
    
    public void setEndgame(Endgame e)
    {
        endgame = e;
    }
    
    public Side getSideInCheck()
    {
        return sideInCheck;
    }
    
    public void setSideInCheck(Side s)
    {
        sideInCheck = s;
    }
    
    public List<ChessMove> getMoves()
    {
        return moves;
    }
    
    public ChessAI getAI()
    {
        return chessAI;
    }
    
    public void resetMain(Side s, int y)
    {
        grid[0][y].setPiece(new ChessPiece(PieceType.CASTLE, s));
        grid[1][y].setPiece(new ChessPiece(PieceType.KNIGHT, s));
        grid[2][y].setPiece(new ChessPiece(PieceType.BISHOP, s));
        grid[3][y].setPiece(new ChessPiece(PieceType.QUEEN, s));
        grid[4][y].setPiece(new ChessPiece(PieceType.KING, s));
        grid[5][y].setPiece(new ChessPiece(PieceType.BISHOP, s));
        grid[6][y].setPiece(new ChessPiece(PieceType.KNIGHT, s));
        grid[7][y].setPiece(new ChessPiece(PieceType.CASTLE, s));
    }
    
    public void resetPawns(Side s, int y)
    {
        for(int x = 0; x < 8; x++)
        {
            grid[x][y].setPiece(new ChessPiece(PieceType.PAWN, s));
        }
    }
    
    public void resetCenter()
    {
        for(int x = 0; x < 8; x++)
        {
            for(int y = 2; y < 6; y++)
            {
                grid[x][y].setPiece(null);
            }
        }
    }
    
    public void reset()
    {
        resetMain(Side.BLACK, 0);
        resetPawns(Side.BLACK, 1);
        
        resetCenter();
        
        resetPawns(Side.WHITE, 6);
        resetMain(Side.WHITE, 7);
        
        side = Side.WHITE;
        turn = Side.WHITE;
        
        endgame = null;
        sideInCheck = null;
        
        moves.clear();
        
        chessAI.reset();
    }
    
    public void doRevertMove()
    {
        //take back opponent's last move
        ChessMove opponentLast = getMoves().get(getMoves().size()-1);
        opponentLast.doRevertMove(this);
        getMoves().remove(opponentLast);
        
        //take back player's last move
        ChessMove last = getMoves().get(getMoves().size()-1);
        last.doRevertMove(this);
        getMoves().remove(last);
        
        ChessPos opponentKingPos = findKing(getSide().getOpposite());
        ChessPos kingPos = findKing(getSide());
        
        if(isInCheck(getSide().getOpposite(), opponentKingPos))
        {
            setSideInCheck(getSide().getOpposite());
        }
        else if(isInCheck(getSide(), kingPos))
        {
            setSideInCheck(getSide());
        }
        else {
            setSideInCheck(null);
        }
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
