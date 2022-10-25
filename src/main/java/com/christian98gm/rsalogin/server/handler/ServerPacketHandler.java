package com.christian98gm.rsalogin.server.handler;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.network.packet.MessageLoginResponse;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerPacketHandler {
    public static class LoginResponseHandler
    {
        public static void handlePacket(MessageLoginResponse msg, Supplier<NetworkEvent.Context> ctx)
        {
            ServerPlayer player = ctx.get().getSender();
            if(player != null) {
                //TODO: Manage user login in server side
                RSALogin.LOGGER.debug("User password: " + msg.getPassword());
            }
        }
    }
}
