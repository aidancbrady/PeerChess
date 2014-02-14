package com.aidancbrady.peerchess;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.aidancbrady.peerchess.net.GameConnector;

public class ConnectingFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	public ChessFrame frame;
	
	public JProgressBar progressBar;
	
	public JButton cancelButton;
	
	public GameConnector thread;
	
	public ConnectingFrame(ChessFrame f, GameConnector c)
	{
		frame = f;
		thread = c;
		
		setTitle("Connection");
		setSize(360, 180);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//HIDE_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		JLabel connectingLabel = new JLabel("Connecting...");
		connectingLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		connectingLabel.setSize(200, 30);
		connectingLabel.setLocation(136, 10);
		add(connectingLabel);
		
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setSize(300, 40);
		progressBar.setLocation(30, 50);
		add(progressBar);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try {
					thread.socket.close();
					thread.interrupt();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		cancelButton.setSize(120, 30);
		cancelButton.setLocation(120, 110);
		add(cancelButton);
	}
}
