package com.aidancbrady.peerchess.gui;

import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.aidancbrady.peerchess.net.GameConnector;

public class ConnectingFrame extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 1L;

	public ChessFrame frame;
	
	public JProgressBar progressBar;
	
	public JButton cancelButton;
	
	public GameConnector thread;
	
	public ConnectingFrame(ChessFrame f)
	{
		frame = f;
		
		setTitle("Connection");
		setSize(360, 180);
		
		setVisible(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		setAlwaysOnTop(true);
		addWindowListener(this);
		
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
		cancelButton.addActionListener(e -> {
		    try {
                thread.socket.close();
                thread.interrupt();
                setVisible(false);
            } catch(Exception e1) {
                e1.printStackTrace();
            }
		});
		cancelButton.setSize(120, 30);
		cancelButton.setLocation(120, 110);
		add(cancelButton);
	}
	
	public ConnectingFrame setThread(GameConnector c)
	{
		thread = c;
		
		return this;
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) 
	{
		try {
			thread.socket.close();
			thread.interrupt();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
