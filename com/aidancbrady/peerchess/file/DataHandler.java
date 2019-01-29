package com.aidancbrady.peerchess.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.aidancbrady.peerchess.PeerChess;

public final class DataHandler 
{
	private static File dataDir = new File(getHomeDirectory() + File.separator + "Documents" + File.separator + "PeerChess" + File.separator + "Data");
	private static File dataFile = new File(dataDir, "Data.txt");
	
	public static void init()
	{
		load();
	}
	
	public static void load()
	{
		try {
			dataDir.mkdirs();
			
			if(!dataFile.exists())
			{
				return;
			}
			
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			
			PeerChess.instance().username = reader.readLine().trim();
			PeerChess.instance().enableSoundEffects = Boolean.parseBoolean(reader.readLine().trim());
			PeerChess.instance().enableAnimations = Boolean.parseBoolean(reader.readLine().trim());
			PeerChess.instance().enableVisualGuides = Boolean.parseBoolean(reader.readLine().trim());
			PeerChess.instance().enableHints = Boolean.parseBoolean(reader.readLine().trim());
			PeerChess.instance().difficulty = Integer.parseInt(reader.readLine().trim());
			
			reader.close();
		} catch(Exception e) {
			System.err.println("An error occured while loading from data file:");
			e.printStackTrace();
		}
	}
	
	public static void save()
	{
		try {
			if(dataFile.exists())
			{
				dataFile.delete();
			}
			
			dataFile.createNewFile();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
			
			writer.append(PeerChess.instance().username.trim());
			writer.newLine();
			
			writer.append(Boolean.toString(PeerChess.instance().enableSoundEffects));
			writer.newLine();
			
			writer.append(Boolean.toString(PeerChess.instance().enableAnimations));
            writer.newLine();
			
			writer.append(Boolean.toString(PeerChess.instance().enableVisualGuides));
			writer.newLine();
			
			writer.append(Boolean.toString(PeerChess.instance().enableHints));
            writer.newLine();
			
			writer.append(Integer.toString(PeerChess.instance().difficulty));
			writer.newLine();
			
			writer.flush();
			writer.close();
		} catch(Exception e) {
			System.err.println("An error occured while saving to data file:");
			e.printStackTrace();
		}
	}
	
	public static String getHomeDirectory()
	{
		return System.getProperty("user.home");
	}
}
