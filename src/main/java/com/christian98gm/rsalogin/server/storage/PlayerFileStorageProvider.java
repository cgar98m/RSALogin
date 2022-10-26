package com.christian98gm.rsalogin.server.storage;

import com.google.gson.Gson;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
    private final Map<String, RSALoginPlayer> players;

    public PlayerFileStorageProvider() throws IOException {
        gson = new Gson();
        this.players = new HashMap<>();
        if(Files.exists(PLAYERS_PATH)) {
            RSALoginPlayer[] players = gson.fromJson(
                    Files.newBufferedReader(PLAYERS_PATH, StandardCharsets.UTF_8),
                    RSALoginPlayer[].class);
            if(players != null) {
                for(RSALoginPlayer p : players) {
                    this.players.put(p.name, p);
                }
            }
        } else {
            Files.createFile(PLAYERS_PATH);
        }
    }

    @Override
    public boolean isRegistered(String name) {
        return players.containsKey(name);
    }

    @Override
    public boolean isMatch(String name, String uuid, String key) {
        return isRegistered(name) && players.get(name).equals(new RSALoginPlayer(name, uuid, key));
    }

    @Override
    public void register(String name, String uuid, String key) {
        players.put(name, new RSALoginPlayer(name, uuid, key));
    }

    @Override
    public void save() throws IOException {
        try {
            Files.write(PLAYERS_PATH, gson.toJson(players.values().toArray()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch(IOException e) {
            throw e;
        }
    }
}
