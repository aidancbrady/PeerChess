package com.aidancbrady.peerchess.gui;

import java.awt.Font;
import java.awt.Graphics;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.aidancbrady.peerchess.PeerChess;
import com.aidancbrady.peerchess.tex.Texture;

public class MenuPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static Texture logo = Texture.load("logo.png");
	
	public ChessFrame frame;
	
	public JButton newButton;
	public JButton loadButton;
	public JButton hostButton;
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
        newButton.setLocation(50, 140);
        newButton.addActionListener(e -> frame.openChess(false));
        add(newButton);
		
		hostButton = new JButton("Host Game");
		hostButton.setSize(300, 60);
		hostButton.setLocation(50, 210);
		hostButton.addActionListener(e -> frame.openChess(true));
		add(hostButton);
		
		joinButton = new JButton("Join Game");
		joinButton.setSize(300, 60);
		joinButton.setLocation(50, 280);
		joinButton.addActionListener(e -> frame.openJoin());
		add(joinButton);
		
        loadButton = new JButton("Load Game");
        loadButton.setSize(300, 60);
        loadButton.setLocation(50, 350);
        loadButton.addActionListener(e -> frame.openSavedChess());
        add(loadButton);
		
		optionsButton = new JButton("Options");
		optionsButton.setSize(300, 60);
		optionsButton.setLocation(50, 420);
		optionsButton.addActionListener(e -> frame.openOptions());
		add(optionsButton);
		
		JLabel version = new JLabel("v1.0");
		version.setFont(new Font("Helvetica", Font.BOLD, 14));
		version.setVisible(true);
		version.setSize(200, 40);
		version.setLocation(340, 520);
		add(version);
		
		JLabel copyright = new JLabel("© aidancbrady, " + PeerChess.instance().calendar.get(Calendar.YEAR));
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
}
