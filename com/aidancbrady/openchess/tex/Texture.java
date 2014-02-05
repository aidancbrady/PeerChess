package com.aidancbrady.openchess.tex;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture 
{
	public BufferedImage img;
	
	public String path;
	
	public Texture(String path)
	{
		try {
			img = ImageIO.read(getClass().getResource(path));
		} catch(Exception e) {
			e.printStackTrace();
		}
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
		draw(g, x, y);
	}
	
	public void draw(Graphics g, int x, int y, int width, int height)
	{
		g.drawImage(img, x, y, width, height, null);
	}
}
