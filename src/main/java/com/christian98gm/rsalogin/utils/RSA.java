package com.christian98gm.rsalogin.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA
{
    private static final String SPEC = "RSA";
    private static final int KEY_SIZE = 4096;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public RSA()
    {
        publicKey = null;
        privateKey = null;
    }

    public void keyGen() throws NoSuchAlgorithmException {
        //Try key pair generation
        KeyPairGenerator g = KeyPairGenerator.getInstance(SPEC);
        g.initialize(KEY_SIZE, new SecureRandom());
        KeyPair kp = g.generateKeyPair();

        //Assign key pairs
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
    }

    public void publicKeyRegen(String base64Key)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Base64.getDecoder().decode(base64Key);
        KeyFactory keyFactory = KeyFactory.getInstance(SPEC);
        EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(bytes);
        publicKey = keyFactory.generatePublic(pubKeySpec);
    }

    public String getEncodedPublicKey()
    {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String encryptMessage(String message) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(SPEC);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedMessage = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedMessage);
    }

    public String decryptMessage(String encryptedMessage) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(RSA.SPEC);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decryptedMessage, StandardCharsets.UTF_8);
    }
}
