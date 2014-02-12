package com.aidancbrady.peerchess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class JoinPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public ChessFrame frame;
	
	public JButton backButton;

	public JoinPanel(ChessFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setVisible(true);
		setLayout(null);
		
		backButton = new JButton("Back");
		backButton.setSize(60, 30);
		backButton.setLocation(5, 5);
		backButton.addActionListener(new BackButtonListener());
		add(backButton);
	}
	
	public class BackButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			frame.openMenu();
		}
	}
}
