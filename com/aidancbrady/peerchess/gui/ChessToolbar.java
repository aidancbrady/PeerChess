package com.aidancbrady.peerchess.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ChessToolbar 
{
	private ChessFrame frame;
	
	private JMenuBar menuBar = new JMenuBar();
	
	private JMenu windowMenu = new JMenu("Window");
	private JMenuItem menuItem = new JMenuItem("Menu");
	
	public ChessToolbar(ChessFrame f)
	{
		frame = f;
		
		menuItem.addActionListener(e -> frame.openMenu());
		windowMenu.add(menuItem);
		
		menuBar.add(windowMenu);
	}
	
	public JMenuBar getMenuBar()
	{
	    return menuBar;
	}
}
