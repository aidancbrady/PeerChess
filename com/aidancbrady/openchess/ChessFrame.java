package com.aidancbrady.openchess;

import javax.swing.JFrame;

public class ChessFrame extends JFrame
{
	public ChessFrame()
	{
		setTitle("PeerChess");
		setSize(768, 768);
		setVisible(true);
		
		add(new ChessComponent());
	}
}
