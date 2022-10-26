package com.christian98gm.rsalogin.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    private static final String SPEC = "SHA-256";

    public static String hash(String message) throws NoSuchAlgorithmException {
        //Hash message
        MessageDigest messageDigest = MessageDigest.getInstance(SPEC);
        byte[] hash = messageDigest.digest(message.getBytes(StandardCharsets.UTF_8));

        //Convert to string
        final StringBuilder hexSb = new StringBuilder();
        for(byte b : hash) {
            final String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexSb.append('0');
            hexSb.append(hex);
        }
        return hexSb.toString();
    }
}
