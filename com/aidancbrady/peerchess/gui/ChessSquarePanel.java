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
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;

public class ChessSquarePanel extends JComponent implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	public ChessComponent game;
	
	public ChessSquare square;
	
	public ChessSquarePanel(ChessComponent com, ChessSquare s)
	{
		game = com;
		square = s;
		
		setSize(96, 96);
		setLocation(square.getPos().xPos*96, square.getPos().yPos*96);
		setFocusable(false);
		addMouseListener(this);
		
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if(game.currentDrag == null && game.turn == game.side) 
                {
                    List<ChessPos> possibleMoves = PeerUtils.getValidatedMoves(game, square);
                    
                    if(square.getPiece() != null && square.getPiece().side == game.side && !possibleMoves.isEmpty())
                    {
                        game.select(null);
                        game.possibleMoves.addAll(possibleMoves);
                        game.repaint();
                        game.currentDrag = new DragAction(ChessSquarePanel.this);
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
			ChessComponent.black.draw(g, 0, 0, getWidth(), getHeight());
		}
		else {
			ChessComponent.white.draw(g, 0, 0, getWidth(), getHeight());	
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
    	    if(square.getPiece() != null && square.getPiece().type == PieceType.KING && game.sideInCheck == square.getPiece().side)
    	    {
    	        if(game.currentMove == null)
    	        {
    	            ChessComponent.check.draw(g, 0, 0, getWidth(), getHeight());
    	        }
    	    }
    	    
    	    if(game.possibleMoves.contains(square.getPos()))
    	    {
    	        ChessComponent.possible.draw(g, 0, 0, getWidth(), getHeight());
    	    }
		}
		
		if(game.selected == square)
		{
			ChessComponent.select.draw(g, 0, 0, getWidth(), getHeight());
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
		if(game.isMoving() || game.turn != game.side || game.endgame != null)
		{
			return;
		}
		
		if(game.multiplayer && game.panel.connection == null)
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
				if(game.side != square.getPiece().side)
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
					
					if(square.getPiece() != null && square.getPiece().side == piece.side)
					{
						game.select(square);
						repaint();
						game.panel.repaint();
						
						return;
					}
					else {
						if(piece.type.getPiece().validateMove(game, move))
						{
							ChessPiece newPiece = piece;
							
							if(piece.type == PieceType.PAWN)
							{
								if((piece.side == Side.WHITE && move.toPos.yPos == 0) || (piece.side == Side.BLACK && move.toPos.yPos == 7))
								{
									game.panel.pawnReplace %= PieceType.values().length-1;
									newPiece = PeerUtils.getPawnReplace(piece.side, game.panel.pawnReplace);
									newPiece.moves = piece.moves;
								}
							}
							
							game.currentMove = new MoveAction(game, move, piece, newPiece);
							
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
}
