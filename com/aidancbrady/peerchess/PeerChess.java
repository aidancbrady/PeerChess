package com.aidancbrady.peerchess;

import javax.swing.SwingUtilities;

public class PeerChess
{
	private static PeerChess instance = new PeerChess();
	
	public ChessFrame frame;
	
	public int port = 26325;
	
	public String username;
	 
	public static void main(String[] args)
	{
		instance().init();
	}
	
	public static PeerChess instance()
	{
		return instance;
	}
	
	public void init()
	{
		initMacOSX();
		
        Runnable doSwingLater = new Runnable() {
            @Override
            public void run() 
            {
                frame = new ChessFrame();
            }
        };
         
        SwingUtilities.invokeLater(doSwingLater);
	}
	
	private void initMacOSX()
	{
		try {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PeerChess");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		} catch(Exception e) {}
	}
}
