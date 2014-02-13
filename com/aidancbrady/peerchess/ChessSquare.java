package com.aidancbrady.peerchess;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

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
		setFocusable(true);
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
			ChessComponent.black.draw(g, 0, 0, 96, 96);
		}
		else {
			ChessComponent.white.draw(g, 0, 0, 96, 96);	
		}
		
		if(housedPiece != null && housedPiece.texture != null)
		{
			housedPiece.texture.draw(g, 0, 0, 96, 96);
		}
		
		if(component.selected == this)
		{
			ChessComponent.select.draw(g, 0, 0, 96, 96);
		}
	}
	
	public void setPiece(ChessPiece piece)
	{
		housedPiece = piece;
		
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{

	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		if(component.isMoving() || component.turn != component.side)
		{
			return;
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
						if(piece.type.getPiece().canMove(component.grid, move))
						{
							component.selected.setPiece(null);
							component.currentAnimation = new MoveAnimation(component, move, piece);
						}
					}
				}
				
				component.select(null);
			}
			
			repaint();
		}
	}
	
	public void repaint()
	{
		super.repaint();
		component.repaint();
	}
}
