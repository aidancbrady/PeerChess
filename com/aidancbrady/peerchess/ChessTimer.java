package com.aidancbrady.peerchess;

public final class ChessTimer extends Thread
{
	public ChessComponent component;
	
	public ChessTimer(ChessComponent c)
	{
		component = c;
		
		setDaemon(true);
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try {
				if(component.isMoving())
				{
					component.currentAnimation.update();
				}
				
				Thread.sleep(1);
			} catch(Exception e) {}
		}
	}
}
