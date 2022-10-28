package com.christian98gm.rsalogin.server.storage.provider;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.util.Collection;

@ThreadSafe
@OnlyIn(Dist.DEDICATED_SERVER)
public interface PlayerStorageProvider {
    boolean isRegistered(String name);
    boolean isMatch(String name, String uuid, String key);
    void register(String name, String uuid, String key);
    Collection<String> getAllRegisterNames();
    void unregister(String name);
    void save() throws IOException;
    void reload() throws IOException;
}
