package com.christian98gm.rsalogin.server.storage;

import com.christian98gm.rsalogin.server.storage.provider.PlayerFileStorageProvider;
import com.christian98gm.rsalogin.server.storage.provider.PlayerStorageProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

@OnlyIn(Dist.DEDICATED_SERVER)
public class RSALStorage {
    private static RSALStorage INSTANCE;
    public static RSALStorage instance() {
        return INSTANCE;
    }

    public static void initialize() throws IOException
    {
        if(INSTANCE == null) {
            INSTANCE = new RSALStorage();
        }
    }

    public final PlayerStorageProvider storageProvider;

    private RSALStorage() throws IOException {
        storageProvider = new PlayerFileStorageProvider();  //Future work?
    }
}
