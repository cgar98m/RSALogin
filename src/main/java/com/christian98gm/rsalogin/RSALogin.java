package com.christian98gm.rsalogin;

import com.christian98gm.rsalogin.network.NetworkPacketHandler;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(RSALogin.MOD_ID)
public class RSALogin
{
    public static final String MOD_ID = "rsalogin";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RSALogin()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        NetworkPacketHandler.registerPackets();
    }
}
