package com.christian98gm.rsalogin.server.handler;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.network.NetworkPacketHandler;
import com.christian98gm.rsalogin.network.packet.MessageLoginRequest;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@OnlyIn(Dist.DEDICATED_SERVER)
@Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = RSALogin.MOD_ID)
public class ServerEventHandler
{
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        NetworkPacketHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                new MessageLoginRequest());
    }
}
