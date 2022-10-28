package com.christian98gm.rsalogin.server;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.network.NetworkPacketHandler;
import com.christian98gm.rsalogin.network.packet.MessageLoginRequest;
import com.christian98gm.rsalogin.server.RSAHolder;
import com.christian98gm.rsalogin.server.storage.RSALStorage;
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
        //Try to init RSAHolder and RSALStorage. Close server if fails
        try {
            RSAHolder.initialize();
            RSALogin.LOGGER.info("Key pairs generated successfully");
            RSALStorage.initialize();
            RSALogin.LOGGER.info("Players storage available");
        } catch (NoSuchAlgorithmException e) {
            RSALogin.LOGGER.error("Required key pairs. Closing server...");
            event.getServer().close();
        } catch (IOException e) {
            RSALogin.LOGGER.error("Server could not access to players storage. Closing server...", e);
            event.getServer().close();
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event)
    {
        try {
            RSALStorage.instance().storageProvider.save();
            RSALogin.LOGGER.info("Players storage updated");
        } catch(IOException e) {
            RSALogin.LOGGER.error("Server could not update players storage", e);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        //Require the player to login
        ServerPlayer player = (ServerPlayer) event.getEntity();
        RSALogin.LOGGER.info("Request " + player.getName().getString() + " login...");
        NetworkPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                new MessageLoginRequest(RSAHolder.instance().getEncodedPublicKey()));

    }
}
