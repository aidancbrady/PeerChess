package com.aidancbrady.peerchess.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ChessToolbar 
{
	public ChessFrame frame;
	
	public JMenuBar menuBar = new JMenuBar();
	
	public JMenu windowMenu = new JMenu("Window");
	
	public JMenuItem menuItem = new JMenuItem("Menu");
	
	public ChessToolbar(ChessFrame f)
	{
		frame = f;
		
		menuItem.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.openMenu();
			}
		});
		windowMenu.add(menuItem);
		
		menuBar.add(windowMenu);
	}
}
