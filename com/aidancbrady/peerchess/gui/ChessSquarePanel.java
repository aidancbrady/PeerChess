package com.aidancbrady.peerchess.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JComponent;

import com.aidancbrady.peerchess.PeerChess;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.client.Assets;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;
import com.aidancbrady.peerchess.gui.action.DragAction;
import com.aidancbrady.peerchess.gui.action.MoveAction;

public class ChessSquarePanel extends JComponent implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private ChessComponent component;
	
	private ChessSquare square;
	
	public ChessSquarePanel(ChessComponent com, ChessSquare s)
	{
		component = com;
		square = s;
		
		setSize(96, 96);
		setLocation(square.getPos().getX()*96, square.getPos().getY()*96);
		setFocusable(false);
		addMouseListener(this);
		
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if(!component.isDragging() && component.getGame().getTurn() == component.getGame().getSide()) 
                {
                    List<ChessPos> possibleMoves = component.getGame().getValidatedMoves(square);
                    
                    if(square.getPiece() != null && square.getPiece().getSide() == component.getGame().getSide() && !possibleMoves.isEmpty())
                    {
                        component.select(null);
                        component.possibleMoves.addAll(possibleMoves);
                        component.repaint();
                        component.currentDrag = new DragAction(ChessSquarePanel.this);
                        component.panel.updateText();
                        component.currentHint = null;
                    }
                }
                
                if(component.isDragging())
                {
                    component.currentDrag.updateMouse(e.getLocationOnScreen());
                }
            }
        
            @Override
            public void mouseMoved(MouseEvent e) {}
        });
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		if(square.isBlack())
		{
			Assets.black.draw(g, 0, 0, getWidth(), getHeight());
		}
		else {
			Assets.white.draw(g, 0, 0, getWidth(), getHeight());	
		}
		
		if(square.getPiece() != null && square.getPiece().getTexture() != null)
		{
		    if(!component.isDragging() || component.currentDrag.getSquarePanel() != this)
	        {
		        square.getPiece().getTexture().draw(g, 0, 0, getWidth(), getHeight());
	        }
		}
		
		if(PeerChess.instance().enableVisualGuides)
		{
    	    if(square.getPiece() != null && square.getPiece().getType() == PieceType.KING && component.getGame().getSideInCheck() == square.getPiece().getSide())
    	    {
    	        if(!component.isMoving())
    	        {
    	            Assets.check.draw(g, 0, 0, getWidth(), getHeight());
    	        }
    	    }
    	    
    	    if(component.possibleMoves.contains(square.getPos()))
    	    {
    	        Assets.possible.draw(g, 0, 0, getWidth(), getHeight());
    	    }
		}
		
		if(component.currentHint != null && (component.currentHint.toPos.equals(square.getPos()) || component.currentHint.fromPos.equals(square.getPos())))
		{
		    Assets.hint.draw(g, 0, 0, getWidth(), getHeight());
		}
		
		if(component.getSelected() == square)
		{
			Assets.select.draw(g, 0, 0, getWidth(), getHeight());
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
	    if(component.isDragging())
	    {
	        component.currentDrag.enter(this);
	    }
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
	    if(component.isDragging())
        {
            component.currentDrag.exit(this);
        }
	}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		if(component.isMoving() || component.getGame().getTurn() != component.getGame().getSide() || component.getGame().getEndgame() != null)
		{
			return;
		}
		
		if(component.multiplayer && component.panel.getConnection() == null)
		{
		    return;
		}
		
		if(component.isDragging())
		{
		    component.currentDrag.release();
		    return;
		}
		
		if(component.getSelected() != null && component.getSelected().getPiece() == null)
		{
		    component.select(null);
		}
		
		if(arg0.getX() >= 0 && arg0.getX() <= getWidth() && arg0.getY() >= 0 && arg0.getY() <= getHeight())
		{
			if(component.getSelected() == null && square.getPiece() != null)
			{
				if(component.getGame().getSide() != square.getPiece().getSide())
				{
					return;
				}
				
				component.select(square);
			}
			else {
				if(component.getSelected() != null)
				{
					ChessPiece piece = component.getSelected().getPiece();
					ChessMove move = new ChessMove(component.getSelected().getPos(), square.getPos());
					
					if(square.getPiece() != null && square.getPiece().getSide() == piece.getSide())
					{
						component.select(square);
						repaint();
						component.panel.repaint();
						
						return;
					}
					else {
						if(piece.getType().getPiece().validateMove(component.getGame(), move))
						{
							ChessPiece newPiece = piece;
							
							if(piece.getType() == PieceType.PAWN)
							{
								if((piece.getSide() == Side.WHITE && move.toPos.getY() == 0) || (piece.getSide() == Side.BLACK && move.toPos.getY() == 7))
								{
									newPiece = PeerUtils.getPawnReplace(piece.getSide(), component.panel.getPawnReplaceIndex());
									newPiece.setMoves(piece.getMoves());
								}
							}
							
							component.currentMove = new MoveAction(component, move, piece, newPiece);
							component.panel.updateText();
							
							if(component.multiplayer)
							{
							    component.currentMove.broadcast();
							}
						}
					}
				}
				
				component.select(null);
			}
			
			repaint();
			component.panel.repaint();
		}
	}
	
	public ChessComponent getComponent()
	{
	    return component;
	}
	
	public ChessSquare getSquare()
	{
	    return square;
	}
}
