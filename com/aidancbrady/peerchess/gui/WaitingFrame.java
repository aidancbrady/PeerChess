package com.aidancbrady.peerchess.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.aidancbrady.peerchess.net.ConnectionWaiter;

public class WaitingFrame extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 1L;

	public ChessFrame frame;
	
	public JProgressBar progressBar;
	
	public JButton cancelButton;
	
	public ConnectionWaiter thread;
	
	public WaitingFrame(ChessFrame f)
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
		
		JLabel connectingLabel = new JLabel("Awaiting Connection...");
		connectingLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		connectingLabel.setSize(160, 30);
		connectingLabel.setLocation(106, 10);
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
				close();
				frame.forceMenu();
		        setVisible(false);
			}
		});
		cancelButton.setSize(120, 30);
		cancelButton.setLocation(120, 110);
		add(cancelButton);
	}
	
	public WaitingFrame setThread(ConnectionWaiter c)
	{
		thread = c;
		
		return this;
	}
	
	public void close()
	{
	    try {
	        if(thread != null)
	        {
	            if(thread.serverSocket != null)
	            {
	                thread.serverSocket.close();
	            }
                
                if(thread.responseThread != null)
                {
                    thread.responseThread.socket.close();
                }
                
                thread.interrupt();
	        }
        } catch(Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		try {
			thread.serverSocket.close();
			thread.interrupt();
			frame.chessPanel.chess.resetGame();
			frame.forceMenu();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}
