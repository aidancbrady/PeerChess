package com.aidancbrady.openchess;

import java.awt.Graphics;

import javax.swing.JComponent;

import com.aidancbrady.openchess.tex.Texture;

public class ChessComponent extends JComponent
{
	public Texture white = new Texture("resources/icon/white.png");
	public Texture black = new Texture("resources/icon/black.png");
	
	public ChessComponent()
	{
		
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		black.draw(g);
	}
}
