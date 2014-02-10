package com.aidancbrady.peerchess;

import javax.swing.SwingUtilities;

public class PeerChess
{
	static ChessFrame jFrameWindow;
	 
	public static void main(String[] args)
	{
		initMacOSX();
		
        Runnable doSwingLater = new Runnable() {
            @Override
            public void run() 
            {
                jFrameWindow = new ChessFrame();
                jFrameWindow.setVisible(true);
            }
        };
         
        SwingUtilities.invokeLater(doSwingLater);
	}
	
	private static void initMacOSX()
	{
		try {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PeerChess");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		} catch(Exception e) {}
	}
}
