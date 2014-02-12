package com.aidancbrady.peerchess;

import javax.swing.JFrame;

public class ChessFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public ChessToolbar toolbar = new ChessToolbar(this);
	
	public ChessPanel chess;
	public MenuPanel menu;
	public JoinPanel join;
	
	public OptionsFrame options;
	
	public ChessFrame()
	{
		setTitle("PeerChess");
		setSize(400, 600);
		
		add(menu = new MenuPanel(this));
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		add(join = new JoinPanel(this));
		join.setVisible(false);
		
		add(chess = new ChessPanel(this));
		chess.setVisible(false);
		
		setJMenuBar(toolbar.menuBar);
	}
	
	public void openChess()
	{
		menu.setVisible(false);
		join.setVisible(false);
		
		setSize(1024, 790);
		chess.setVisible(true);
	}
	
	public void openJoin()
	{
		menu.setVisible(false);
		chess.setVisible(false);
		
		setSize(400, 600);
		join.setVisible(true);
	}
	
	public void openMenu()
	{
		if(chess.isVisible())
		{
			if(!chess.exit())
			{
				return;
			}
		}
		
		chess.setVisible(false);
		join.setVisible(false);
		
		setSize(400, 600);
		menu.setVisible(true);
	}
	
	public void openOptions()
	{
		setSize(400, 600);
		
		if(options == null)
		{
			options = new OptionsFrame(this);
		}
		else {
			options.setVisible(true);
			options.toFront();
		}
	}
}
