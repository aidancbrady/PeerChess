package com.aidancbrady.peerchess;

import java.util.HashSet;
import java.util.Set;

public final class ChessTimer extends Thread
{
	private static PeerChess chess = PeerChess.instance();
	
	private Set<ITicker> tickers = new HashSet<ITicker>();
	
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
				if(chess.getFrame() != null)
				{
					if(chess.getFrame().getPanel().chess.isMoving())
					{
						chess.getFrame().getPanel().chess.currentMove.update();
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
