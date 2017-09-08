package com.aidancbrady.peerchess;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.aidancbrady.peerchess.ChessPiece.Endgame;
import com.aidancbrady.peerchess.ChessPiece.PieceType;
import com.aidancbrady.peerchess.ChessPiece.Side;
import com.aidancbrady.peerchess.tex.Texture;

public class ChessComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	public ChessAI chessAI = new ChessAI(this);
	
	public ChessPanel panel;
	
	public ChessSquare[][] grid = new ChessSquare[8][8];
	
	public ChessSquare selected;
	
	public MoveAction currentAnimation;
	
	public OverlayComponent overlay;
	
	public JPanel chessboard;
	
	public Side side = Side.WHITE;
	public Side turn = Side.WHITE;
	public Endgame endgame = null;
	
	public Set<ChessPos> possibleMoves = new HashSet<>();
	public Side sideInCheck = null;
	
	public boolean multiplayer;
	
	public List<ChessMove> moves = new ArrayList<ChessMove>();
	
	public static Texture white = Texture.load("resources/icon/white.png");
	public static Texture black = Texture.load("resources/icon/black.png");
	
	public static Texture select = Texture.load("resources/icon/select.png");
	public static Texture possible = Texture.load("resources/icon/possible.png");
	public static Texture check = Texture.load("resources/icon/check.png");
	
	public ChessComponent(ChessPanel p)
	{
		panel = p;
		
		setLayout(new BorderLayout());
		setSize(768, 768);
		
		boolean color = false;
		
		chessboard = new JPanel(new GridLayout(8, 8));
		
		add(overlay = new OverlayComponent(this), BorderLayout.CENTER);
		
		for(int y = 0; y < 8; y++)
		{			
			for(int x = 0; x < 8; x++)
			{
			    grid[x][y] = new ChessSquare(color, new ChessPos(x, y));
				chessboard.add(new ChessSquarePanel(this, grid[x][y]));
				color = !color;
			}
			
			color = !color;
		}
		
		add(chessboard, BorderLayout.CENTER);

		resetGame();
	}
	
	public void select(ChessSquare square)
	{
		selected = square;
		
		possibleMoves.clear();
		
		if(selected != null)
		{
		    possibleMoves.addAll(PeerUtils.getValidatedMoves(this));
		}
		
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
		
		currentAnimation = null;
		side = Side.WHITE;
		turn = Side.WHITE;
		selected = null;
		moves.clear();
		panel.pawnReplace = 0;
		panel.chatBox.setText("");
		panel.updateText();
		endgame = null;
		chessAI.reset();
		
		possibleMoves.clear();
		sideInCheck = null;
		
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
	
	public boolean isMoving()
	{
		return currentAnimation != null;
	}
	
	public void setSide(Side s)
	{
		side = s;
	}
	
	@Override
	public void paintComponent(Graphics g) {}
}
