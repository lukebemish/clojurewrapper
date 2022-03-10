package com.github.lukebemish.clojurewrapper.env.forge;

import net.minecraftforge.fml.loading.FMLEnvironment;

public class SidedImpl {
    public static boolean isClient() {
        return FMLEnvironment.dist.isClient();
    }

    public static boolean isServer() {
        return FMLEnvironment.dist.isDedicatedServer();
    }
}
