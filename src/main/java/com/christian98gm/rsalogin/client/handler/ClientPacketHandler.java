package com.christian98gm.rsalogin.client.handler;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.client.KeyHolder;
import com.christian98gm.rsalogin.network.NetworkPacketHandler;
import com.christian98gm.rsalogin.network.packet.MessageLoginRequest;
import com.christian98gm.rsalogin.network.packet.MessageLoginResponse;
import com.christian98gm.rsalogin.server.RSAHolder;
import com.christian98gm.rsalogin.utils.RSA;
import net.minecraftforge.network.NetworkEvent;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.function.Supplier;

public class ClientPacketHandler
{
    public static class LoginRequestHandler
    {
        public static void handlePacket(MessageLoginRequest msg, Supplier<NetworkEvent.Context> ctx)
        {
            try {
                //Get server public key
                RSA rsa = new RSA();
                rsa.publicKeyRegen(msg.getPublicKeyText());

                //Get personal key
                try {
                    String key = KeyHolder.instance().getKey();
                    //TODO: Apply transform to key

                    //Encrypt personal key and send it
                    try {
                        String encryptedKey = rsa.encryptMessage(key);
                        NetworkPacketHandler.INSTANCE.sendToServer(new MessageLoginResponse(encryptedKey));
                    } catch (NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                        //Send empty message as error identifier
                        RSALogin.LOGGER.error("Client couldn't encrypt key", e);
                        NetworkPacketHandler.INSTANCE.sendToServer(new MessageLoginResponse(""));
                    }
                } catch (IOException e) {
                    //Send empty message as error identifier
                    RSALogin.LOGGER.error("Client couldn't has no key", e);
                    NetworkPacketHandler.INSTANCE.sendToServer(new MessageLoginResponse(""));
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                //Send empty message as error identifier
                RSALogin.LOGGER.error("Client couldn't obtain server's public key", e);
                NetworkPacketHandler.INSTANCE.sendToServer(new MessageLoginResponse(""));
            }
        }
    }
}
