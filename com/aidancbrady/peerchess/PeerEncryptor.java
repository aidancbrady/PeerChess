package com.aidancbrady.peerchess;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;

import com.aidancbrady.peerchess.net.PeerConnection;

public class PeerEncryptor 
{
    private KeyPairGenerator keyGen;
    private SecureRandom random;
    
    private PublicKey publicKey;
    private PrivateKey privateKey;
    
    private byte[] secretKey;
    
    public boolean init()
    {
        try {
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            keyGen.initialize(1024, random);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        
        KeyPair pair = keyGen.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
        
        return true;
    }
    
    public boolean receiveKey(String textKey)
    {
        try {
            PublicKey otherKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(textKey.getBytes()));
            
            KeyAgreement agreement = KeyAgreement.getInstance("DH");
            agreement.init(privateKey);
            agreement.doPhase(otherKey, true);
            
            byte[] key = agreement.generateSecret();
            byte[] finalKey = new byte[8];
            System.arraycopy(key, 0, finalKey, 0, finalKey.length);
            secretKey = finalKey;
            
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public String encrypt(String s) throws Exception
    {
        SecretKeySpec spec = new SecretKeySpec(secretKey, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, spec);
        
        return new String(cipher.doFinal(s.getBytes()));
    }
    
    public String decrypt(String s) throws Exception
    {
        SecretKeySpec spec = new SecretKeySpec(secretKey, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, spec);
        
        return new String(cipher.doFinal(s.getBytes()));
    }
    
    public void sendPublicKey(PeerConnection connection)
    {
        connection.write("HANDSHAKE:" + new String(publicKey.getEncoded()));
    }
}
