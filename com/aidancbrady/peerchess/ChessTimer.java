package com.aidancbrady.peerchess;

public final class ChessTimer extends Thread
{
	public ChessTimer()
	{
		setDaemon(true);
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try {
				Thread.sleep(1);
			} catch(Exception e) {}
		}
	}
}
