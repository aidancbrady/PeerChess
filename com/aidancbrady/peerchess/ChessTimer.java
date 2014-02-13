package com.aidancbrady.peerchess;

import java.util.HashSet;
import java.util.Set;

public final class ChessTimer extends Thread
{
	public PeerChess chess = PeerChess.instance();
	
	public Set<ITicker> tickers = new HashSet<ITicker>();
	
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
				if(chess != null && chess.frame != null && chess.frame.chess != null && chess.frame.chess.chess != null)
				{
					if(chess.frame.chess.chess.isMoving())
					{
						chess.frame.chess.chess.currentAnimation.update();
					}
				}
				
				Set<ITicker> remove = new HashSet<ITicker>();
				
				for(ITicker ticker : tickers)
				{
					if(!ticker.tick())
					{
						remove.add(ticker);
					}
				}
				
				tickers.removeAll(remove);
				
				Thread.sleep(1);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static interface ITicker
	{
		public boolean tick();
	}
}
