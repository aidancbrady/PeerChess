package com.aidancbrady.peerchess;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.aidancbrady.peerchess.tex.Texture;

public class ChessMenu extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static Texture logo = Texture.load("logo.png");
	
	public ChessFrame frame;
	
	public JButton newButton;
	public JButton joinButton;
	
	public ChessMenu(ChessFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setVisible(true);
		
		setLayout(null);
		
		newButton = new JButton("New Game");
		newButton.setSize(300, 60);
		newButton.setLocation(50, 200);
		newButton.addActionListener(new NewButtonListener());
		add(newButton);
		
		joinButton = new JButton("Join Game");
		joinButton.setSize(300, 60);
		joinButton.setLocation(50, 280);
		joinButton.addActionListener(new JoinButtonListener());
		add(joinButton);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		logo.draw(g, 50, 50, 300, 46);
	}
	
	public class NewButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			frame.openChess();
		}
	}
	
	public class JoinButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			
		}
	}
}
