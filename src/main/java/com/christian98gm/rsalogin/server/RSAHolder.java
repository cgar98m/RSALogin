package com.christian98gm.rsalogin.server;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.utils.RSA;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@OnlyIn(Dist.DEDICATED_SERVER)
public class RSAHolder {
    private static RSAHolder INSTANCE;

    public static RSAHolder instance() {
        return INSTANCE;
    }

    public static void initialize() throws NoSuchAlgorithmException
    {
        INSTANCE = new RSAHolder();
    }

    private RSA rsa;

    private RSAHolder() throws NoSuchAlgorithmException {
        rsa = new RSA();
        try {
            rsa.keyGen();
        } catch(NoSuchAlgorithmException e) {
            RSALogin.LOGGER.error("Failed to generate key pairs", e);
            throw e;
        }
    }

    public String getEncodedPublicKey()
    {
        return rsa.getEncodedPublicKey();
    }

    public String decryptMessage(String encryptedMessage)
    {
        try {
            return rsa.decryptMessage(encryptedMessage);
        } catch (NoSuchPaddingException | IllegalBlockSizeException
                 | NoSuchAlgorithmException | BadPaddingException
                 | InvalidKeyException e) {
            RSALogin.LOGGER.error("Failed to decrypt user key", e);
            throw new RuntimeException(e);
        }
    }
}
