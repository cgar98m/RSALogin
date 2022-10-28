package com.christian98gm.rsalogin.server.handler;

import com.christian98gm.rsalogin.RSALogin;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerNegotiationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@OnlyIn(Dist.DEDICATED_SERVER)
@Mod.EventBusSubscriber(value = Dist.DEDICATED_SERVER, modid = RSALogin.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerForgeEventHandler {
    @SubscribeEvent
    public static void onForgeHandshake(PlayerNegotiationEvent event)
    {
        //Get server instance and player info
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        String playerName = event.getProfile().getName();

        //Check if player is already connected
        PlayerList playerList = server.getPlayerList();
        RSALogin.LOGGER.debug("Connected players: " + playerList.getPlayers());
        for(ServerPlayer p : playerList.getPlayers()) {
            if(p.getName().getString().equals(playerName)) {
                //Denny player login
                RSALogin.LOGGER.debug("Player " + playerName + " already connected");
                event.getConnection().disconnect(
                        Component.literal("Player " + playerName + " is already connected"));
                return;
            }
        }

        //Player not found: valid login
        RSALogin.LOGGER.debug("Player " + playerName + " can try server join");
    }
}
