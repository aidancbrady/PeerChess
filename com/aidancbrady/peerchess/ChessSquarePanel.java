package com.aidancbrady.peerchess;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import com.aidancbrady.peerchess.ChessPiece.PieceType;
import com.aidancbrady.peerchess.ChessPiece.Side;

public class ChessSquarePanel extends JComponent implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	public ChessComponent component;
	
	public ChessSquare square;
	
	public ChessSquarePanel(ChessComponent com, ChessSquare s)
	{
		component = com;
		square = s;
		
		setSize(96, 96);
		setLocation(square.getPos().xPos*96, square.getPos().yPos*96);
		setFocusable(false);
		addMouseListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		if(square.isBlack())
		{
			ChessComponent.black.draw(g, 0, 0, getWidth(), getHeight());
		}
		else {
			ChessComponent.white.draw(g, 0, 0, getWidth(), getHeight());	
		}
		
		if(square.getPiece() != null && square.getPiece().getTexture() != null)
		{
			square.getPiece().getTexture().draw(g, 0, 0, getWidth(), getHeight());
		}
		
		if(PeerChess.instance().enableVisualGuides)
		{
    	    if(square.getPiece() != null && square.getPiece().type == PieceType.KING && component.sideInCheck == square.getPiece().side)
    	    {
    	        if(component.currentAnimation == null)
    	        {
    	            ChessComponent.check.draw(g, 0, 0, getWidth(), getHeight());
    	        }
    	    }
    	    
    	    if(component.possibleMoves.contains(square.getPos()))
    	    {
    	        ChessComponent.possible.draw(g, 0, 0, getWidth(), getHeight());
    	    }
		}
		
		if(component.selected == square)
		{
			ChessComponent.select.draw(g, 0, 0, getWidth(), getHeight());
		}
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
		
		if(component.selected != null && component.selected.getPiece() == null)
		{
		    component.select(null);
		}
		
		if(arg0.getX() >= 0 && arg0.getX() <= getWidth() && arg0.getY() >= 0 && arg0.getY() <= getHeight())
		{
			if(component.selected == null && square.getPiece() != null)
			{
				if(component.side != square.getPiece().side)
				{
					return;
				}
				
				component.select(square);
			}
			else {
				if(component.selected != null)
				{
					ChessPiece piece = component.selected.getPiece();
					ChessMove move = new ChessMove(component.selected.getPos(), square.getPos());
					
					if(square.getPiece() != null && square.getPiece().side == piece.side)
					{
						component.select(square);
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
