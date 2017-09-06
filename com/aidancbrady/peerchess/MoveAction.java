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
	
	public ChessPiece newCastle;
	
	public ChessMove move;
	
	public MoveAction(ChessComponent c, ChessMove m, ChessPiece p, ChessPiece np)
	{
		component = c;
		move = m;
		piece = p;
		newPiece = np;
		
		start();
	}
	
	public void start()
	{
	    if(move.fromPosCastle != null)
        {
            newCastle = move.fromPosCastle.getSquare(component.grid).housedPiece;
            move.fromPosCastle.getSquare(component.grid).setPiece(null);
        }
	    
	    move.fromPos.getSquare(component.grid).setPiece(null);
	}
	
	public int getScale()
	{
	    return (int)(96*(double)component.getWidth()/768D);
	}
	
	public int getPosX(boolean castle)
	{
	    ChessPos fromPos = castle ? move.fromPosCastle : move.fromPos;
	    ChessPos toPos = castle ? move.toPosCastle : move.toPos;
	    
		int xDist = (toPos.xPos-fromPos.xPos)*getScale();
		
		return (fromPos.xPos*getScale()) + (int)(xDist*getPercentage());
	}
	
	public int getPosY(boolean castle)
	{
	    ChessPos fromPos = castle ? move.fromPosCastle : move.fromPos;
        ChessPos toPos = castle ? move.toPosCastle : move.toPos;
        
		int yDist = (toPos.yPos-fromPos.yPos)*getScale();
		
		return (fromPos.yPos*getScale()) + (int)(yDist*getPercentage());
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
	    newPiece.move();
	    
		move.toPos.getSquare(component.grid).setPiece(newPiece);
		move.toPos.getSquare(component.grid).repaint();
		
		if(move.toPosCastle != null)
		{
		    move.toPosCastle.getSquare(component.grid).setPiece(newCastle);
		    move.toPosCastle.getSquare(component.grid).repaint();
		}
		
		moveSound.stop();
		
		component.turn = component.turn.getOpposite();
		
		if(PeerUtils.isCheckMate(getSide().getOpposite(), component.grid))
		{
			component.winner = getSide();
		}
		
		component.panel.updateText();
		component.currentAnimation = null;
		
		if(component.winner == null && !component.multiplayer && component.turn != component.side)
		{
		    component.chessAI.triggerMove();
		}
	}
	
	public float getPercentage()
	{
		return (float)frames/FRAME_LIMIT;
	}
	
	public void render(Graphics g)
	{
		piece.getTexture().draw(g, getPosX(false), getPosY(false), getScale(), getScale());
		
		if(newCastle != null)
		{
		    newCastle.getTexture().draw(g, getPosX(true), getPosY(true), getScale(), getScale());
		}
	}
	
	public Side getSide()
	{
		return piece.side;
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
