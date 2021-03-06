package com.aidancbrady.peerchess.gui;

import java.awt.Graphics;

import javax.swing.JComponent;

public class OverlayComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	private ChessComponent component;
	
	public OverlayComponent(ChessComponent c)
	{
		component = c;
		
		setSize(768, 768);
		setLocation(0, 0);
		setVisible(true);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		if(component.isMoving())
		{
			component.currentMove.render(g);
		}
		
		if(component.isDragging())
		{
		    component.currentDrag.render(g);
		}
	}
}
