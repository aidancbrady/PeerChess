package com.aidancbrady.peerchess;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ChessPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public ChessFrame frame;

	public ChessComponent chess;
	
	public ChessPanel(ChessFrame f)
	{
		frame = f;
		
		setSize(1024, 790);
		setVisible(true);
		setLayout(null);
		
		setBackground(Color.GRAY);
		
		add(chess = new ChessComponent());
		chess.setVisible(true);
	}
	
	public boolean exit()
	{
		int returned = JOptionPane.showConfirmDialog(this, "Would you like to save your game?");
		
		if(returned == 2)
		{
			return false;
		}
		else if(returned == 0)
		{
			String s = JOptionPane.showInputDialog(this, "Please provide a name for this save.");
			
			if(s == null)
			{
				return false;
			}
		}
		
		return true;
	}
}
