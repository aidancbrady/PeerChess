package com.aidancbrady.peerchess;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class OptionsFrame extends JFrame implements ItemListener
{
	private static final long serialVersionUID = 1L;
	
	public ChessFrame frame;
	
	public JButton backButton;
	public JButton usernameButton;
	
	public JCheckBox soundEffectsBox;
	public JCheckBox visualGuidesBox;
	
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
		usernameLabel.setSize(240, 30);
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
		
		soundEffectsBox = new JCheckBox();
		soundEffectsBox.setSelected(PeerChess.instance().enableSoundEffects);
		soundEffectsBox.setSize(24, 24);
		soundEffectsBox.setLocation(16, 90);
		soundEffectsBox.addItemListener(this);
		add(soundEffectsBox);
		
		visualGuidesBox = new JCheckBox();
        visualGuidesBox.setSelected(PeerChess.instance().enableVisualGuides);
        visualGuidesBox.setSize(24, 24);
        visualGuidesBox.setLocation(16, 110);
        visualGuidesBox.addItemListener(this);
        add(visualGuidesBox);
		
		JLabel soundEffectsLabel = new JLabel("Enable sound effects");
		soundEffectsLabel.setSize(200, 30);
		soundEffectsLabel.setLocation(40, 86);
		add(soundEffectsLabel);
		
		JLabel visualGuidesLabel = new JLabel("Enable visual guides");
        visualGuidesLabel.setSize(200, 30);
        visualGuidesLabel.setLocation(40, 106);
        add(visualGuidesLabel);
		
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
						continue;
					}
				}
				else if(result == null)
				{
					break;
				}
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) 
	{
		if(arg0.getSource() == soundEffectsBox)
		{
			PeerChess.instance().enableSoundEffects = arg0.getStateChange() == 1;
		}
		else if(arg0.getSource() == visualGuidesBox)
		{
		    PeerChess.instance().enableVisualGuides = arg0.getStateChange() == 1;
		}
	}
}
