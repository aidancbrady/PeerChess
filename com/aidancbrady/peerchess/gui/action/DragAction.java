package com.aidancbrady.peerchess.gui.action;

import java.awt.Graphics;
import java.awt.Point;

import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.gui.ChessComponent;
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
        getComponent().repaint();
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
        if(hoverPanel != null && getComponent().possibleMoves.contains(hoverPanel.getSquare().getPos()))
        {
            ChessMove move = new ChessMove(startPiece.getSquare().getPos(), hoverPanel.getSquare().getPos());
            
            if(getPiece().getType().getPiece().validateMove(getComponent().getGame(), move))
            {
                ChessPiece newPiece = getPiece();
                
                if(getPiece().getType() == PieceType.PAWN)
                {
                    if((getPiece().getSide() == Side.WHITE && move.toPos.getY() == 0) || (getPiece().getSide() == Side.BLACK && move.toPos.getY() == 7))
                    {
                        newPiece = PeerUtils.getPawnReplace(getPiece().getSide(), getComponent().panel.getPawnReplaceIndex());
                        newPiece.setMoves(getPiece().getMoves());
                    }
                }
                
                getComponent().currentMove = new MoveAction(getComponent(), move, getPiece(), newPiece);
                getComponent().currentMove.setNoAnimation();
                
                if(getComponent().multiplayer)
                {
                    getComponent().currentMove.broadcast();
                }
                
                getComponent().currentDrag = null;
                getComponent().select(null);
            }
        }
        else if(hoverPanel != null && hoverPanel == startPiece)
        {
            getComponent().select(startPiece.getSquare());
            getComponent().currentDrag = null;
        }
        else {
            getComponent().possibleMoves.clear();
            getComponent().currentDrag = null;
            getComponent().repaint();
        }
        
        getComponent().panel.updateText();
    }
    
    public ChessSquarePanel getSquarePanel()
    {
        return startPiece;
    }
    
    private ChessComponent getComponent()
    {
        return startPiece.getComponent();
    }
    
    private ChessPiece getPiece()
    {
        return startPiece.getSquare().getPiece();
    }
    
    private int getScale()
    {
        return (int)(96*(double)getComponent().getWidth()/768D);
    }
    
    private double getX()
    {
        return mousePoint.x - getComponent().getLocationOnScreen().getX() - getScale()/2;
    }
    
    private double getY()
    {
        return mousePoint.y - getComponent().getLocationOnScreen().getY() - getScale()/2;
    }
}
