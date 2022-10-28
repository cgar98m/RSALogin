package com.christian98gm.rsalogin.command.arguments;

import com.christian98gm.rsalogin.server.storage.RSALStorage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.forgespi.Environment;

import java.util.concurrent.CompletableFuture;

public class PlayerEntryArgument implements ArgumentType<String> {
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_PLAYER =
            new DynamicCommandExceptionType((o -> Component.literal("Player entry not found")));

    private PlayerEntryArgument()
    {
    }

    public static PlayerEntryArgument playerEntry()
    {
        return new PlayerEntryArgument();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();
        if(Environment.get().getDist() == Dist.DEDICATED_SERVER && !RSALStorage.instance().storageProvider.isRegistered(name)) {
            throw ERROR_UNKNOWN_PLAYER.create(name);
        }
        return name;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if(context.getSource() instanceof CommandSourceStack) {
            return SharedSuggestionProvider.suggest(RSALStorage.instance().storageProvider.getAllRegisterNames(),
                    builder);
        } else if(context.getSource() instanceof ClientSuggestionProvider src) {
            return src.customSuggestion(context);
        }
        return Suggestions.empty();
    }

    public static <T> String getPlayerEntry(CommandContext<T> ctx, String name) {
        return ctx.getArgument(name, String.class);
    }
}
