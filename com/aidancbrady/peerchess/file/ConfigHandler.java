package com.aidancbrady.peerchess.file;

import java.io.File;

public final class ConfigHandler 
{
	public static File dataDir = new File(getHomeDirectory() + File.separator + "Documents" + File.separator + "PeerChess" + File.separator + "Data");
	
	public static void init()
	{
		dataDir.mkdirs();
		
		load();
	}
	
	public static void load()
	{
		
	}
	
	public static void save()
	{
		
	}
	
	public static String getHomeDirectory()
	{
		return System.getProperty("user.home");
	}
}
