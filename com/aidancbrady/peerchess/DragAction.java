package com.aidancbrady.peerchess;

import java.awt.Graphics;
import java.awt.Point;

import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.gui.ChessSquarePanel;

public class DragAction
{
    private ChessSquarePanel startPiece;
    
    private Point mousePoint;
    private ChessSquarePanel hoverPanel;
    
    public DragAction(ChessSquarePanel panel)
    {
        startPiece = hoverPanel = panel;
    }
    
    public void render(Graphics g)
    {
        if(getPiece() != null)
        {
            getPiece().getTexture().draw(g, (int)getX(), (int)getY(), getScale(), getScale());
        }
    }
    
    public void updateMouse(Point point)
    {
        mousePoint = point;
        getGame().repaint();
    }
    
    public void enter(ChessSquarePanel panel)
    {
        hoverPanel = panel;
    }
    
    public void exit(ChessSquarePanel panel)
    {
        hoverPanel = null;
    }
    
    public void release()
    {
        if(hoverPanel != null && getGame().possibleMoves.contains(hoverPanel.square.getPos()))
        {
            ChessMove move = new ChessMove(startPiece.square.getPos(), hoverPanel.square.getPos());
            
            if(getPiece().type.getPiece().validateMove(getGame(), move))
            {
                ChessPiece newPiece = getPiece();
                
                if(getPiece().type == PieceType.PAWN)
                {
                    if((getPiece().side == Side.WHITE && move.toPos.yPos == 0) || (getPiece().side == Side.BLACK && move.toPos.yPos == 7))
                    {
                        getGame().panel.pawnReplace %= PieceType.values().length-1;
                        newPiece = PeerUtils.getPawnReplace(getPiece().side, getGame().panel.pawnReplace);
                        newPiece.moves = getPiece().moves;
                    }
                }
                
                getGame().currentMove = new MoveAction(getGame(), move, getPiece(), newPiece);
                getGame().currentMove.setNoAnimation();
                
                if(getGame().multiplayer)
                {
                    getGame().currentMove.broadcast();
                }
                
                getGame().currentDrag = null;
                getGame().select(null);
            }
        }
        else {
            getGame().possibleMoves.clear();
            getGame().currentDrag = null;
            getGame().repaint();
        }
    }
    
    public ChessSquarePanel getSquarePanel()
    {
        return startPiece;
    }
    
    private ChessComponent getGame()
    {
        return startPiece.game;
    }
    
    private ChessPiece getPiece()
    {
        return startPiece.square.getPiece();
    }
    
    private int getScale()
    {
        return (int)(96*(double)getGame().getWidth()/768D);
    }
    
    private double getX()
    {
        return mousePoint.x - getGame().getLocationOnScreen().getX() - getScale()/2;
    }
    
    private double getY()
    {
        return mousePoint.y - getGame().getLocationOnScreen().getY() - getScale()/2;
    }
}
