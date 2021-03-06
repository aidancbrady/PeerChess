package com.aidancbrady.peerchess.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;

import com.aidancbrady.peerchess.PeerUtils;

public class PeerEncryptor 
{
    private KeyPairGenerator keyGen;
    private SecureRandom random;
    
    private PublicKey publicKey;
    private PrivateKey privateKey;
    
    private byte[] secretKey;
    
    public boolean init(PeerConnection connection)
    {
        try {
            random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen = KeyPairGenerator.getInstance("DH");
            keyGen.initialize(1024, random);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        
        KeyPair pair = keyGen.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
        
        return sendPublicKey(connection);
    }
    
    public boolean parseHandshake(PeerConnection connection)
    {
        try {
            DataInputStream in = new DataInputStream(connection.getSocket().getInputStream());
            byte[] bytes = new byte[in.readInt()];
            in.readFully(bytes);
            
            return receiveKey(bytes);
        } catch(Exception e) {
            return false;
        }
    }
    
    public boolean receiveKey(byte[] keyBytes)
    {
        try {
            PublicKey otherKey = KeyFactory.getInstance("DH").generatePublic(new X509EncodedKeySpec(keyBytes));
            
            KeyAgreement agreement = KeyAgreement.getInstance("DH");
            agreement.init(privateKey);
            agreement.doPhase(otherKey, true);
            
            byte[] key = agreement.generateSecret();
            byte[] finalKey = new byte[8];
            System.arraycopy(key, 0, finalKey, 0, finalKey.length);
            secretKey = finalKey;
            
            PeerUtils.debug("Completed security handshake process.");
            
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
        
        return Base64.getEncoder().encodeToString(cipher.doFinal(s.getBytes()));
    }
    
    public String decrypt(String s) throws Exception
    {
        SecretKeySpec spec = new SecretKeySpec(secretKey, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, spec);
        
        return new String(cipher.doFinal(Base64.getDecoder().decode(s)));
    }
    
    public boolean sendPublicKey(PeerConnection connection)
    {
        byte[] encoded = publicKey.getEncoded();
        
        try {
            DataOutputStream out = new DataOutputStream(connection.getSocket().getOutputStream());
            out.writeInt(encoded.length);
            out.write(encoded);
            out.flush();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
