package com.aidancbrady.openchess;

import javax.swing.JFrame;

public class ChessFrame extends JFrame
{
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
