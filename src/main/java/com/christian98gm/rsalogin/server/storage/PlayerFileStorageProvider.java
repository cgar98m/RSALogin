package com.christian98gm.rsalogin.server.storage;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@OnlyIn(Dist.DEDICATED_SERVER)
public class PlayerFileStorageProvider implements PlayerStorageProvider
{
    //TODO
    @Override
    public boolean isRegistered(String name, String uuid, String key) {
        return false;
    }

    @Override
    public void register(String name, String uuid, String key) {

    }

    @Override
    public void save() {

    }
}
