package com.christian98gm.rsalogin.server.handler;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.network.packet.MessageLoginResponse;
import com.christian98gm.rsalogin.server.RSAHolder;
import com.christian98gm.rsalogin.server.storage.RSALoginStorage;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;

public class ServerPacketHandler {
    public static class LoginResponseHandler
    {
        public static void handlePacket(MessageLoginResponse msg, Supplier<NetworkEvent.Context> ctx)
        {
            //Check if is a valid player
            ServerPlayer player = ctx.get().getSender();
            if(player == null) {
                return;
            }

            //Check non-empty content
            if(msg.getEncryptedKey().isEmpty()) {
                RSALogin.LOGGER.error("Player " + player.getName() + " sent no key");
                player.connection.disconnect(
                        Component.literal("Login required - Player key not found"));
            }

            //Decrypt player key
            String playerKey = null;
            try {
                playerKey = RSAHolder.instance().decryptMessage(msg.getEncryptedKey());
                RSALogin.LOGGER.debug("Player " + player.getName() + "'s key: " + playerKey);
            } catch (NoSuchAlgorithmException e) {
                RSALogin.LOGGER.error("Player " + player.getName() + "'s key couldn't be decrypted");
                player.connection.disconnect(
                        Component.literal("Login required - Player's key couldn't be decrypted"));
                return;
            }

            //Check if is a registered user
            if(RSALoginStorage.instance().storageProvider.isRegistered(
                    player.getName().getString(), player.getStringUUID(), playerKey)) {
                RSALogin.LOGGER.info("Player " + player.getName() + " login was successful");
            } else {
                RSALogin.LOGGER.info("Registering player " + player.getName() + "...");
                RSALoginStorage.instance().storageProvider.register(
                        player.getName().getString(), player.getStringUUID(), playerKey);
            }
        }
    }
}
