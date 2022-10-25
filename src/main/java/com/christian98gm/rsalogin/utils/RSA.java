package com.christian98gm.rsalogin.utils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@OnlyIn(Dist.DEDICATED_SERVER)
public class RSA
{
    private static final String SPEC = "RSA";
    private static final int KEY_SIZE = 4096;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public void keyGen() throws NoSuchAlgorithmException {
        KeyPairGenerator g = KeyPairGenerator.getInstance(SPEC);
        g.initialize(KEY_SIZE, new SecureRandom());
        KeyPair kp = g.generateKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
    }

    public void keyRegen(byte[] pubBytes, byte[] privateBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(SPEC);

        //Regen private key
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateBytes);
        privateKey = keyFactory.generatePrivate(privateKeySpec);

        //Regen public key
        EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubBytes);
        publicKey = keyFactory.generatePublic(pubKeySpec);
    }
}
