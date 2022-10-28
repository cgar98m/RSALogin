package com.christian98gm.rsalogin.command;

import com.christian98gm.rsalogin.RSALogin;
import com.christian98gm.rsalogin.command.arguments.PlayerEntryArgument;
import com.christian98gm.rsalogin.server.storage.RSALStorage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.io.IOException;

public class RSALCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("rsalogin")
                .then(Commands.literal("unregister").requires((c) -> c.hasPermission(4))
                                .then(Commands.argument("playerName", PlayerEntryArgument.playerEntry())
                                        .executes(RSALCommand::unregister)))
                .then(Commands.literal("save").requires((c) -> c.hasPermission(4))
                        .executes(RSALCommand::save))
                .then(Commands.literal("reload").requires((c) -> c.hasPermission(4))
                        .executes(RSALCommand::reload))
                .then(Commands.literal("list").requires((c) -> c.hasPermission(4))
                        .executes(RSALCommand::list));
        dispatcher.register(command);
    }

    private static int unregister(CommandContext<CommandSourceStack> ctx) {
        String playerName = PlayerEntryArgument.getPlayerEntry(ctx, "playerName");
        if(RSALStorage.instance().storageProvider.isRegistered(playerName)) {
            RSALStorage.instance().storageProvider.unregister(playerName);
            RSALogin.LOGGER.info("Player " + playerName + " was successfully unregistered");
            ctx.getSource().sendSuccess(
                    Component.literal("Player " + playerName + " was successfully unregistered"),
                    false);

        } else {
            RSALogin.LOGGER.info("Player " + playerName + " was already unregistered");
            ctx.getSource().sendSuccess(
                    Component.literal("Player " + playerName + " was already unregistered"),
                    false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int save(CommandContext<CommandSourceStack> ctx) {
        try {
            RSALStorage.instance().storageProvider.save();
            RSALogin.LOGGER.info("Players storage updated");
            ctx.getSource().sendSuccess(
                    Component.literal("Players storage updated"),
                    false);
        } catch(IOException e) {
            RSALogin.LOGGER.error("Server could not update players storage", e);
            ctx.getSource().sendFailure(Component.literal("Server could not update players storage"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int reload(CommandContext<CommandSourceStack> ctx) {
        try {
            RSALStorage.instance().storageProvider.reload();
            String playerList = RSALStorage.instance().storageProvider.getAllRegisterNames().toString();
            RSALogin.LOGGER.info("Player list reloaded from storage\n" + playerList);
            ctx.getSource().sendSuccess(
                    Component.literal("Player list reloaded from storage\n" + playerList),
                    false);
        } catch (IOException e) {
            RSALogin.LOGGER.error("Server could not access to players storage. Previous player list will be kept", e);
            ctx.getSource().sendFailure(
                    Component.literal("Server could not access to players storage." +
                            " Previous player list will be kept"));
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int list(CommandContext<CommandSourceStack> ctx) {
        String playerList = RSALStorage.instance().storageProvider.getAllRegisterNames().toString();
        RSALogin.LOGGER.info("Player list: " + playerList);
        ctx.getSource().sendSuccess(Component.literal("Player list: " + playerList), false);
        return Command.SINGLE_SUCCESS;
    }
}
