package com.christian98gm.rsalogin.command;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.command.arguments.PlayerEntryArgument;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = RSALogin.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandHandler
{
    public static void commonSetup(FMLCommonSetupEvent event)
    {
        ArgumentTypeInfos.registerByClass(PlayerEntryArgument.class,
                SingletonArgumentInfo.contextFree(PlayerEntryArgument::playerEntry));
    }

    @SubscribeEvent
    public static void onRegisterCommand(RegisterCommandsEvent event)
    {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        RSALCommand.register(dispatcher);
    }
}
