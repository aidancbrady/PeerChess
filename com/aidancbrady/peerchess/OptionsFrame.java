package com.aidancbrady.peerchess;

import javax.swing.JFrame;

public class OptionsFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	public OptionsFrame()
	{
		setTitle("Options");
		setSize(800, 600);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}
}
