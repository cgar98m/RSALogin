package com.christian98gm.rsalogin.network.packet;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.client.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class MessageLoginRequest
{
    private final String publicKey;
    public MessageLoginRequest(String publicKey)
    {
        this.publicKey = publicKey;
    }

    public static void encode(MessageLoginRequest msg, FriendlyByteBuf buf)
    {
        buf.writeInt(msg.publicKey.length());
        buf.writeCharSequence(msg.publicKey, StandardCharsets.UTF_8);
    }

    public static MessageLoginRequest decode(FriendlyByteBuf buf)
    {
        int length = buf.readInt();
        return new MessageLoginRequest(buf.readCharSequence(length, StandardCharsets.UTF_8).toString());
    }

    public static void handle(MessageLoginRequest msg, Supplier<NetworkEvent.Context> ctx)
    {
        RSALogin.LOGGER.debug("MessageLoginRequest received");
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> ClientPacketHandler.LoginRequestHandler.handlePacket(msg, ctx));
        });
        ctx.get().setPacketHandled(true);
    }

    public String getPublicKeyText() {
        return publicKey;
    }
}
