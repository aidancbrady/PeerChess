package com.aidancbrady.peerchess.gui.action;

import java.awt.Graphics;

import com.aidancbrady.peerchess.PeerChess;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.client.Assets;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.Endgame;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.gui.ChessComponent;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.DrawTracker;

public class MoveAction 
{	
	private static final int FRAME_LIMIT = 600;
	
	private int frames;
	
	private ChessComponent component;
	
	private ChessPiece piece;
	
	private ChessPiece newPiece;
	private ChessPiece newCastle;
	
	private ChessMove move;
	
	public MoveAction(ChessComponent c, ChessMove m, ChessPiece p, ChessPiece np)
	{
		component = c;
		move = m;
		piece = p;
		newPiece = np;
		
		start();
	}
	
	public void setNoAnimation()
	{
	    frames = FRAME_LIMIT;
	}
	
	public void start()
	{
	    move.boardPreMove = PeerUtils.deepCopyBoard(component.getGame().getGrid());
	       
	    if(move.fromPosCastle != null)
        {
            newCastle = move.fromPosCastle.getSquare(component.getGame().getGrid()).getPiece();
            move.fromPosCastle.getSquare(component.getGame().getGrid()).setPiece(null);
        }
	    
	    move.fromPos.getSquare(component.getGame().getGrid()).setPiece(null);
	}
	
	public int getScale()
	{
	    return (int)(96*(double)component.getWidth()/768D);
	}
	
	public int getPosX(boolean castle)
	{
	    ChessPos fromPos = castle ? move.fromPosCastle : move.fromPos;
	    ChessPos toPos = castle ? move.toPosCastle : move.toPos;
	    
	    if(component.multiplayer && !component.host)
	    {
	        fromPos = PeerUtils.invert(fromPos);
	        toPos = PeerUtils.invert(toPos);
	    }
	    
		int xDist = (toPos.getX()-fromPos.getX())*getScale();
		
		return (fromPos.getX()*getScale()) + (int)(xDist*getPercentage());
	}
	
	public int getPosY(boolean castle)
	{
	    ChessPos fromPos = castle ? move.fromPosCastle : move.fromPos;
        ChessPos toPos = castle ? move.toPosCastle : move.toPos;
        
        if(component.multiplayer && !component.host)
        {
            fromPos = PeerUtils.invert(fromPos);
            toPos = PeerUtils.invert(toPos);
        }
        
		int yDist = (toPos.getY()-fromPos.getY())*getScale();
		
		return (fromPos.getY()*getScale()) + (int)(yDist*getPercentage());
	}
	
	public void update()
	{
		if(frames == 0 && PeerChess.instance().enableSoundEffects && PeerChess.instance().enableAnimations)
		{
			Assets.moveSound.play();
		}
		
		frames++;
		
		if(!PeerChess.instance().enableAnimations || frames >= FRAME_LIMIT)
		{
			move();
		}
		
		component.repaint();
	}
	
	public void move()
	{
	    newPiece.move();
	    
		move.toPos.getSquare(component.getGame().getGrid()).setPiece(newPiece);
		
		if(move.toPosCastle != null)
		{
		    move.toPosCastle.getSquare(component.getGame().getGrid()).setPiece(newCastle);
		}
		
		if(move.enPassantTakePos != null)
		{
		    move.enPassantTakePos.getSquare(component.getGame().getGrid()).setPiece(null);
		}
		
		component.repaint();
		
		Assets.moveSound.stop();
		
		component.getGame().setTurn(component.getGame().getTurn().getOpposite());
		component.getGame().getMoves().add(move);
		
		ChessPos kingPos = component.getGame().findKing(newPiece.getSide().getOpposite());
        
        if(component.getGame().isInCheck(newPiece.getSide().getOpposite(), kingPos))
        {
            component.getGame().setSideInCheck(newPiece.getSide().getOpposite());
        }
        else {
            component.getGame().setSideInCheck(null);
        }
		
		if(component.getGame().isCheckMate(getSide().getOpposite(), false))
		{
		    if(component.getGame().getSideInCheck() == getSide().getOpposite())
		    {
		        component.getGame().setEndgame(Endgame.get(getSide()));
		    }
		    else {
		        component.getGame().setEndgame(Endgame.STALEMATE);
		    }
		}
		
		if(DrawTracker.isDraw(component.getGame().getMoves()))
		{
		    component.getGame().setEndgame(Endgame.DRAW);
		}
		
		component.currentMove = null;
		component.panel.updateText();
		
		if(component.getGame().getEndgame() == null && !component.multiplayer && component.getGame().getTurn() != component.getGame().getSide())
		{
		    component.getGame().getAI().triggerMove();
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
		return piece.getSide();
	}
	
	public void broadcast()
	{
	    component.panel.getConnection().write(write());
	}
	
	public String write()
	{
		StringBuilder str = new StringBuilder("MOVE:");
		
		str.append(piece.getType().ordinal() + "," + piece.getSide().ordinal());
		str.append(":");
		str.append(newPiece.getType().ordinal() + "," + newPiece.getSide().ordinal());
		str.append(":");
		str.append(move.fromPos.getX() + "," + move.fromPos.getY());
		str.append(":");
		str.append(move.toPos.getX() + "," + move.toPos.getY());
		
		return str.toString();
	}
}
