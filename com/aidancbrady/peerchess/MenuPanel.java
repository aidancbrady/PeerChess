package com.aidancbrady.peerchess;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.aidancbrady.peerchess.tex.Texture;

public class MenuPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static Texture logo = Texture.load("logo.png");
	
	public ChessFrame frame;
	
	public JButton newButton;
	public JButton loadButton;
	public JButton joinButton;
	public JButton optionsButton;
	
	public MenuPanel(ChessFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setVisible(true);
		setLayout(null);
		
		newButton = new JButton("New Game");
		newButton.setSize(300, 60);
		newButton.setLocation(50, 160);
		newButton.addActionListener(new NewButtonListener());
		add(newButton);
		
		loadButton = new JButton("Load Game");
		loadButton.setSize(300, 60);
		loadButton.setLocation(50, 240);
		loadButton.addActionListener(new LoadButtonListener());
		add(loadButton);
		
		joinButton = new JButton("Join Game");
		joinButton.setSize(300, 60);
		joinButton.setLocation(50, 320);
		joinButton.addActionListener(new JoinButtonListener());
		add(joinButton);
		
		optionsButton = new JButton("Options");
		optionsButton.setSize(300, 60);
		optionsButton.setLocation(50, 400);
		optionsButton.addActionListener(new OptionsButtonListener());
		add(optionsButton);
		
		JLabel version = new JLabel("v1.0");
		version.setFont(new Font("Helvetica", Font.BOLD, 14));
		version.setVisible(true);
		version.setSize(200, 40);
		version.setLocation(340, 520);
		add(version);
		
		JLabel copyright = new JLabel("© aidancbrady, 2014");
		copyright.setVisible(true);
		copyright.setSize(200, 40);
		copyright.setLocation(30, 520);
		add(copyright);
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
	
	public class LoadButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			frame.openSavedChess();
		}
	}
	
	public class JoinButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			frame.openJoin();
		}
	}
	
	public class OptionsButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			frame.openOptions();
		}
	}
}
