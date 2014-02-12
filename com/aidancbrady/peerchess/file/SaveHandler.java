package com.aidancbrady.peerchess.file;

import java.io.File;

import com.aidancbrady.peerchess.ChessComponent;

public final class SaveHandler 
{
	public static File saveDir = new File(getHomeDirectory() + File.separator + "Documents" + File.separator + "PeerChess" + File.separator + "Saves");
	
	public static void init()
	{
		saveDir.mkdirs();
	}
	
	public static boolean saveExists(String name)
	{
		name = name.trim().replace(".save", "");
		
		for(File file : saveDir.listFiles())
		{
			if(file.getName().equals(name + ".save"))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static void saveGame(ChessComponent chess)
	{
		
	}
	
	public static String getHomeDirectory()
	{
		return System.getProperty("user.home");
	}
}
