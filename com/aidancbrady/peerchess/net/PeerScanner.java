package com.aidancbrady.peerchess.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.aidancbrady.peerchess.PeerChess;

public class PeerScanner extends Thread
{
	public static final int SCAN_TOTAL = 256*256;
	
	public List<String> openServers = new ArrayList<String>();
	
	public boolean finished;
	
	public int scanned = 0;
	
	public PeerScanner()
	{
		setDaemon(true);
	}
	
	@Override
	public void run()
	{
		Socket s;
		
		try {
			String IP2 = getIP2();
			
			for(int ip3 = 0; ip3 < 256; ip3++)
			{
				for(int ip4 = 0; ip4 < 256; ip4++)
				{
					String ip = IP2 + "." + ip3 + "." + ip4;
					
					s = new Socket();
					scanned++;
					
					try {
						s.connect(new InetSocketAddress(ip, PeerChess.instance().port), 10);
						openServers.add(ip);
						s.close();
					} catch (Exception e) {}
				}
			}
			
			finished = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getIP2() throws IOException
	{
		String ip = InetAddress.getLocalHost().getHostAddress().replace(".", ":");
		return ip.split(":")[0] + "." + ip.split(":")[1];
	}
}
