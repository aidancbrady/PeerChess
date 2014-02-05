package com.aidancbrady.openchess;

import javax.swing.SwingUtilities;

public class PeerChess
{
	 static ChessFrame jFrameWindow;
	 
	public static void main(String[] args)
	{
        Runnable doSwingLater = new Runnable(){
            
            @Override
            public void run() {
                jFrameWindow = new ChessFrame();
                jFrameWindow.setVisible(true);
            }
        };
         
        SwingUtilities.invokeLater(doSwingLater);
	}
}
