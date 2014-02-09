package com.aidancbrady.peerchess;

import javax.swing.JFrame;

public class ChessFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public ChessFrame()
	{
		setTitle("PeerChess");
		setSize(768, 790);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		add(new ChessComponent());
	}
}
