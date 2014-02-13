package com.aidancbrady.peerchess;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.aidancbrady.peerchess.file.SaveHandler;

public class ChessPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public ChessFrame frame;

	public ChessComponent chess;
	
	public JButton exitButton;
	public JButton sendButton;
	
	public JLabel opponentLabel;
	public JLabel titleLabel;
	public JLabel turnLabel;
	public JLabel statusLabel;
	
	public JTextArea chatBox;
	
	public JTextField chatField;
	
	public ChessPanel(ChessFrame f)
	{
		frame = f;
		
		setSize(1024, 790);
		setVisible(true);
		setLayout(null);
		
		setBackground(Color.GRAY);
		
		sendButton = new JButton("Send");
		sendButton.setSize(60, 32);
		sendButton.setLocation(968, 740);
		sendButton.addActionListener(new ChatListener());
		add(sendButton);
		
		exitButton = new JButton("Exit to Menu");
		exitButton.setSize(256, 30);
		exitButton.setLocation(768, 456);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.openMenu();
			}
		});
		add(exitButton);
		
		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setBorder(new TitledBorder(new EtchedBorder(), "Chatbox"));
		chatBox.setAutoscrolls(true);
		chatBox.setBackground(Color.LIGHT_GRAY);
		JScrollPane chatScroll = new JScrollPane(chatBox);
		chatScroll.setSize(256, 256);
		chatScroll.setLocation(768, 484);
		add(chatScroll);
		
		chatField = new JTextField();
		chatField.setFocusable(true);
		chatField.setSize(206, 30);
		chatField.setLocation(768, 740);
		chatField.setBackground(Color.LIGHT_GRAY);
		chatField.addActionListener(new ChatListener());
		add(chatField);
		
		add(chess = new ChessComponent(this));
		chess.setVisible(true);
		
		titleLabel = new JLabel("PeerChess - " + chess.side.name);
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		titleLabel.setSize(300, 20);
		titleLabel.setLocation(820, 5);
		add(titleLabel);
		
		opponentLabel = new JLabel("Opponent:");
		opponentLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
		opponentLabel.setSize(200, 40);
		opponentLabel.setLocation(774, 40);
		add(opponentLabel);
		
		statusLabel = new JLabel(chess.turn == chess.side ? "Ready for your move" : "Waiting for opponent");
		statusLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		statusLabel.setSize(200, 40);
		statusLabel.setLocation(815, 420);
		add(statusLabel);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		
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
			String s;
			
			loop:
			while(true)
			{
				s = JOptionPane.showInputDialog(this, "Please provide a name for this save.");

				if(s == null)
				{
					return false;
				}
				else if(s.isEmpty())
				{
					JOptionPane.showMessageDialog(this, "No name entered.");
					continue loop;
				}
				
				if(SaveHandler.saveExists(s))
				{
					int overwrite = JOptionPane.showConfirmDialog(this, "Already exists, overwrite?");
					
					if(overwrite == 2)
					{
						return false;
					}
					else if(returned == 1)
					{
						continue loop;
					}
				}
				
				break;
			}
		
			if(SaveHandler.saveGame(chess, s))
			{
				JOptionPane.showMessageDialog(this, "Game saved as \"" + SaveHandler.getTrimmedName(s) + ".\"");
			}
			else {
				JOptionPane.showMessageDialog(this, "Error saving game.");
			}
		}
		
		chess.resetBoard();
		
		return true;
	}
	
	public static class ChatListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			
		}
	}
}
