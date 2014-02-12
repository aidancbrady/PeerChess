package com.aidancbrady.peerchess;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class OptionsFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public ChessFrame frame;
	
	public JButton backButton;
	
	public JLabel usernameLabel;

	public OptionsFrame(ChessFrame f)
	{
		frame = f;
		
		setTitle("Options");
		setSize(300, 500);
		setLayout(null);
		
		JLabel titleLabel = new JLabel("PeerChess Settings");
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		titleLabel.setSize(300, 30);
		titleLabel.setLocation(90, 6);
		add(titleLabel);
		
		usernameLabel = new JLabel("Username: ");
		usernameLabel.setSize(120, 30);
		usernameLabel.setLocation(10, 30);
		add(usernameLabel);
		
		backButton = new JButton("Close");
		backButton.setSize(120, 30);
		backButton.setLocation(90, 400);
		backButton.addActionListener(new CloseButtonListener());
		add(backButton);
		
		setVisible(true);
		setResizable(false);
	}
	
	public class CloseButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			setVisible(false);
			frame.toFront();
		}
	}
}
