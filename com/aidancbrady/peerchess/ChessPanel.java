package com.aidancbrady.peerchess;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.aidancbrady.peerchess.ChessPiece.PieceType;
import com.aidancbrady.peerchess.ChessPiece.Side;
import com.aidancbrady.peerchess.file.SaveHandler;
import com.aidancbrady.peerchess.net.PeerConnection;
import com.aidancbrady.peerchess.piece.Piece;

public class ChessPanel extends JPanel implements MouseListener
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
	
	public PeerConnection connection;
	
	public JTextArea chatBox;
	
	public JTextField chatField;
	
	public int pawnReplace;
	
	public ChessPanel(ChessFrame f)
	{
		frame = f;
		
		setSize(1024, 790);
		setVisible(true);
		setLayout(null);
		addMouseListener(this);
		
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
		chatBox.setAutoscrolls(true);
		chatBox.setBackground(Color.LIGHT_GRAY);
		JScrollPane chatScroll = new JScrollPane(chatBox);
		chatScroll.setSize(256, 256);
		chatScroll.setLocation(768, 484);
		chatScroll.setBorder(new TitledBorder(new EtchedBorder(), "Chatbox"));
		chatScroll.setBackground(Color.LIGHT_GRAY);
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
		
		opponentLabel = new JLabel("Opponent: waiting");
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
	
	public void updateText()
	{
		if(opponentLabel != null && titleLabel != null && statusLabel != null)
		{
		    String opponentName = "Computer";
		    
		    if(chess.multiplayer)
		    {
		        opponentName = connection != null ? connection.username : "disconnected";
		    }
		    
			opponentLabel.setText("Opponent: " + opponentName);
			titleLabel.setText("PeerChess - " + chess.side.name);
			
			if(chess.winner == null)
			{
				statusLabel.setText(chess.turn == chess.side ? "Ready for your move" : "Waiting for opponent");
			}
			else {
				statusLabel.setText(chess.winner == chess.side ? "You win the game!" : "You lost the game.");
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		byte replace = shouldPawnReplace();
		
		if(replace == 0)
		{
			ChessPiece.getPieceList(Side.WHITE).get(pawnReplace).texture.draw(g, 830, 160, 128, 128);
		}
		else if(replace == 1)
		{
			ChessPiece.getPieceList(Side.BLACK).get(pawnReplace).texture.draw(g, 830, 160, 128, 128);
		}
	}

	public void appendChat(String str) 
	{	
		chatBox.append(str + "\n");
		chatBox.setCaretPosition(chatBox.getText().length() - 1);
	}
	
	/**
	 * @return -1 if no, 0 if white, 1 if black
	 */
	public byte shouldPawnReplace()
	{
		if(chess != null && chess.selected != null && chess.selected.housedPiece != null)
		{
			if(chess.selected.housedPiece.type == PieceType.PAWN)
			{
				if(chess.selected.housedPiece.side == Side.BLACK && chess.selected.pos.translate(0, 1).yPos == 7)
				{
					Piece piece = chess.selected.housedPiece.type.getPiece();
					
					ChessMove leftMove = new ChessMove(chess.selected.pos, chess.selected.pos.translate(-1, 1));
					ChessMove centerMove = new ChessMove(chess.selected.pos, chess.selected.pos.translate(0, 1));
					ChessMove rightMove = new ChessMove(chess.selected.pos, chess.selected.pos.translate(1, 1));
					
					if(piece.canMove(chess.grid, leftMove) || piece.canMove(chess.grid, centerMove) || piece.canMove(chess.grid, rightMove))
					{
						return 1;
					}
				}
				else if(chess.selected.housedPiece.side == Side.WHITE && chess.selected.pos.translate(0, -1).yPos == 0)
				{
					Piece piece = chess.selected.housedPiece.type.getPiece();
					
					ChessMove leftMove = new ChessMove(chess.selected.pos, chess.selected.pos.translate(-1, -1));
					ChessMove centerMove = new ChessMove(chess.selected.pos, chess.selected.pos.translate(0, -1));
					ChessMove rightMove = new ChessMove(chess.selected.pos, chess.selected.pos.translate(1, -1));
					
					if(piece.canMove(chess.grid, leftMove) || piece.canMove(chess.grid, centerMove) || piece.canMove(chess.grid, rightMove))
					{
						return 0;
					}
				}
			}
		}
		
		return -1;
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
		
		chess.resetGame();
		
		return true;
	}
	
	public class ChatListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			if(connection != null && !chatField.getText().isEmpty())
			{
				if(chatField.getText().length() <= 250)
				{
					String text = chatField.getText().trim();
					
					if(text.isEmpty())
					{
						return;
					}
					
					connection.chat(text);
					chatField.setText("");
					appendChat(PeerChess.instance().username + ": " + text);
				}
				else {
					JOptionPane.showMessageDialog(ChessPanel.this, "Chat messages must be at most 250 characters.");
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		int x = arg0.getX();
		int y = arg0.getY();
		
		if(x >= 830 && x <= 958 && y >= 160 && y <= 288)
		{
			byte replace = shouldPawnReplace();
			
			if(replace != -1)
			{
				if(replace == 0 && pawnReplace > PieceType.values().length)
				{
					pawnReplace = 0;
				}
				
				pawnReplace = (pawnReplace+1)%PieceType.values().length;
				repaint();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
