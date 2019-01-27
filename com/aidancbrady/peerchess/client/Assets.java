package com.aidancbrady.peerchess.client;

import com.aidancbrady.peerchess.sound.Sound;
import com.aidancbrady.peerchess.tex.Texture;

public final class Assets
{
    public static Texture white = Texture.load("resources/icon/white.png");
    public static Texture black = Texture.load("resources/icon/black.png");
    
    public static Texture select = Texture.load("resources/icon/select.png");
    public static Texture possible = Texture.load("resources/icon/possible.png");
    public static Texture hint = Texture.load("resources/icon/hint.png");
    public static Texture check = Texture.load("resources/icon/check.png");
    
    public static final Sound moveSound = new Sound("resources/sound/move.wav");
}
