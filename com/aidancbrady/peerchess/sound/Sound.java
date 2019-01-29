package com.aidancbrady.peerchess.sound;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound 
{
	private boolean isPlaying;
	private String path;
	private Clip clip;
	
	public Sound(String s)
	{
		path = s;
		
		try {
			clip = AudioSystem.getClip();
			URL url = getClass().getClassLoader().getResource(path);
			AudioInputStream audio = AudioSystem.getAudioInputStream(url);
			
			clip.open(audio);
		} catch(Exception e) {
			System.err.println("Error loading sound: " + path);
			e.printStackTrace();
		}
	}
	
	public void play()
	{
		if(!isPlaying)
		{
			clip.start();
		}
		
		isPlaying = true;
	}
	
	public void stop()
	{
		if(isPlaying)
		{
			clip.stop();
		}
		
		isPlaying = false;
	}
}
