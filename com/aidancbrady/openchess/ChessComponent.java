package com.aidancbrady.openchess;

import java.awt.Graphics;

import javax.swing.JComponent;

import com.aidancbrady.openchess.tex.Texture;

public class ChessComponent extends JComponent
{
	public static Texture white = new Texture("src/resources/icon/white.png");
	public static Texture black = new Texture("src/resources/icon/black.png");
	
	public ChessComponent()
	{
		setLayout(null);
		
		boolean state = false;
		
		for(int y = 0; y < 8; y++)
		{			
			for(int x = 0; x < 8; x++)
			{
				add(new ChessSquare(state, x, y));
				
				state = !state;
			}
			
			state = !state;
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		boolean state = false;
		
		for(int y = 0; y < 8; y++)
		{			
			for(int x = 0; x < 8; x++)
			{
				if(state)
				{
					black.draw(g, x*96, y*96, 96, 96);
				}
				else if(!state)
				{
					white.draw(g, x*96, y*96, 96, 96);	
				}
				
				state = !state;
			}
			
			state = !state;
		}
	}
}
