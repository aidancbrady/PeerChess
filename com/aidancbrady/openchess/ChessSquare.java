package com.aidancbrady.openchess;

import java.awt.Graphics;

import javax.swing.JComponent;

public class ChessSquare extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	public boolean color;
	
	public ChessSquare(boolean c, int x, int y)
	{
		color = c;
		
		setSize(96, 96);
		setLocation(x*96, y*96);
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
	}
}
