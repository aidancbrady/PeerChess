package com.aidancbrady.peerchess.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.aidancbrady.peerchess.IChessGame;
import com.aidancbrady.peerchess.game.ChessPiece.Side;

public class BoardCache
{
    private Map<ChessPos, Set<ChessPos>> attackingMap = new HashMap<>();
    private Map<ChessPos, Set<ChessPos>> scopeMap = new HashMap<>();
    
    private Set<ChessPos> updated = new HashSet<>();
    
    private IChessGame game;
    
    public BoardCache(IChessGame g)
    {
        game = g;
    }
    
    public void reset()
    {
        attackingMap.clear();
        scopeMap.clear();
        
        for(int i = 0; i < 8; i++)
        {
            // all pieces
            updateScope(new ChessPos(i, 0));
            updateScope(new ChessPos(i, 1));
            updateScope(new ChessPos(i, 6));
            updateScope(new ChessPos(i, 7));
            
            // pawns
            updateAttacking(new ChessPos(i, 1));
            updateAttacking(new ChessPos(1, 6));
        }
        
        // knights
        updateAttacking(new ChessPos(1, 0));
        updateAttacking(new ChessPos(1, 6));
        updateAttacking(new ChessPos(1, 7));
        updateAttacking(new ChessPos(6, 7));
    }
    
    public void updateAll()
    {
        attackingMap.clear();
        scopeMap.clear();
        
        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                if(game.getGrid()[x][y].getPiece() != null)
                {
                    updateScope(new ChessPos(x, y));
                    updateAttacking(new ChessPos(x, y));
                }
            }
        }
    }
    
    public void applyMove(ChessMove move)
    {
        // remove 'from' square from scope & attacking maps
        attackingMap.remove(move.fromPos);
        scopeMap.remove(move.fromPos);
        
        // update scope & attacking maps at 'to' position
        updateAttacking(move.toPos);
        updateScope(move.toPos);
        
        // update pieces affected by piece's new position
        updateAffected(move.fromPos);
        updateAffected(move.toPos);
        
        if(move.enPassantTakePos != null) // en passant
        {
            // remove pawn taken by en passant from scope/attacking maps
            attackingMap.remove(move.enPassantTakePos);
            scopeMap.remove(move.enPassantTakePos);
            
            // update pieces affected by en passant taken piece
            updateAffected(move.enPassantTakePos);
        }
        
        if(move.fromPosCastle != null) // in case we castled
        {
            // remove castle from scope/attacking maps
            attackingMap.remove(move.fromPosCastle);
            scopeMap.remove(move.fromPosCastle);
            
            // update scope/attacking maps for castle's new position
            updateAttacking(move.toPosCastle);
            updateScope(move.toPosCastle);
            
            // update pieces affected by castle's old and new positions
            updateAffected(move.fromPosCastle);
            updateAffected(move.toPosCastle);
        }
        
        updated.clear();
    }
    
    public void revertMove(ChessMove move)
    {
        if(move.toPos.getSquare(game.getGrid()).getPiece() == null)
        {
            // remove toPos from maps if there's no piece there after revert
            attackingMap.remove(move.toPos);
            scopeMap.remove(move.toPos);
        }
        else {
            // otherwise, update maps to reflect old piece's cache
            updateAttacking(move.toPos);
            updateScope(move.toPos);
        }
        
        // update scope & attacking maps at piece's original position
        updateAttacking(move.fromPos);
        updateScope(move.fromPos);
        
        // update pieces affected by piece's new position
        updateAffected(move.fromPos);
        updateAffected(move.toPos);
        
        if(move.enPassantTakePos != null) // en passant
        {
            // update scope & attacking maps of en passant taken piece's position
            updateAttacking(move.enPassantTakePos);
            updateScope(move.enPassantTakePos);
            
            // update pieces affected by en passant taken piece
            updateAffected(move.enPassantTakePos);
        }
        
        if(move.fromPosCastle != null) // in case we castled
        {
            // update scope & attacking maps at castle's original position
            updateAttacking(move.fromPosCastle);
            updateScope(move.fromPosCastle);
            
            // remove castle's toPos from scope/attacking maps
            attackingMap.remove(move.toPosCastle);
            scopeMap.remove(move.toPosCastle);
            
            // update pieces affected by castle's old and new positions
            updateAffected(move.fromPosCastle);
            updateAffected(move.toPosCastle);
        }
        
        updated.clear();
    }
    
    public boolean isAttacked(ChessPos pos)
    {
        if(pos == null)
        {
            System.out.println("lol");
        }
        
        Side posSide = pos.getSquare(game.getGrid()).getPiece().getSide();
        
        for(Map.Entry<ChessPos, Set<ChessPos>> entry : attackingMap.entrySet())
        {
            Side attackingSide = entry.getKey().getSquare(game.getGrid()).getPiece().getSide();
            
            if(entry.getValue().contains(pos) && posSide != attackingSide)
            {
                return true;
            }
        }
        
        return false;
    }
    
    private void updateAffected(ChessPos pos)
    {
        for(ChessPos iterPos : getAffected(pos))
        {
            if(!updated.contains(iterPos))
            {
                updateAttacking(iterPos);
                updated.add(iterPos);
            }
        }
    }
    
    private Set<ChessPos> getAffected(ChessPos pos)
    {
        Set<ChessPos> ret = new HashSet<>();
        
        for(Map.Entry<ChessPos, Set<ChessPos>> entry : scopeMap.entrySet())
        {
            if(entry.getValue().contains(pos))
            {
                ret.add(entry.getKey());
            }
        }
        
        return ret;
    }
    
    private void updateScope(ChessPos pos)
    {
        Set<ChessPos> scope = pos.getSquare(game.getGrid()).getPiece().getType().getPiece().getCurrentPossibleMoves(game, pos, false);
        scopeMap.put(pos, scope);
    }
    
    private void updateAttacking(ChessPos pos)
    {
        Set<ChessPos> attacking = pos.getSquare(game.getGrid()).getPiece().getType().getPiece().getCurrentPossibleMoves(game, pos, true);
        attackingMap.put(pos, attacking);
    }
    
    public BoardCache copy(IChessGame newGame)
    {
        BoardCache cache = new BoardCache(newGame);
        cache.attackingMap.putAll(attackingMap);
        cache.scopeMap.putAll(scopeMap);
        return cache;
    }
}
