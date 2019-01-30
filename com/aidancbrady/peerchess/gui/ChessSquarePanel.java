package com.aidancbrady.peerchess.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JComponent;

import com.aidancbrady.peerchess.ChessComponent;
import com.aidancbrady.peerchess.DragAction;
import com.aidancbrady.peerchess.MoveAction;
import com.aidancbrady.peerchess.PeerChess;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.client.Assets;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;

public class ChessSquarePanel extends JComponent implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	private ChessComponent game;
	
	private ChessSquare square;
	
	public ChessSquarePanel(ChessComponent com, ChessSquare s)
	{
		game = com;
		square = s;
		
		setSize(96, 96);
		setLocation(square.getPos().getX()*96, square.getPos().getY()*96);
		setFocusable(false);
		addMouseListener(this);
		
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if(game.currentDrag == null && game.getGame().getTurn() == game.getGame().getSide()) 
                {
                    List<ChessPos> possibleMoves = PeerUtils.getValidatedMoves(game, square);
                    
                    if(square.getPiece() != null && square.getPiece().getSide() == game.getGame().getSide() && !possibleMoves.isEmpty())
                    {
                        game.select(null);
                        game.possibleMoves.addAll(possibleMoves);
                        game.repaint();
                        game.currentDrag = new DragAction(ChessSquarePanel.this);
                        game.panel.updateText();
                        game.currentHint = null;
                    }
                }
                
                if(game.currentDrag != null)
                {
                    game.currentDrag.updateMouse(e.getLocationOnScreen());
                }
            }
        
            @Override
            public void mouseMoved(MouseEvent e) {}
        });
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		if(square.isBlack())
		{
			Assets.black.draw(g, 0, 0, getWidth(), getHeight());
		}
		else {
			Assets.white.draw(g, 0, 0, getWidth(), getHeight());	
		}
		
		if(square.getPiece() != null && square.getPiece().getTexture() != null)
		{
		    if(game.currentDrag == null || game.currentDrag.getSquarePanel() != this)
	        {
		        square.getPiece().getTexture().draw(g, 0, 0, getWidth(), getHeight());
	        }
		}
		
		if(PeerChess.instance().enableVisualGuides)
		{
    	    if(square.getPiece() != null && square.getPiece().getType() == PieceType.KING && game.getGame().getSideInCheck() == square.getPiece().getSide())
    	    {
    	        if(game.currentMove == null)
    	        {
    	            Assets.check.draw(g, 0, 0, getWidth(), getHeight());
    	        }
    	    }
    	    
    	    if(game.possibleMoves.contains(square.getPos()))
    	    {
    	        Assets.possible.draw(g, 0, 0, getWidth(), getHeight());
    	    }
		}
		
		if(game.currentHint != null && (game.currentHint.toPos.equals(square.getPos()) || game.currentHint.fromPos.equals(square.getPos())))
		{
		    Assets.hint.draw(g, 0, 0, getWidth(), getHeight());
		}
		
		if(game.selected == square)
		{
			Assets.select.draw(g, 0, 0, getWidth(), getHeight());
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
	    if(game.currentDrag != null)
	    {
	        game.currentDrag.enter(this);
	    }
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
	    if(game.currentDrag != null)
        {
            game.currentDrag.exit(this);
        }
	}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		if(game.isMoving() || game.getGame().getTurn() != game.getGame().getSide() || game.getGame().getEndgame() != null)
		{
			return;
		}
		
		if(game.multiplayer && game.panel.getConnection() == null)
		{
		    return;
		}
		
		if(game.currentDrag != null)
		{
		    game.currentDrag.release();
		    return;
		}
		
		if(game.selected != null && game.selected.getPiece() == null)
		{
		    game.select(null);
		}
		
		if(arg0.getX() >= 0 && arg0.getX() <= getWidth() && arg0.getY() >= 0 && arg0.getY() <= getHeight())
		{
			if(game.selected == null && square.getPiece() != null)
			{
				if(game.getGame().getSide() != square.getPiece().getSide())
				{
					return;
				}
				
				game.select(square);
			}
			else {
				if(game.selected != null)
				{
					ChessPiece piece = game.selected.getPiece();
					ChessMove move = new ChessMove(game.selected.getPos(), square.getPos());
					
					if(square.getPiece() != null && square.getPiece().getSide() == piece.getSide())
					{
						game.select(square);
						repaint();
						game.panel.repaint();
						
						return;
					}
					else {
						if(piece.getType().getPiece().validateMove(game, move))
						{
							ChessPiece newPiece = piece;
							
							if(piece.getType() == PieceType.PAWN)
							{
								if((piece.getSide() == Side.WHITE && move.toPos.getY() == 0) || (piece.getSide() == Side.BLACK && move.toPos.getY() == 7))
								{
									newPiece = PeerUtils.getPawnReplace(piece.getSide(), game.panel.getPawnReplaceIndex());
									newPiece.setMoves(piece.getMoves());
								}
							}
							
							game.currentMove = new MoveAction(game, move, piece, newPiece);
							game.panel.updateText();
							
							if(game.multiplayer)
							{
							    game.currentMove.broadcast();
							}
						}
					}
				}
				
				game.select(null);
			}
			
			repaint();
			game.panel.repaint();
		}
	}
	
	public ChessComponent getGame()
	{
	    return game;
	}
	
	public ChessSquare getSquare()
	{
	    return square;
	}
}
