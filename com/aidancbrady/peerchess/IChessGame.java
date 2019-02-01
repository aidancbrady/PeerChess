package com.aidancbrady.peerchess;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aidancbrady.peerchess.game.BoardCache;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;
import com.aidancbrady.peerchess.piece.Piece;

public interface IChessGame 
{
    public ChessSquare[][] getGrid();
    
    public List<ChessMove> getPastMoves();
    
    public BoardCache getCache();
    
    public default boolean isCheckMate(Side side, boolean requiresCheck)
    {
        ChessPos pos = findKing(side);
        if(requiresCheck && !isInCheck(side, pos)) return false;
        
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                if(getGrid()[x][y].getPiece() != null && getGrid()[x][y].getPiece().getSide() == side)
                {
                    ChessPiece piece = getGrid()[x][y].getPiece();
                    
                    for(ChessPos newPos : piece.getType().getPiece().getCurrentPossibleMoves(this, new ChessPos(x, y), true))
                    {
                        ChessMove move = new ChessMove(new ChessPos(x, y), newPos);
                        ChessPos kingPos = piece.getType() == PieceType.KING ? newPos : pos;
                        
                        if(!testCheck(side, kingPos, move))
                        {
                            return false;
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    public default boolean testCheck(Side side, ChessPos pos, ChessMove move)
    {
        move.testApplyMove(this);
        boolean inCheck = isInCheck(side, pos);
        move.testRevertMove(this);
        return inCheck;
    }
    
    public default boolean isInCheck(Side side, ChessPos pos)
    {
        Set<ChessPos> moves = PeerUtils.getValidKnightMoves(pos);
        
        for(ChessPos iterPos : moves)
        {
            if(iterPos.getSquare(getGrid()).getPiece() != null && iterPos.getSquare(getGrid()).getPiece().getSide() != side && iterPos.getSquare(getGrid()).getPiece().getType() == PieceType.KNIGHT)
            {
                return true;
            }
        }
        
        moves = PeerUtils.getValidPawnAttackMoves(pos, side);
        
        for(ChessPos iterPos : moves)
        {
            if(iterPos.getX() != pos.getX())
            {
                if(iterPos.getSquare(getGrid()).getPiece() != null && iterPos.getSquare(getGrid()).getPiece().getSide() != side && iterPos.getSquare(getGrid()).getPiece().getType() == PieceType.PAWN)
                {
                    return true;
                }
            }
        }
        
        int xPointer = pos.getX();
        int yPointer = pos.getY();
        
        while(xPointer < 7)
        {
            xPointer++;
            
            if(getGrid()[xPointer][pos.getY()].getPiece() != null)
            {
                ChessPiece piece = getGrid()[xPointer][pos.getY()].getPiece();
                
                if(piece.getSide() != side)
                {
                    if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1)
                    {
                        return true;
                    }
                    else if(piece.getType() == PieceType.CASTLE || piece.getType() == PieceType.QUEEN)
                    {
                        return true;
                    }
                }
                
                break;
            }
        }
        
        xPointer = pos.getX();
        yPointer = pos.getY();
        
        while(xPointer > 0)
        {
            xPointer--;
            
            if(getGrid()[xPointer][pos.getY()].getPiece() != null)
            {
                ChessPiece piece = getGrid()[xPointer][pos.getY()].getPiece();
                
                if(piece.getSide() != side)
                {
                    if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1)
                    {
                        return true;
                    }
                    else if(piece.getType() == PieceType.CASTLE || piece.getType() == PieceType.QUEEN)
                    {
                        return true;
                    }
                }
                
                break;
            }
        }
        
        xPointer = pos.getX();
        yPointer = pos.getY();
        
        while(yPointer < 7)
        {
            yPointer++;
            
            if(getGrid()[pos.getX()][yPointer].getPiece() != null)
            {
                ChessPiece piece = getGrid()[pos.getX()][yPointer].getPiece();
                
                if(piece.getSide() != side)
                {
                    if(piece.getType() == PieceType.KING && Math.abs(pos.getY()-yPointer) == 1)
                    {
                        return true;
                    }
                    else if(piece.getType() == PieceType.CASTLE || piece.getType() == PieceType.QUEEN)
                    {
                        return true;
                    }
                }
                
                break;
            }
        }
        
        yPointer = pos.getY();
        
        while(yPointer > 0)
        {
            yPointer--;
            
            if(getGrid()[pos.getX()][yPointer].getPiece() != null)
            {
                ChessPiece piece = getGrid()[pos.getX()][yPointer].getPiece();
                
                if(piece.getSide() != side)
                {
                    if(piece.getType() == PieceType.KING && Math.abs(pos.getY()-yPointer) == 1)
                    {
                        return true;
                    }
                    else if(piece.getType() == PieceType.CASTLE || piece.getType() == PieceType.QUEEN)
                    {
                        return true;
                    }
                }
                
                break;
            }
        }
        
        xPointer = pos.getX();
        yPointer = pos.getY();
        
        while(xPointer < 7 && yPointer < 7)
        {
            xPointer++;
            yPointer++;
            
            if(getGrid()[xPointer][yPointer].getPiece() != null)
            {
                ChessPiece piece = getGrid()[xPointer][yPointer].getPiece();
                
                if(piece.getSide() != side)
                {
                    if(piece.getType() == PieceType.BISHOP || piece.getType() == PieceType.QUEEN)
                    {
                        return true;
                    }
                    else if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1 && Math.abs(pos.getY()-yPointer) == 1)
                    {
                        return true;
                    }
                }
                
                break;
            }
        }
        
        xPointer = pos.getX();
        yPointer = pos.getY();
        
        while(xPointer > 0 && yPointer > 0)
        {
            xPointer--;
            yPointer--;
            
            if(getGrid()[xPointer][yPointer].getPiece() != null)
            {
                ChessPiece piece = getGrid()[xPointer][yPointer].getPiece();
                
                if(piece.getSide() != side)
                {
                    if(piece.getType() == PieceType.BISHOP || piece.getType() == PieceType.QUEEN)
                    {
                        return true;
                    }
                    else if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1 && Math.abs(pos.getY()-yPointer) == 1)
                    {
                        return true;
                    }
                }
                
                break;
            }
        }
        
        xPointer = pos.getX();
        yPointer = pos.getY();
        
        while(xPointer < 7 && yPointer > 0)
        {
            xPointer++;
            yPointer--;
            
            if(getGrid()[xPointer][yPointer].getPiece() != null)
            {
                ChessPiece piece = getGrid()[xPointer][yPointer].getPiece();
                
                if(piece.getSide() != side)
                {
                    if(piece.getType() == PieceType.BISHOP || piece.getType() == PieceType.QUEEN)
                    {
                        return true;
                    }
                    else if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1 && Math.abs(pos.getY()-yPointer) == 1)
                    {
                        return true;
                    }
                }
                
                break;
            }
        }
        
