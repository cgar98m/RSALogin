package com.christian98gm.rsalogin.server.handler;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.network.NetworkPacketHandler;
import com.christian98gm.rsalogin.network.packet.MessageLoginRequest;
import com.christian98gm.rsalogin.server.RSAHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.security.NoSuchAlgorithmException;

@OnlyIn(Dist.DEDICATED_SERVER)
@Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = RSALogin.MOD_ID)
public class ServerEventHandler
{
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        //Try to init RSAHolder. Close server if fails
        try {
            RSAHolder.instance();
            RSALogin.LOGGER.info("Key pairs generated successfully");
        } catch (NoSuchAlgorithmException e) {
            RSALogin.LOGGER.error("Required key pairs. Closing server...");
            event.getServer().close();
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        //Require the player to login
        ServerPlayer player = (ServerPlayer) event.getEntity();
        try {
            RSALogin.LOGGER.info("Request " + player.getName() + " login...");
            NetworkPacketHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new MessageLoginRequest(RSAHolder.instance().getEncodedPublicKey()));
        } catch(NoSuchAlgorithmException e) {   //It may not happen: already controlled in 'onServerStarting'
            RSALogin.LOGGER.error("Required key pairs. Closing server...");
            player.server.close();
        }
    }
}
