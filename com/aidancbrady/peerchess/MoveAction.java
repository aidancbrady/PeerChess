package com.aidancbrady.peerchess;

import java.awt.Graphics;

import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessPiece.Endgame;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
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
            newCastle = move.fromPosCastle.getSquare(component.grid).getPiece();
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
		
		if(move.toPosCastle != null)
		{
		    move.toPosCastle.getSquare(component.grid).setPiece(newCastle);
		}
		
		component.repaint();
		
		moveSound.stop();
		
		component.turn = component.turn.getOpposite();
		
		ChessPos kingPos = PeerUtils.findKing(newPiece.side.getOpposite(), component.grid);
        
        if(PeerUtils.isInCheck(newPiece.side.getOpposite(), kingPos, component.grid))
        {
            component.sideInCheck = newPiece.side.getOpposite();
        }
        else {
            component.sideInCheck = null;
        }
		
		if(PeerUtils.isCheckMate(getSide().getOpposite(), component))
		{
		    if(component.sideInCheck == getSide().getOpposite())
		    {
		        component.endgame = Endgame.get(getSide());
		    }
		    else {
		        component.endgame = Endgame.STALEMATE;
		    }
		}
		
		component.panel.updateText();
		component.currentMove = null;
		
		if(component.endgame == null && !component.multiplayer && component.turn != component.side)
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
	
	public void broadcast()
	{
	    component.panel.connection.write(write());
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
