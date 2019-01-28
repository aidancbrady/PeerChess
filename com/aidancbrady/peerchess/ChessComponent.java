package com.aidancbrady.peerchess;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.aidancbrady.peerchess.ai.ChessAI;
import com.aidancbrady.peerchess.game.ChessGame;
import com.aidancbrady.peerchess.game.ChessMove;
import com.aidancbrady.peerchess.game.ChessPiece;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.game.ChessPos;
import com.aidancbrady.peerchess.game.ChessSquare;
import com.aidancbrady.peerchess.gui.ChessPanel;
import com.aidancbrady.peerchess.gui.ChessSquarePanel;
import com.aidancbrady.peerchess.gui.OverlayComponent;
import com.aidancbrady.peerchess.piece.Piece;

public class ChessComponent extends JComponent implements IChessGame
{
	private static final long serialVersionUID = 1L;
	
	public ChessGame game = new ChessGame(this);
	
	public ChessAI chessAI = new ChessAI(this);
	
	public ChessPanel panel;
	
	public ChessSquare[][] grid = PeerUtils.createEmptyBoard();
	
	public ChessSquare selected;
	
	public MoveAction currentMove;
	public DragAction currentDrag;
	
	public OverlayComponent overlay;
	
	public JPanel chessboard;
	
	public Set<ChessPos> possibleMoves = new HashSet<>();
	
	public boolean multiplayer;
	public boolean host;
	
	public ChessMove currentHint;
	
	public List<ChessMove> moves = new ArrayList<ChessMove>();
	
	public ChessComponent(ChessPanel p)
	{
		panel = p;
		
		setLayout(new BorderLayout());
		setSize(768, 768);
		
		add(overlay = new OverlayComponent(this), BorderLayout.CENTER);
		
	    setupBoard(false);

		resetGame();
	}
	
	public void select(ChessSquare square)
	{
		selected = square;
		
		possibleMoves.clear();
		
		if(selected != null)
		{
		    possibleMoves.addAll(PeerUtils.getValidatedMoves(this, selected));
		}
		
		currentHint = null;
		
		repaint();
	}
	
	public ChessPiece getSelectedPiece()
	{
		if(selected == null)
		{
			return null;
		}
		
		return selected.getPiece();
	}
	
	public void resetMain(Side s, int y)
	{
		grid[0][y].setPiece(new ChessPiece(PieceType.CASTLE, s));
		grid[1][y].setPiece(new ChessPiece(PieceType.KNIGHT, s));
		grid[2][y].setPiece(new ChessPiece(PieceType.BISHOP, s));
		grid[3][y].setPiece(new ChessPiece(PieceType.QUEEN, s));
		grid[4][y].setPiece(new ChessPiece(PieceType.KING, s));
		grid[5][y].setPiece(new ChessPiece(PieceType.BISHOP, s));
		grid[6][y].setPiece(new ChessPiece(PieceType.KNIGHT, s));
		grid[7][y].setPiece(new ChessPiece(PieceType.CASTLE, s));
	}
	
	public void resetPawns(Side s, int y)
	{
		for(int x = 0; x < 8; x++)
		{
			grid[x][y].setPiece(new ChessPiece(PieceType.PAWN, s));
		}
	}
	
	public void resetCenter()
	{
		for(int x = 0; x < 8; x++)
		{
			for(int y = 2; y < 6; y++)
			{
				grid[x][y].setPiece(null);
			}
		}
	}
	
	public void resetGame()
	{
		resetMain(Side.BLACK, 0);
		resetPawns(Side.BLACK, 1);
		
		resetCenter();
		
		resetPawns(Side.WHITE, 6);
		resetMain(Side.WHITE, 7);
		
		currentMove = null;
		currentDrag = null;
		
		game.reset();
	
		selected = null;
		moves.clear();
		panel.pawnReplace = 0;
		panel.chatBox.setText("");
		panel.updateText();

		chessAI.reset();
		host = false;
		multiplayer = false;
		
		possibleMoves.clear();
		
		setupBoard(false);
		
		if(panel.opponentLabel != null)
		{
			panel.opponentLabel.setText("Opponent: waiting");
		}
		
		if(panel.connection != null)
		{
			panel.connection.close();
		}
		
		panel.frame.waiting.close();
	}
	
    /**
     * @return -1 if no, 0 if white, 1 if black
     */
    public byte shouldPawnReplace()
    {
        if(selected != null && selected.getPiece() != null)
        {
            if(selected.getPiece().type == PieceType.PAWN)
            {
                if(selected.getPiece().side == Side.BLACK && selected.getPos().translate(0, 1).yPos == 7)
                {
                    Piece piece = selected.getPiece().type.getPiece();
                    
                    ChessMove leftMove = new ChessMove(selected.getPos(), selected.getPos().translate(-1, 1));
                    ChessMove centerMove = new ChessMove(selected.getPos(), selected.getPos().translate(0, 1));
                    ChessMove rightMove = new ChessMove(selected.getPos(), selected.getPos().translate(1, 1));
                    
                    if((leftMove.toPos.xPos >= 0 && piece.validateMove(this, leftMove)) || piece.validateMove(this, centerMove) || (rightMove.toPos.xPos <= 7 && piece.validateMove(this, rightMove)))
                    {
                        return 1;
                    }
                }
                else if(selected.getPiece().side == Side.WHITE && selected.getPos().translate(0, -1).yPos == 0)
                {
                    Piece piece = selected.getPiece().type.getPiece();
                    
                    ChessMove leftMove = new ChessMove(selected.getPos(), selected.getPos().translate(-1, -1));
                    ChessMove centerMove = new ChessMove(selected.getPos(), selected.getPos().translate(0, -1));
                    ChessMove rightMove = new ChessMove(selected.getPos(), selected.getPos().translate(1, -1));
                    
                    if((leftMove.toPos.xPos >= 0 && piece.validateMove(this, leftMove)) || piece.validateMove(this, centerMove) || (rightMove.toPos.xPos <= 7 && piece.validateMove(this, rightMove)))
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
        
        //take back opponent's last move
        ChessMove opponentLast = moves.get(moves.size()-1);
        opponentLast.doRevertMove(this);
        moves.remove(opponentLast);
        
        //take back player's last move
        ChessMove last = moves.get(moves.size()-1);
        last.doRevertMove(this);
        moves.remove(last);
        
        ChessPos opponentKingPos = PeerUtils.findKing(getGame().side.getOpposite(), grid);
        ChessPos kingPos = PeerUtils.findKing(getGame().side, grid);
        
        if(PeerUtils.isInCheck(getGame().side.getOpposite(), opponentKingPos, grid))
        {
            getGame().sideInCheck = getGame().side.getOpposite();
        }
        else if(PeerUtils.isInCheck(getGame().side, kingPos, grid))
        {
            getGame().sideInCheck = getGame().side;
        }
        else {
            getGame().sideInCheck = null;
        }
        
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
                chessboard.add(new ChessSquarePanel(this, grid[flip ? 7-x : x][flip ? 7-y : y]));
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
	    currentHint = chessAI.minimax(true);
	    repaint();
	}
	
	@Override
	public ChessSquare[][] getGrid()
	{
	    return grid;
	}
	
	@Override
	public List<ChessMove> getPastMoves()
	{
	    return moves;
	}
}
