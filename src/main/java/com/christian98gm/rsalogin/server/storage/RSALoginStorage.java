package com.christian98gm.rsalogin.server.storage;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

@OnlyIn(Dist.DEDICATED_SERVER)
public class RSALoginStorage {
    private static RSALoginStorage INSTANCE;

    public static RSALoginStorage instance() throws IOException {
        if(INSTANCE == null) {
            INSTANCE = new RSALoginStorage();
        }
        return INSTANCE;
    }

    public final PlayerStorageProvider storageProvider;

    private RSALoginStorage() throws IOException {
        storageProvider = new PlayerFileStorageProvider();  //Future work?
    }
}
