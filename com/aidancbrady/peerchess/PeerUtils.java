package com.aidancbrady.peerchess;

public final class PeerUtils
{
	public static boolean isValidIP(String s)
	{
		if(s == null || s.isEmpty())
		{
			return false;
		}
		
		s = s.trim().replace(".", ":");
		
		if(s.equals("localhost"))
		{
			return true;
		}
		
		String[] split = s.split(":");
		
		if(split.length != 4)
		{
			return false;
		}
		
		for(String b : split)
		{
			if(!isInteger(b))
			{
				return false;
			}
			
			int i = Integer.parseInt(b);
			
			if(i < 0 || i > 255)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isInteger(String s)
	{
		if(s == null || s.isEmpty())
		{
			return false;
		}
		
		try {
			Integer.parseInt(s);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}
