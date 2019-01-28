package com.aidancbrady.peerchess.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.aidancbrady.peerchess.PeerUtils;
import com.aidancbrady.peerchess.net.GameConnector;
import com.aidancbrady.peerchess.net.GameScanner;
import com.aidancbrady.peerchess.net.GameScanner.Server;

public class JoinPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public ChessFrame frame;
	
	public JButton exitButton;
	public JButton refreshButton;
	public JButton joinButton;
	public JButton connectButton;
	
	public GameScanner scanner;
	
	public JLabel refreshLabel;
	
	public JTextField ipField;
	
	public JList<String> serverList;
	
	public List<Server> serversLoaded = new ArrayList<Server>();
	
	public JProgressBar refreshBar;

	public JoinPanel(ChessFrame f)
	{
		frame = f;
		
		setSize(400, 600);
		setLayout(null);
		
		JLabel titleLabel = new JLabel("Game Connection Menu");
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		titleLabel.setSize(200, 30);
		titleLabel.setLocation(100, 5);
		add(titleLabel);
		
		JLabel listLabel = new JLabel("Local Server List");
		listLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		listLabel.setSize(200, 30);
		listLabel.setLocation(10, 50);
		add(listLabel);
		
		JLabel ipLabel = new JLabel("Direct IP Connection");
		ipLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		ipLabel.setSize(200, 30);
		ipLabel.setLocation(10, 400);
		add(ipLabel);
		
		refreshLabel = new JLabel("Refreshing...");
		refreshLabel.setSize(200, 30);
		refreshLabel.setLocation(310, 50);
		refreshLabel.setVisible(false);
		add(refreshLabel);
		
		serverList = new JList<String>();
		serverList.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent event)
			{
				if(event.getClickCount() == 2)
				{
					doLocalConnect();
				}
			}
		});
		
		serverList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		serverList.setBorder(new TitledBorder(new EtchedBorder(), "LAN Games"));
		serverList.setVisible(true);
		serverList.setFocusable(true);
		serverList.setEnabled(true);
		serverList.setSelectionInterval(1, 1);
		serverList.setToolTipText("Servers on your local network");
		JScrollPane onlinePane = new JScrollPane(serverList);
		onlinePane.setSize(new Dimension(400, 250));
		onlinePane.setLocation(0, 80);
		add(onlinePane);
		
		ipField = new JTextField();
		ipField.setFocusable(true);
		ipField.setSize(180, 20);
		ipField.setLocation(5, 430);
		ipField.addActionListener(new ConnectListener());
		add(ipField);
		
		exitButton = new JButton("Exit to Menu");
		exitButton.setSize(200, 30);
		exitButton.setLocation(100, 540);
		exitButton.addActionListener(new ExitButtonListener());
		add(exitButton);
		
		refreshButton = new JButton("Refresh");
		refreshButton.setSize(100, 30);
		refreshButton.setLocation(5, 330);
		refreshButton.addActionListener(new RefreshButtonListener());
		add(refreshButton);
		
		joinButton = new JButton("Join");
		joinButton.setSize(100, 30);
		joinButton.setLocation(295, 330);
		joinButton.addActionListener(new JoinButtonListener());
		add(joinButton);
		
		connectButton = new JButton("Connect");
		connectButton.setSize(100, 20);
		connectButton.setLocation(190, 430);
		connectButton.addActionListener(new ConnectListener());
		add(connectButton);
		
		JLabel infoLabel = new JLabel("Directly connect to an IPv4 address");
		infoLabel.setSize(300, 30);
		infoLabel.setLocation(10, 450);
		add(infoLabel);
		
		refreshBar = new JProgressBar();
		refreshBar.setSize(180, 30);
		refreshBar.setLocation(110, 330);
		refreshBar.setMinimum(0);
		refreshBar.setMaximum(GameScanner.MAX_PING);
		refreshBar.setVisible(false);
		add(refreshBar);
	}
	
	public void doLocalConnect()
	{
		int index = serverList.getSelectedIndex();
		
		if(index != -1)
		{
			Server server = serversLoaded.get(index);
			
			if(server != null)
			{
			    synchronized(frame.connecting)
			    {
    				frame.connecting.setThread(new GameConnector(server, frame.chessPanel));
    				frame.connecting.setVisible(true);
			    }
			}
		}
	}
	
	public void doRemoteConnect()
	{
		String s = ipField.getText();
		
		if(PeerUtils.isValidIP(s))
		{
		    synchronized(frame.connecting)
		    {
    			frame.connecting.setThread(new GameConnector(new Server(null, s.trim()), frame.chessPanel));
    			frame.connecting.setVisible(true);
		    }
		    
			ipField.setText("");
		}
	}
	
	public class ExitButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			frame.openMenu();
			
			if(scanner != null && scanner.isAlive())
			{
				scanner.interrupt();
			}
		}
	}
	
	public class RefreshButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			doRefresh();
		}
	}
	
	public class JoinButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			doLocalConnect();
		}
	}
	
	public class ConnectListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			doRemoteConnect();
		}
	}
	
	public void doRefresh()
	{
	    if(refreshButton.isEnabled())
	    {
    	    serverList.setListData(new Vector<String>());
            
            refreshBar.setIndeterminate(true);
            refreshLabel.setVisible(true);
            refreshBar.setVisible(true);
            refreshButton.setEnabled(false);
            
            (scanner = new GameScanner(JoinPanel.this)).start();
	    }
	}
	
	public void populateServers(List<Server> servers)
	{
		refreshLabel.setVisible(false);
		refreshBar.setVisible(false);
		refreshButton.setEnabled(true);
		
		if(servers.isEmpty())
		{
			return;
		}
		
		serversLoaded = servers;
		
		Vector<String> listVector = new Vector<String>();
		
		for(Server s : servers)
		{
			listVector.add(s.username + "'s Game [" + s.ip + "]");
		}
		
		serverList.setListData(listVector);
	}
	
	@Override
	public void setVisible(boolean visible)
	{
	    super.setVisible(visible);
	    
	    if(visible)
	    {
	        doRefresh();
	    }
	}
}
