package com.christian98gm.rsalogin.server;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.network.packet.MessageLoginResponse;
import com.christian98gm.rsalogin.server.RSAHolder;
import com.christian98gm.rsalogin.server.storage.RSALStorage;
import com.christian98gm.rsalogin.utils.SHA256;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.io.IOException;
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
                RSALogin.LOGGER.warn("Received unexpected " + MessageLoginResponse.class.getName() + " packet");
                return;
            }

            //Check non-empty content
            String playerName = player.getName().getString();
            if(msg.getEncryptedKey().isEmpty()) {
                RSALogin.LOGGER.error("Player " + playerName + " sent no key");
                player.connection.disconnect(
                        Component.literal("Login required - Player key not found"));
            }

            //Decrypt player key
            String playerKey  = RSAHolder.instance().decryptMessage(msg.getEncryptedKey());
                RSALogin.LOGGER.debug("Player " + playerName + "'s key: " + playerKey);

            //Hash key
            String hashedKey = null;
            try {
                hashedKey = SHA256.hash(playerKey);
                RSALogin.LOGGER.debug("Player " + playerName + "'s hashed key: " + hashedKey);
            } catch (NoSuchAlgorithmException e) {
                RSALogin.LOGGER.error("Player " + playerName + "'s key couldn't be hashed");
                player.connection.disconnect(
                        Component.literal("Login required - Player's key couldn't be hashed"));
                return;
            }

            //Check if is a registered user
            if(!RSALStorage.instance().storageProvider.isRegistered(playerName)) {
                RSALogin.LOGGER.info("Registering player " + playerName + "...");
                RSALStorage.instance().storageProvider.register(
                        playerName, player.getStringUUID(), hashedKey);
                RSALogin.LOGGER.info("Player " + playerName + " register was successful");
            } else {
                if(RSALStorage.instance().storageProvider.isMatch(player.getName().getString(),
                        player.getStringUUID(), hashedKey)) {
                    RSALogin.LOGGER.info("Player " + playerName + " login was successful");
                } else {
                    RSALogin.LOGGER.error("Player " + playerName + "'s key is invalid");
                    player.connection.disconnect(
                            Component.literal("Login required - Invalid player's key"));
                }
            }
        }
    }
}
