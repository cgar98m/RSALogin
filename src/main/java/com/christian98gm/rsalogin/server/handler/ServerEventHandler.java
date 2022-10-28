package com.christian98gm.rsalogin.server.handler;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.network.NetworkPacketHandler;
import com.christian98gm.rsalogin.network.packet.MessageLoginRequest;
import com.christian98gm.rsalogin.server.RSAHolder;
import com.christian98gm.rsalogin.server.storage.RSALoginStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@OnlyIn(Dist.DEDICATED_SERVER)
@Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = RSALogin.MOD_ID)
public class ServerEventHandler
{
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event)
    {
        //Try to init RSAHolder. Close server if fails
        try {
            RSAHolder.instance();
            RSALogin.LOGGER.info("Key pairs generated successfully");
        } catch (NoSuchAlgorithmException e) {
            RSALogin.LOGGER.error("Required key pairs. Closing server...");
            event.getServer().close();
            return;
        }

        //Try to init RSALoginStorage
        try {
            RSALoginStorage.instance();
            RSALogin.LOGGER.info("Players storage available");
        } catch (IOException e) {
            RSALogin.LOGGER.error("Server could not access to players storage. Closing server...", e);
            event.getServer().close();
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event)
    {
        try {
            RSALoginStorage.instance().storageProvider.save();
            RSALogin.LOGGER.info("Players storage updated");
        } catch(IOException e) {
            RSALogin.LOGGER.error("Server could not update players storage", e);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        //Require the player to login
        //TODO: Prevent player rejoin
        ServerPlayer player = (ServerPlayer) event.getEntity();
        try {
            RSALogin.LOGGER.info("Request " + player.getName().getString() + " login...");
            NetworkPacketHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new MessageLoginRequest(RSAHolder.instance().getEncodedPublicKey()));
        } catch(NoSuchAlgorithmException e) {   //It may not happen: already controlled in 'onServerStarting'
            RSALogin.LOGGER.error("Required key pairs. Closing server...");
            player.server.close();
        }
    }
}
