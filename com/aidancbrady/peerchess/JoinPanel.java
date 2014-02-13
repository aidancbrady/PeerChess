package com.aidancbrady.peerchess;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class JoinPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public ChessFrame frame;
	
	public JButton backButton;
	public JButton refreshButton;
	public JButton joinButton;
	public JButton connectButton;
	
	public JTextField ipField;
	
	public JList serverList;

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
		
		JLabel listLabel = new JLabel("Local Server List");
		listLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		listLabel.setSize(200, 30);
		listLabel.setLocation(10, 50);
		add(listLabel);
		
		JLabel ipLabel = new JLabel("Direct IP Connection");
		listLabel.setFont(new Font("Helvetica", Font.BOLD, 14));
		listLabel.setSize(200, 30);
		listLabel.setLocation(10, 400);
		add(ipLabel);
		
		serverList = new JList();
		serverList.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent event)
			{
				if(event.getClickCount() == 2)
				{
					
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
		ipField.setSize(140, 20);
		ipField.setLocation(50, 400);
		ipField.addActionListener(new IPFieldListener());
		add(ipField);
		
		refreshButton = new JButton("Refresh");
		refreshButton.setSize(100, 30);
		refreshButton.setLocation(295, 330);
		refreshButton.addActionListener(new RefreshButtonListener());
		add(refreshButton);
		
		joinButton = new JButton("Join");
		joinButton.setSize(100, 30);
		joinButton.setLocation(5, 330);
		joinButton.addActionListener(new JoinButtonListener());
		add(joinButton);
		
		connectButton = new JButton("Connect");
		connectButton.setSize(120, 30);
		connectButton.setLocation(5, 550);
		connectButton.addActionListener(new ConnectButtonListener());
		add(connectButton);
	}
	
	public class BackButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			frame.openMenu();
		}
	}
	
	public class RefreshButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			
		}
	}
	
	public class JoinButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			
		}
	}
	
	public class ConnectButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			
		}
	}
	
	public class IPFieldListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			
		}
	}
}
