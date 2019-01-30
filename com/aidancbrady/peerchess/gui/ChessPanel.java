package com.aidancbrady.peerchess.gui;

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

import com.aidancbrady.peerchess.ChessComponent;
import com.aidancbrady.peerchess.PeerChess;
import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.file.SaveHandler;
import com.aidancbrady.peerchess.game.ChessPiece.Endgame;
import com.aidancbrady.peerchess.game.ChessPiece.PieceType;
import com.aidancbrady.peerchess.game.ChessPiece.Side;
import com.aidancbrady.peerchess.net.PeerConnection;

public class ChessPanel extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	public ChessFrame frame;

	public ChessComponent chess;
	
	private PeerConnection connection;
	
	private JButton hintButton;
	private JButton revertButton;
	
	private JButton exitButton;
	private JButton sendButton;
	
	private JLabel opponentLabel;
	private JLabel titleLabel;
	private JLabel statusLabel;
	
	private JTextArea chatBox;
	private JScrollPane chatScroll;
	
	private JTextField chatField;
	
	private int pawnReplaceIndex;
	
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
		
		hintButton = new JButton("Hint");
		hintButton.setSize(127, 30);
		hintButton.setLocation(768, 422);
		hintButton.setEnabled(false);
		hintButton.addActionListener(e -> chess.hint());
		add(hintButton);
		
		revertButton = new JButton("Revert");
		revertButton.setSize(127, 30);
		revertButton.setLocation(898, 422);
		revertButton.setEnabled(false);
		revertButton.addActionListener(e -> chess.takeBackLastMove());
		add(revertButton);
		
		exitButton = new JButton("Exit to Menu");
		exitButton.setSize(256, 30);
		exitButton.setLocation(768, 456);
		exitButton.addActionListener(e -> frame.openMenu());
		add(exitButton);
		
		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setAutoscrolls(true);
		chatBox.setBackground(Color.LIGHT_GRAY);
		chatScroll = new JScrollPane(chatBox);
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
		
		titleLabel = new JLabel("PeerChess - " + chess.getGame().getSide().name);
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		titleLabel.setSize(300, 20);
		titleLabel.setLocation(820, 5);
		add(titleLabel);
		
		opponentLabel = new JLabel("Opponent: waiting");
		opponentLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
		opponentLabel.setSize(200, 40);
		opponentLabel.setLocation(774, 40);
		add(opponentLabel);
		
		statusLabel = new JLabel(chess.getGame().getTurn() == chess.getGame().getSide() ? "Ready for your move" : "Waiting for opponent");
		statusLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		statusLabel.setLocation(815, 420);
		statusLabel.setSize(200, 40);
		add(statusLabel);
	}
	
	public void updateText()
	{
		if(opponentLabel != null && titleLabel != null && statusLabel != null)
		{
		    String opponentName = "Computer";
		    
		    if(chess.multiplayer)
		    {
		        opponentName = connection != null ? connection.getOpponent() : "disconnected";
		    }
		    
			opponentLabel.setText("Opponent: " + opponentName);
			titleLabel.setText("PeerChess - " + chess.getGame().getSide().name);
			
			if(chess.getGame().getEndgame() == null)
			{
				statusLabel.setText(chess.getGame().getTurn() == chess.getGame().getSide() ? "Ready for your move" : "Waiting for opponent");
				
				if(!chess.isDragging() && !chess.isMoving() && chess.getGame().getTurn() == chess.getGame().getSide())
				{
			        hintButton.setEnabled(PeerChess.instance().enableHints);
                    revertButton.setEnabled(chess.multiplayer == false && chess.getGame().getMoves().size() >= 2);
				}
				else {
				    hintButton.setEnabled(false);
				    revertButton.setEnabled(false);
				}
			}
			else {
			    if(chess.getGame().getEndgame() == Endgame.STALEMATE)
			    {
			        statusLabel.setText("Stalemate!");
			    }
			    else if(chess.getGame().getEndgame() == Endgame.DRAW)
			    {
			        statusLabel.setText("Draw!");
			    }
			    else {
			        statusLabel.setText(Endgame.get(chess.getGame().getSide()) == chess.getGame().getEndgame() ? "You win the game!" : "You lost the game.");
			    }
			    
			    hintButton.setEnabled(false);
                revertButton.setEnabled(false);
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		byte replace = chess.shouldPawnReplace();
		
		int width = frame.getContentPane().getWidth();
		int size = (int)((width-chess.getWidth())*(2D/3D));
		int yStart = opponentLabel.getY()+opponentLabel.getHeight()+15;
		size = Math.min(size, (statusLabel.getY()-yStart)/2);
		int x = chess.getWidth() + (width-chess.getWidth())/2 - size/2;
		int y = yStart + (int)((statusLabel.getY()-yStart)*(1D/3D)) - size/2;
		
		if(replace != -1)
		{
		    PeerUtils.getPawnReplace(replace == 0 ? Side.WHITE : Side.BLACK, pawnReplaceIndex).getTexture().draw(g, x, y, size, size);
		}
	}

	public void appendChat(String str) 
	{	
		chatBox.append(str + "\n");
		chatBox.setCaretPosition(chatBox.getText().length() - 1);
	}
	
	public boolean exit()
	{
	    if(!chess.multiplayer && chess.getGame().getMoves().isEmpty())
	    {
	        chess.resetGame();
	        return true;
	    }
	    
	    if(chess.getGame().getEndgame() == null && (chess.host || !chess.multiplayer))
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
	    }
		
		chess.resetGame();
		return true;
	}
	
	public void reset()
	{
	    pawnReplaceIndex = 0;
	    chatBox.setText("");
	    
	    if(opponentLabel != null)
        {
            opponentLabel.setText("Opponent: waiting");
        }
        
        if(connection != null)
        {
            connection.close();
        }
        
        frame.getWaitingFrame().close();
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
	
	public void setConnection(PeerConnection conn)
	{
	    connection = conn;
	}
	
	public PeerConnection getConnection()
	{
	    return connection;
	}
	
	public int getPawnReplaceIndex()
	{
	    return pawnReplaceIndex;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
	    int x = chess.getWidth();
	    int y = opponentLabel.getY()+opponentLabel.getHeight();
	    int xMax = frame.getContentPane().getWidth();
	    int yMax = statusLabel.getY();
        
		int mouseX = arg0.getX();
		int mouseY = arg0.getY();
		
		if(mouseX >= x && mouseX <= xMax && mouseY >= y && mouseY <= yMax)
		{
			byte replace = chess.shouldPawnReplace();
			
			if(replace != -1)
			{
				pawnReplaceIndex = (pawnReplaceIndex+1)%(PieceType.values().length-1);
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
	
	@Override
	public void setVisible(boolean visible)
	{
	    super.setVisible(visible);
	    
	    frame.setResizable(visible);
	}
	
	protected void onWindowResized()
	{
	    int width = frame.getContentPane().getWidth();
        int height = frame.getContentPane().getHeight();
        
        int size = Math.min(width, height);
        
        double minRatio = 1024D/768D;
        double ratio = (double)width/(double)height;
        
        if(ratio < minRatio)
        {
            size = (int)(width*(768D/1024D));
        }
        
        chess.setSize(size, size);
        chess.resize(size);
        
        chatScroll.setLocation(size, (int)(size*0.6));
        chatScroll.setSize(width-size, (int)(size*0.4)-30);
        
        exitButton.setLocation(size, chatScroll.getY()-30);
        exitButton.setSize(width-size, 30);
        
        hintButton.setLocation(size, exitButton.getY()-34);
        hintButton.setSize((width-size)/2 - 1, 30);
        
        revertButton.setLocation(hintButton.getX()+(int)hintButton.getSize().getWidth()+2, exitButton.getY()-34);
        revertButton.setSize((width-size)/2 - 1, 30);
        
        chatField.setLocation(size, chatScroll.getY()+chatScroll.getHeight());
        chatField.setSize(width-size-60, 30);
        
        sendButton.setLocation(size+chatField.getWidth(), chatField.getY());
        
        int textCenter = size + (width-size)/2;
        
        statusLabel.setLocation(textCenter - (int)(statusLabel.getPreferredSize().getWidth()/2), hintButton.getY()-40);
        titleLabel.setLocation(textCenter - (int)(titleLabel.getPreferredSize().getWidth()/2), 5);
        opponentLabel.setLocation(size + 6, 40);
	}
}
