package com.aidancbrady.peerchess;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.aidancbrady.peerchess.ChessPiece.PieceType;
import com.aidancbrady.peerchess.ChessPiece.Side;
import com.aidancbrady.peerchess.tex.Texture;

public class ChessComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	public ChessSquare[][] grid = new ChessSquare[8][8];
	
	public ChessSquare selected;
	
	public List<ChessMove> moves = new ArrayList<ChessMove>();
	
	public List<ChessPiece> whiteTaken = new ArrayList<ChessPiece>();
	public List<ChessPiece> blackTaken = new ArrayList<ChessPiece>();
	
	public static Texture white = Texture.load("src/resources/icon/white.png");
	public static Texture black = Texture.load("src/resources/icon/black.png");
	
	public static Texture select = Texture.load("src/resources/icon/select.png");
	
	public ChessComponent()
	{
		setLayout(null);
		
		boolean state = false;
		
		for(int y = 0; y < 8; y++)
		{			
			for(int x = 0; x < 8; x++)
			{
				grid[x][y] = (ChessSquare)add(new ChessSquare(this, state, new ChessPos(x, y)));
				
				state = !state;
			}
			
			state = !state;
		}
		
		resetBoard();
	}
	
	public void select(ChessSquare square)
	{
		ChessSquare prev = selected;
		
		selected = square;
		
		if(prev != null)
		{
			prev.repaint();
		}
	}
	
	public ChessPiece getSelectedPiece()
	{
		if(selected == null)
		{
			return null;
		}
		
		return selected.housedPiece;
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
	
	public void resetBoard()
	{
		resetMain(Side.BLACK, 0);
		resetPawns(Side.BLACK, 1);
		
		resetCenter();
		
		resetPawns(Side.WHITE, 6);
		resetMain(Side.WHITE, 7);
	}
	
	@Override
	public void paintComponent(Graphics g) {}
}
