package com.aidancbrady.peerchess;

import java.awt.Graphics;

import com.aidancbrady.peerchess.sound.Sound;

public class MoveAnimation 
{
	public static Sound moveSound = new Sound("resources/sound/move.wav");
	
	public static final int FRAME_LIMIT = 600;
	
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
		int xDist = (move.toPos.xPos-move.fromPos.xPos)*96;
		
		return (move.fromPos.xPos*96) + (int)(xDist*getPercentage());
	}
	
	public int getPosY()
	{
		int yDist = (move.toPos.yPos-move.fromPos.yPos)*96;
		
		return (move.fromPos.yPos*96) + (int)(yDist*getPercentage());
	}
	
	public void update()
	{
		if(frames == 0)
		{
			moveSound.play();
		}
		
		frames++;
		
		if(frames == FRAME_LIMIT)
		{
			move();
			component.currentAnimation = null;
		}
		
		component.overlay.repaint();
	}
	
	public void move()
	{
		move.toPos.getSquare(component.grid).setPiece(piece);
		move.toPos.getSquare(component.grid).repaint();
		
		moveSound.stop();
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
