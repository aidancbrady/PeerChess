package com.aidancbrady.peerchess;

import java.util.Calendar;

import javax.swing.SwingUtilities;

import com.aidancbrady.peerchess.file.DataHandler;
import com.aidancbrady.peerchess.file.SaveHandler;
import com.aidancbrady.peerchess.gui.ChessFrame;

public class PeerChess
{
	private static PeerChess instance = new PeerChess();
	
	public ChessFrame frame;
	
	public ChessTimer timer;
	
	public int port = 26325;
	
	public String username = "Guest";
	
	public boolean enableSoundEffects = true;
	public boolean enableVisualGuides = true;
	
	public Calendar calendar = Calendar.getInstance();
	
	public boolean debug = true;
	 
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
		
		DataHandler.init();
		SaveHandler.init();
		
		(timer = new ChessTimer()).start();
		
        Runnable doSwingLater = new Runnable() {
            @Override
            public void run() 
            {
                frame = new ChessFrame();
            }
        };
         
        SwingUtilities.invokeLater(doSwingLater);
	}
	
	public ChessComponent getChess()
	{
		return frame.chess.chess;
	}
	
	private void initMacOSX()
	{
		try {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PeerChess");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		} catch(Exception e) {}
	}
}
