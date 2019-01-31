package com.aidancbrady.peerchess.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.game.ChessGame;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.gui.action.DragAction;
import com.aidancbrady.peerchess.gui.action.MoveAction;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;
import com.aidancbrady.peerchess.piece.Piece;

public class ChessComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
	
    private ChessGame game = new ChessGame(this);
    
    private JPanel chessboard;
    private ChessSquare selected;
    private OverlayComponent overlay;

    public ChessPanel panel;

    public MoveAction currentMove;
    public DragAction currentDrag;

    public ChessMove currentHint;

    public Set<ChessPos> possibleMoves = new HashSet<>();

    public boolean multiplayer;
    public boolean host;

	public ChessComponent(ChessPanel p)
	{
		panel = p;
		
		setLayout(new BorderLayout());
		setSize(768, 768);
		
		add(overlay = new OverlayComponent(this), BorderLayout.CENTER);

		resetGame();
	}
	
	public void select(ChessSquare square)
	{
		selected = square;
		
		possibleMoves.clear();
		
		if(selected != null)
		{
		    possibleMoves.addAll(PeerUtils.getValidatedMoves(getGame(), selected));
		}
		
		currentHint = null;
		
		repaint();
	}
	
    public ChessSquare getSelected()
    {
        return selected;
    }
	
	public void resetGame()
	{
	    setupBoard(false);
		
		currentMove = null;
		currentDrag = null;
		currentHint = null;
		
		game.reset();
	
		selected = null;

		panel.updateText();
		
		host = false;
		multiplayer = false;
		
		possibleMoves.clear();
		
		panel.reset();
	}
	
    /**
     * @return -1 if no, 0 if white, 1 if black
     */
    public byte shouldPawnReplace()
    {
        if(selected != null && selected.getPiece() != null)
        {
            if(selected.getPiece().getType() == PieceType.PAWN)
            {
                if(selected.getPiece().getSide() == Side.BLACK && selected.getPos().translate(0, 1).getY() == 7)
                {
                    Piece piece = selected.getPiece().getType().getPiece();
                    
                    ChessMove leftMove = new ChessMove(selected.getPos(), selected.getPos().translate(-1, 1));
                    ChessMove centerMove = new ChessMove(selected.getPos(), selected.getPos().translate(0, 1));
                    ChessMove rightMove = new ChessMove(selected.getPos(), selected.getPos().translate(1, 1));
                    
                    if((leftMove.toPos.getX() >= 0 && piece.validateMove(getGame(), leftMove)) || piece.validateMove(getGame(), centerMove) || (rightMove.toPos.getX() <= 7 && piece.validateMove(getGame(), rightMove)))
                    {
                        return 1;
                    }
                }
                else if(selected.getPiece().getSide() == Side.WHITE && selected.getPos().translate(0, -1).getY() == 0)
                {
                    Piece piece = selected.getPiece().getType().getPiece();
                    
                    ChessMove leftMove = new ChessMove(selected.getPos(), selected.getPos().translate(-1, -1));
                    ChessMove centerMove = new ChessMove(selected.getPos(), selected.getPos().translate(0, -1));
                    ChessMove rightMove = new ChessMove(selected.getPos(), selected.getPos().translate(1, -1));
                    
                    if((leftMove.toPos.getX() >= 0 && piece.validateMove(getGame(), leftMove)) || piece.validateMove(getGame(), centerMove) || (rightMove.toPos.getX() <= 7 && piece.validateMove(getGame(), rightMove)))
                    {
                        return 0;
                    }
                }
            }
        }
        
        return -1;
    }
    
    public void takeBackLastMove()
    {
        currentHint = null;
        possibleMoves.clear();
        
        game.doRevertMove();
        
        panel.updateText();
        repaint();
    }
    
    public void setupBoard(boolean flip)
    {
        if(chessboard != null) remove(chessboard);
        
        chessboard = new JPanel(new GridLayout(8, 8));
        
        for(int y = 0; y < 8; y++)
        {           
            for(int x = 0; x < 8; x++)
            {
                chessboard.add(new ChessSquarePanel(this, getGame().getGrid()[flip ? 7-x : x][flip ? 7-y : y]));
            }
        }
        
        add(chessboard, BorderLayout.CENTER);
    }
	
	public boolean isMoving()
	{
		return currentMove != null;
	}
	
	public boolean isDragging()
	{
	    return currentDrag != null;
	}
	
	public ChessGame getGame()
	{
	    return game;
	}
	
	public void hint()
	{
	    currentHint = game.getAI().minimax(true);
	    repaint();
	}
	
	public void resize(int size)
	{
        chessboard.setSize(size, size);
        overlay.setSize(size, size);
	}
}
