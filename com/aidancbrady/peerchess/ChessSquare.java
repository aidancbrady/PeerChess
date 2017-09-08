package com.aidancbrady.peerchess;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import com.aidancbrady.peerchess.ChessPiece.PieceType;
import com.aidancbrady.peerchess.ChessPiece.Side;

public class ChessSquare extends JComponent implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	public boolean color;
	
	public ChessComponent component;
	
	public ChessPiece housedPiece;
	
	public ChessPos pos;
	
	public ChessSquare(ChessComponent com, boolean c, ChessPos p)
	{
		component = com;
		color = c;
		pos = p;
		
		setSize(96, 96);
		setLocation(p.xPos*96, p.yPos*96);
		setFocusable(false);
		addMouseListener(this);
	}
	
	public ChessPos getPos()
	{
		return pos;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		if(color)
		{
			ChessComponent.black.draw(g, 0, 0, getWidth(), getHeight());
		}
		else {
			ChessComponent.white.draw(g, 0, 0, getWidth(), getHeight());	
		}
		
		if(housedPiece != null && housedPiece.getTexture() != null)
		{
			housedPiece.getTexture().draw(g, 0, 0, getWidth(), getHeight());
		}
		
		if(PeerChess.instance().enableVisualGuides)
		{
    	    if(housedPiece != null && housedPiece.type == PieceType.KING && component.sideInCheck == housedPiece.side)
    	    {
    	        if(component.currentAnimation == null)
    	        {
    	            ChessComponent.check.draw(g, 0, 0, getWidth(), getHeight());
    	        }
    	    }
    	    
    	    if(component.possibleMoves.contains(pos))
    	    {
    	        ChessComponent.possible.draw(g, 0, 0, getWidth(), getHeight());
    	    }
		}
		
		if(component.selected == this)
		{
			ChessComponent.select.draw(g, 0, 0, getWidth(), getHeight());
		}
	}
	
	public void setPiece(ChessPiece piece)
	{
		housedPiece = piece;
		
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		if(component.isMoving() || component.turn != component.side || component.endgame != null)
		{
			return;
		}
		
		if(component.multiplayer && component.panel.connection == null)
		{
		    return;
		}
		
		if(component.selected != null && component.selected.housedPiece == null)
		{
		    component.select(null);
		}
		
		if(arg0.getX() >= 0 && arg0.getX() <= getWidth() && arg0.getY() >= 0 && arg0.getY() <= getHeight())
		{
			if(component.selected == null && housedPiece != null)
			{
				if(component.side != housedPiece.side)
				{
					return;
				}
				
				component.select(this);
			}
			else {
				if(component.selected != null)
				{
					ChessPiece piece = component.selected.housedPiece;
					ChessMove move = new ChessMove(component.selected.getPos(), getPos());
					
					if(housedPiece != null && housedPiece.side == piece.side)
					{
						component.select(this);
						repaint();
						
						return;
					}
					else {
						if(piece.type.getPiece().validateMove(component.grid, move))
						{
							ChessPiece newPiece = piece;
							
							if(piece.type == PieceType.PAWN)
							{
								if(piece.side == Side.WHITE && move.toPos.yPos == 0)
								{
									component.panel.pawnReplace %= PieceType.values().length;
									newPiece = ChessPiece.getPieceList(Side.WHITE).get(component.panel.pawnReplace);
									newPiece = newPiece.copyWithMoves(piece.moves);
								}
								else if(piece.side == Side.BLACK && move.toPos.yPos == 7)
								{
									component.panel.pawnReplace %= PieceType.values().length;
									newPiece = ChessPiece.getPieceList(Side.BLACK).get(component.panel.pawnReplace);
									newPiece = newPiece.copyWithMoves(piece.moves);
								}
							}
							
							component.currentAnimation = new MoveAction(component, move, piece, newPiece);
							
							if(component.multiplayer)
							{
							    component.panel.connection.move(component.currentAnimation);
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
}
