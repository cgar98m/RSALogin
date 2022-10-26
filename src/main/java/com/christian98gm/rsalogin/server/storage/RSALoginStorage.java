package com.christian98gm.rsalogin.server.storage;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.DEDICATED_SERVER)
public class RSALoginStorage {
    private static RSALoginStorage INSTANCE;

    public static RSALoginStorage instance()
    {
        if(INSTANCE == null) {
            INSTANCE = new RSALoginStorage();
        }
        return INSTANCE;
    }

    public final PlayerStorageProvider storageProvider;

    private RSALoginStorage()
    {
        storageProvider = new PlayerFileStorageProvider();  //Future work?
    }
}
