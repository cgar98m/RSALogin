package com.christian98gm.rsalogin.network.packet;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.server.handler.ServerPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class MessageLoginResponse
{
    private final String password;

    public MessageLoginResponse(String password)
    {
        this.password = password;
    }

    public static void encode(MessageLoginResponse msg, FriendlyByteBuf buf)
    {
        buf.writeInt(msg.password.length());
        buf.writeCharSequence(msg.password, StandardCharsets.UTF_8);
    }

    public static MessageLoginResponse decode(FriendlyByteBuf buf)
    {
        int length = buf.readInt();
        return new MessageLoginResponse(buf.readCharSequence(length, StandardCharsets.UTF_8).toString());
    }

    public static void handle(MessageLoginResponse msg, Supplier<NetworkEvent.Context> ctx)
    {
        RSALogin.LOGGER.debug("MessageLoginResponse received");
        ctx.get().enqueueWork(() -> {
            ServerPacketHandler.LoginResponseHandler.handlePacket(msg, ctx);
        });
        ctx.get().setPacketHandled(true);
    }

    public String getPassword()
    {
        return password;
    }
}
