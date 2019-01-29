package com.aidancbrady.peerchess;

import java.util.Calendar;

import javax.swing.SwingUtilities;

import com.aidancbrady.peerchess.file.DataHandler;
import com.aidancbrady.peerchess.file.SaveHandler;
import com.aidancbrady.peerchess.gui.ChessFrame;

public class PeerChess
{
	private static PeerChess instance = new PeerChess();
	
	private ChessFrame frame;
	
	private ChessTimer timer;
	
	private int port = 26325;
	
	public String username = "Guest";
	
    private Calendar calendar = Calendar.getInstance();

    private boolean debug = true;
	
    public boolean enableSoundEffects = true;
    public boolean enableAnimations = true;
    public boolean enableVisualGuides = true;
    public boolean enableHints = true;
    public int difficulty = 4;
	 
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
	
	public int getPort()
	{
	    return port;
	}
	
	public ChessTimer getTimer()
	{
	    return timer;
	}
	
	public ChessFrame getFrame()
	{
	    return frame;
	}
	
	public Calendar getCalendar()
	{
	    return calendar;
	}
	
	public boolean isDebug()
	{
	    return debug;
	}
	
	private void initMacOSX()
	{
		try {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PeerChess");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		} catch(Exception e) {}
	}
}
