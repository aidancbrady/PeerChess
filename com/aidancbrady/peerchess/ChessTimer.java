package com.aidancbrady.peerchess;

import java.util.HashSet;
import java.util.Set;

import com.aidancbrady.peerchess.net.PeerScanner;
import com.aidancbrady.peerchess.net.PeerScanner.ThreadPing;

public final class ChessTimer extends Thread
{
	public PeerChess chess = PeerChess.instance();
	
	public Set<ThreadPing> pings = new HashSet<ThreadPing>();
	
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
				if(chess.frame.chess.chess.isMoving())
				{
					chess.frame.chess.chess.currentAnimation.update();
				}
				
				for(ThreadPing ping : pings)
				{
					ping.pingMillis++;
					
					if(ping.pingMillis == PeerScanner.MAX_PING)
					{
						ping.invalid = true;
						pings.remove(ping);
					}
				}
				
				Thread.sleep(1);
			} catch(Exception e) {}
		}
	}
}