        xPointer = pos.getX();
        yPointer = pos.getY();
        
        while(xPointer > 0 && yPointer < 7)
        {
            xPointer--;
            yPointer++;
            
            if(getGrid()[xPointer][yPointer].getPiece() != null)
            {
                ChessPiece piece = getGrid()[xPointer][yPointer].getPiece();
                
                if(piece.getSide() != side)
                {
                    if(piece.getType() == PieceType.BISHOP || piece.getType() == PieceType.QUEEN)
                    {
                        return true;
                    }
                    else if(piece.getType() == PieceType.KING && Math.abs(pos.getX()-xPointer) == 1 && Math.abs(pos.getY()-yPointer) == 1)
                    {
                        return true;
                    }
                }
                
                break;
            }
        }
        
        return false;
    }
    
    public default ChessPos findKing(Side side)
    {
        for(ChessSquare[] array : getGrid())
        {
            for(ChessSquare square : array)
            {
                if(square.getPiece() != null)
                {
                    if(square.getPiece().getType() == PieceType.KING && square.getPiece().getSide() == side)
                    {
                        return square.getPos().clone();
                    }
                }
            }
        }
        
        return null;
    }
    
    public default Set<ChessPos> getValidatedMoves(ChessSquare square)
    {
        Set<ChessPos> ret = new HashSet<>();
        
        if(square.getPiece() == null) return ret;
        
        Piece piece = square.getPiece().getType().getPiece();
        
        for(ChessPos pos : piece.getCurrentPossibleMoves(this, square.getPos(), true))
        {
            ChessMove move = new ChessMove(square.getPos(), pos);
            
            if(piece.validateMove(this, move))
            {
                ret.add(pos);
            }
        }
        
        return ret;
    }
}
