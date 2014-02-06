package com.aidancbrady.openchess;

import javax.swing.JComponent;

import com.aidancbrady.openchess.tex.Texture;

public class ChessComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
	
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
}
