package com.aidancbrady.peerchess;

import java.awt.Graphics;

public class MoveAnimation 
{
	public static int FRAME_LIMIT = 2000;
	
	public int frames;
	
	public ChessComponent component;
	
	public ChessPiece piece;
	
	public ChessMove move;
	
	public MoveAnimation(ChessComponent c, ChessMove m, ChessPiece p)
	{
		component = c;
		move = m;
		piece = p;
	}
	
	public int getPosX()
	{
		int xDist = (move.fromPos.xPos-move.fromPos.xPos)*96;
		
		return (int)(xDist*getPercentage());
	}
	
	public int getPosY()
	{
		int yDist = (move.fromPos.yPos-move.fromPos.yPos)*96;
		
		return (int)(yDist*getPercentage());
	}
	
	public void update()
	{
		frames++;
		
		component.repaint();
	}
	
	public float getPercentage()
	{
		return (float)frames/FRAME_LIMIT;
	}
	
	public void render(Graphics g)
	{
		piece.texture.draw(g, getPosX(), getPosY(), 96, 96);
	}
}
