package com.aidancbrady.openchess;

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
	
	public int xPos;
	public int yPos;
	
	public ChessSquare(ChessComponent com, boolean c, int x, int y)
	{
		component = com;
		color = c;
		
		xPos = x;
		yPos = y;
		
		setSize(96, 96);
		setLocation(x*96, y*96);
		setFocusable(true);
		addMouseListener(this);
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
		
		if(component.selected == this)
		{
			ChessComponent.select.draw(g, 0, 0, 96, 96);
		}
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
		component.select(this);
		
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		
	}
}
