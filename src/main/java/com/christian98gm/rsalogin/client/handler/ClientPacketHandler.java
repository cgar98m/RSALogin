package com.christian98gm.rsalogin.client.handler;

import com.christian98gm.rsalogin.client.KeyHolder;
import com.christian98gm.rsalogin.network.NetworkPacketHandler;
import com.christian98gm.rsalogin.network.packet.MessageLoginRequest;
import com.christian98gm.rsalogin.network.packet.MessageLoginResponse;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler
{
    public static class LoginRequestHandler
    {
        public static void handlePacket(MessageLoginRequest msg, Supplier<NetworkEvent.Context> ctx)
        {
            String key = KeyHolder.instance().getKey();
            String encodedKey = key;    //TODO: Do key encoding
            NetworkPacketHandler.INSTANCE.sendToServer(new MessageLoginResponse(encodedKey));
        }
    }
}
