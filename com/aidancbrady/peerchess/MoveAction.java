package com.aidancbrady.peerchess;

import java.awt.Graphics;

import com.aidancbrady.peerchess.ChessPiece.Side;
import com.aidancbrady.peerchess.sound.Sound;

public class MoveAction 
{
	public Sound moveSound = new Sound("resources/sound/move.wav");
	
	public static final int FRAME_LIMIT = 600;
	
	public int frames;
	
	public ChessComponent component;
	
	public ChessPiece piece;
	
	public ChessPiece newPiece;
	
	public ChessMove move;
	
	public MoveAction(ChessComponent c, ChessMove m, ChessPiece p, ChessPiece np)
	{
		component = c;
		move = m;
		piece = p;
		newPiece = np;
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
		if(frames == 0 && PeerChess.instance().enableSoundEffects)
		{
			moveSound.play();
		}
		
		frames++;
		
		if(frames == FRAME_LIMIT)
		{
			move();
		}
		
		component.overlay.repaint();
	}
	
	public void move()
	{
		if(move.toPos.getSquare(component.grid).housedPiece != null)
		{
			if(move.toPos.getSquare(component.grid).housedPiece.side == Side.WHITE)
			{
				component.blackTaken.add(move.toPos.getSquare(component.grid).housedPiece);
			}
			else {
				component.whiteTaken.add(move.toPos.getSquare(component.grid).housedPiece);
			}
		}
		
		move.toPos.getSquare(component.grid).setPiece(newPiece);
		move.toPos.getSquare(component.grid).repaint();
		
		moveSound.stop();
		
		component.turn = component.turn == Side.WHITE ? Side.BLACK : Side.WHITE;
		component.panel.updateText();
		
		component.currentAnimation = null;
	}
	
	public float getPercentage()
	{
		return (float)frames/FRAME_LIMIT;
	}
	
	public void render(Graphics g)
	{
		piece.texture.draw(g, getPosX(), getPosY(), 96, 96);
	}
	
	public String write()
	{
		StringBuilder str = new StringBuilder("MOVE:");
		
		str.append(piece.type.ordinal() + "," + piece.side.ordinal());
		str.append(":");
		str.append(newPiece.type.ordinal() + "," + newPiece.side.ordinal());
		str.append(":");
		str.append(move.fromPos.xPos + "," + move.fromPos.yPos);
		str.append(":");
		str.append(move.toPos.xPos + "," + move.toPos.yPos);
		
		return str.toString();
	}
}
