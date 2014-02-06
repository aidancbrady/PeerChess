package com.aidancbrady.openchess;

import java.awt.Graphics;

import javax.swing.JComponent;

import com.aidancbrady.openchess.tex.Texture;

public class ChessComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	public ChessSquare[][] grid = new ChessSquare[8][8];
	
	public ChessSquare selected;
	
	public static Texture white = new Texture("src/resources/icon/white.png");
	public static Texture black = new Texture("src/resources/icon/black.png");
	
	public static Texture select = new Texture("src/resources/icon/select.png");
	
	public ChessComponent()
	{
		setLayout(null);
		
		boolean state = false;
		
		for(int y = 0; y < 8; y++)
		{			
			for(int x = 0; x < 8; x++)
			{
				grid[x][y] = (ChessSquare)add(new ChessSquare(this, state, x, y));
				
				state = !state;
			}
			
			state = !state;
		}
	}
	
	public void select(ChessSquare square)
	{
		ChessSquare prev = selected;
		
		selected = square;
		
		if(prev != null)
		{
			prev.repaint();
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{

	}
}
