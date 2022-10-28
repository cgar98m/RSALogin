package com.christian98gm.rsalogin.server.storage.provider;

import com.christian98gm.rsalogin.server.storage.RSALPlayer;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ThreadSafe
@OnlyIn(Dist.DEDICATED_SERVER)
public class PlayerFileStorageProvider implements PlayerStorageProvider
{
    private static final String BASE_PATH = ".";
    private static final String PLAYERS_FILENAME = "rsalogin_players.json";
    private static final Path PLAYERS_PATH = Paths.get(BASE_PATH, PLAYERS_FILENAME);

    private final Gson gson;
    private final Map<String, RSALPlayer> players;

    public PlayerFileStorageProvider() throws IOException {
        gson = new GsonBuilder().setPrettyPrinting().create();
        players = new HashMap<>();
        players.putAll(readStorage());
    }

    private Map<String, RSALPlayer> readStorage() throws IOException
    {
        //Check if player storage exists
        Map<String, RSALPlayer> container = new HashMap<>();
        if(Files.exists(PLAYERS_PATH)) {
            RSALPlayer[] players = gson.fromJson(
                    Files.newBufferedReader(PLAYERS_PATH, StandardCharsets.UTF_8),
                    RSALPlayer[].class);
            if(players != null) {
                for(RSALPlayer p : players) {
                    container.put(p.name, p);
                }
            }
        } else {
            Files.createFile(PLAYERS_PATH);
        }
        return container;
    }

    @Override
    public synchronized boolean isRegistered(String name)
    {
        return players.containsKey(name);
    }

    @Override
    public synchronized boolean isMatch(String name, String uuid, String key)
    {
        return isRegistered(name) && players.get(name).equals(new RSALPlayer(name, uuid, key));
    }

    @Override
    public synchronized void register(String name, String uuid, String key)
    {
        players.put(name, new RSALPlayer(name, uuid, key));
    }

    @Override
    public synchronized Collection<String> getAllRegisterNames()
    {
        return new ImmutableList.Builder<String>().addAll(players.keySet()).build();
    }

    @Override
    public synchronized void unregister(String name)
    {
        if(isRegistered(name)) {
            players.remove(name);
        }
    }

    @Override
    public synchronized void save() throws IOException
    {
        Files.write(PLAYERS_PATH, gson.toJson(players.values().toArray()).getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public synchronized void reload() throws IOException {
        Map<String, RSALPlayer> newList = readStorage();
        players.clear();
        players.putAll(newList);
    }
}
