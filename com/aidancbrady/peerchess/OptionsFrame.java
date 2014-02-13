package com.aidancbrady.peerchess;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class OptionsFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public ChessFrame frame;
	
	public JButton backButton;
	public JButton usernameButton;
	
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
		
		usernameLabel = new JLabel("Username: " + PeerChess.instance().username);
		usernameLabel.setSize(120, 30);
		usernameLabel.setLocation(16, 40);
		add(usernameLabel);
		
		backButton = new JButton("Close");
		backButton.setSize(120, 30);
		backButton.setLocation(90, 400);
		backButton.addActionListener(new CloseButtonListener());
		add(backButton);
		
		usernameButton = new JButton("Edit");
		usernameButton.setSize(80, 30);
		usernameButton.setLocation(210, 40);
		usernameButton.addActionListener(new UsernameButtonListener());
		add(usernameButton);
		
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
	
	public class UsernameButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			while(true)
			{
				String result = JOptionPane.showInputDialog(OptionsFrame.this, "Enter a new username");
				
				if(result != null && !result.isEmpty())
				{
					result = result.trim();
					
					if(result.length() <= 16)
					{
						PeerChess.instance().username = result;
						usernameLabel.setText("Username: " + PeerChess.instance().username);
						break;
					}
					else {
						JOptionPane.showMessageDialog(OptionsFrame.this, "Username must be less than 16 characters");
					}
				}
			}
		}
	}
}
