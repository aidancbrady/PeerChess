package com.aidancbrady.peerchess.tex;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Texture 
{
	private static Map<String, Texture> loadedTextures = new HashMap<String, Texture>();
	
	public BufferedImage img;
	
	public String path;
	
	public Texture(String path) throws Exception
	{
		img = ImageIO.read(new File(path));
	}
	
	public static Texture load(String path)
	{
		if(loadedTextures.containsKey(path))
		{
			return loadedTextures.get(path);
		}
		
		try {
			Texture ret = new Texture(path);
			loadedTextures.put(path, ret);
			
			return ret;
		} catch(Exception e) {
			System.err.println("Error while loading texture: " + path);
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int getWidth()
	{
		return img.getWidth();
	}
	
	public int getHeight()
	{
		return img.getHeight();
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(img, 0, 0, null);
	}
	
	public void draw(Graphics g, int x, int y)
	{
		g.drawImage(img, x, y, null);
	}
	
	public void draw(Graphics g, int x, int y, int width, int height)
	{
		g.drawImage(img, x, y, width, height, null);
	}
}
