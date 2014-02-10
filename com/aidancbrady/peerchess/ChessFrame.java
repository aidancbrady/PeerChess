package com.aidancbrady.peerchess;

import javax.swing.JFrame;

public class ChessFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public ChessComponent chess;
	public ChessMenu menu;
	
	public OptionsFrame options;
	
	public ChessFrame()
	{
		setTitle("PeerChess");
		setSize(400, 600);
		
		add(menu = new ChessMenu(this));
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		add(chess = new ChessComponent());
		chess.setVisible(false);
	}
	
	public void openChess()
	{
		menu.setVisible(false);
		
		setSize(768, 790);
		chess.setVisible(true);
	}
	
	public void openOptions()
	{
		if(options == null)
		{
			options = new OptionsFrame();
		}
		else {
			options.toFront();
		}
	}
}
