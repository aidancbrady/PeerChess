package com.aidancbrady.peerchess.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.aidancbrady.peerchess.PeerChess;

public class PeerScanner extends Thread
{
	public static final int SCAN_TOTAL = 256*256;
	
	public static final int MAX_PING = 1000;
	
	public List<String> openServers = new ArrayList<String>();
	public List<Server> pingedServers = new ArrayList<Server>();
	
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
	
	public class ThreadPing extends Thread
	{
		public String ip;
		
		public Socket socket = new Socket();
		
		public int pingMillis;
		
		public boolean invalid = false;
		
		public ThreadPing(String s)
		{
			ip = s;
		}
		
		@Override
		public void run()
		{
			PeerChess.instance().timer.pings.add(this);
			
			try {
				InetAddress address = InetAddress.getByName(ip);
				socket.connect(new InetSocketAddress(address, PeerChess.instance().port), 10);
				
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
				
				printWriter.println("PING:" + PeerChess.instance().username);
				
				String username = bufferedReader.readLine();
				
				if(username != null)
				{
					pingedServers.add(new Server(ip, username.trim()));
				}
			} catch(Exception e) {
				invalid = true;
			}
		}
	}
	
	public static class Server
	{
		public String username;
		public String ip;
		
		public Server(String s, String s1)
		{
			username = s;
			ip = s1;
		}
	}
}
