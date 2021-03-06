package com.aidancbrady.peerchess.client;

import com.aidancbrady.peerchess.PeerUtils;

public final class Constants
{
    /** Max depth for AI algorithm */
    public static final int MAX_DEPTH = 5;
    
    /** Max ping in milliseconds for datagram packet pings */
    public static final int MAX_PING = 2000;
    
    public static double[][] pawnEvalWhite = {
            {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
            {5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0,  5.0},
            {1.0,  1.0,  2.0,  3.0,  3.0,  2.0,  1.0,  1.0},
            {0.5,  0.5,  1.0,  2.5,  2.5,  1.0,  0.5,  0.5},
            {0.0,  0.0,  0.0,  2.0,  2.0,  0.0,  0.0,  0.0},
            {0.5, -0.5, -1.0,  0.0,  0.0, -1.0, -0.5,  0.5},
            {0.5,  1.0, 1.0,  -2.0, -2.0,  1.0,  1.0,  0.5},
            {0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0}
        };

        public static double[][] pawnEvalBlack = PeerUtils.reverseArray(pawnEvalWhite);

        public static double[][] knightEval = {
            {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
            {-4.0, -2.0,  0.0,  0.0,  0.0,  0.0, -2.0, -4.0},
            {-3.0,  0.0,  1.0,  1.5,  1.5,  1.0,  0.0, -3.0},
            {-3.0,  0.5,  1.5,  2.0,  2.0,  1.5,  0.5, -3.0},
            {-3.0,  0.0,  1.5,  2.0,  2.0,  1.5,  0.0, -3.0},
            {-3.0,  0.5,  1.0,  1.5,  1.5,  1.0,  0.5, -3.0},
            {-4.0, -2.0,  0.0,  0.5,  0.5,  0.0, -2.0, -4.0},
            {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}
        };

        public static double[][] bishopEvalWhite = {
            {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
            {-1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
            {-1.0,  0.0,  0.5,  1.0,  1.0,  0.5,  0.0, -1.0},
            {-1.0,  0.5,  0.5,  1.0,  1.0,  0.5,  0.5, -1.0},
            {-1.0,  0.0,  1.0,  1.0,  1.0,  1.0,  0.0, -1.0},
            {-1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0, -1.0},
            {-1.0,  0.5,  0.0,  0.0,  0.0,  0.0,  0.5, -1.0},
            {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}
        };

        public static double[][] bishopEvalBlack = PeerUtils.reverseArray(bishopEvalWhite);

        public static double[][] castleEvalWhite = {
            { 0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0},
            { 0.5,  1.0,  1.0,  1.0,  1.0,  1.0,  1.0,  0.5},
            {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            {-0.5,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -0.5},
            { 0.0,   0.0, 0.0,  0.5,  0.5,  0.0,  0.0,  0.0}
        };

        public static double[][] castleEvalBlack = PeerUtils.reverseArray(castleEvalWhite);

        public static double[][] queenEval = {
            {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
            {-1.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0, -1.0},
            {-1.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
            {-0.5,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
            { 0.0,  0.0,  0.5,  0.5,  0.5,  0.5,  0.0, -0.5},
            {-1.0,  0.5,  0.5,  0.5,  0.5,  0.5,  0.0, -1.0},
            {-1.0,  0.0,  0.5,  0.0,  0.0,  0.0,  0.0, -1.0},
            {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}
        };

        public static double[][] kingEvalWhite = {
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
            {-1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
            { 2.0,  2.0,  0.0,  0.0,  0.0,  0.0,  2.0,  2.0},
            { 2.0,  3.0,  1.0,  0.0,  0.0,  1.0,  3.0,  2.0}
        };

        public static double[][] kingEvalBlack = PeerUtils.reverseArray(kingEvalWhite);
}
