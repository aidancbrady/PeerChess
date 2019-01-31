package com.aidancbrady.peerchess.gui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.aidancbrady.peerchess.file.DataHandler;
import com.aidancbrady.peerchess.file.SaveHandler;
import com.aidancbrady.peerchess.net.ConnectionWaiter;

public class ChessFrame extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 1L;
	
	private ChessToolbar toolbar = new ChessToolbar(this);
	
	private ChessPanel chessPanel;
	private MenuPanel menu;
	private JoinPanel join;
	
	private OptionsFrame options;
	
	private ConnectingFrame connectingFrame = new ConnectingFrame();
	private WaitingFrame waitingFrame = new WaitingFrame(this);
	
	public ChessFrame()
	{
		setTitle("PeerChess");
		setSize(400, 600);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		addWindowListener(this);
		
		add(menu = new MenuPanel(this));
		
		add(join = new JoinPanel(this));
		join.setVisible(false);
		
		add(chessPanel = new ChessPanel(this));
		chessPanel.setVisible(false);
		
		setJMenuBar(toolbar.getMenuBar());
		addComponentListener(new ComponentListener() {
		    @Override
		    public void componentResized(ComponentEvent e)
		    {
		        onWindowResized();
		    }

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {}

            @Override
            public void componentHidden(ComponentEvent e) {}
		});
	}
	
	public ConnectingFrame getConnectingFrame()
	{
	    return connectingFrame;
	}
	
	public WaitingFrame getWaitingFrame()
	{
	    return waitingFrame;
	}
	
	public ChessPanel getPanel()
	{
	    return chessPanel;
	}
	
	public void openChess(boolean mp)
	{
		forceChess(mp);
		
		if(mp)
		{
    		waitingFrame.setThread(new ConnectionWaiter(chessPanel));
    		waitingFrame.setVisible(true);
		}
		else {
		    chessPanel.updateText();
		}
		
		revalidate();
        onWindowResized();
	}
	
	public void forceChess(boolean mp)
	{
		menu.setVisible(false);
		join.setVisible(false);
		
		chessPanel.component.multiplayer = mp;
		
		setSize(1024, 790);
		chessPanel.setVisible(true);
	}
	
	public void openSavedChess()
	{		
		JFileChooser chooser = new JFileChooser();
		
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File file) 
			{
				return file.getName().endsWith(".chess");
			}

			@Override
			public String getDescription() 
			{
				return "PeerChess saves (.chess)";
			}
		};
		
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(SaveHandler.saveDir);
		int returnVal = chooser.showOpenDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			if(SaveHandler.loadGame(chessPanel.component, chooser.getSelectedFile()))
			{
				openChess(chessPanel.component.multiplayer);
			}
			else {
				chessPanel.component.resetGame();
				JOptionPane.showMessageDialog(this, "Error loading game.");
			}
		}
	}
	
	public void openJoin()
	{
		menu.setVisible(false);
		chessPanel.setVisible(false);
		
		setSize(400, 600);
		join.setVisible(true);
	}
	
	public void openMenu()
	{
		if(chessPanel.isVisible())
		{
			if(!chessPanel.exit())
			{
				return;
			}
		}
		
		forceMenu();
	}
	
	public void forceMenu()
	{
		chessPanel.setVisible(false);
		join.setVisible(false);
		
		setSize(400, 600);
		menu.setVisible(true);
	}
	
	public void openOptions()
	{
		setSize(400, 600);
		
		if(options == null)
		{
			options = new OptionsFrame(this);
		}
		else {
			options.setVisible(true);
			options.toFront();
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		if(chessPanel != null && chessPanel.isVisible())
		{
			if(!chessPanel.exit())
			{
				return;
			}
		}
		
		DataHandler.save();
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
	
	public void onWindowResized()
	{
	    chessPanel.onWindowResized();
	}
}
