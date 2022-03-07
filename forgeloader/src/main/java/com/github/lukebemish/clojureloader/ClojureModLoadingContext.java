package com.github.lukebemish.clojureloader;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClojureModLoadingContext {
    private final ClojureModContainer container;

    public ClojureModLoadingContext(ClojureModContainer container) {
        this.container = container;
    }

    public IEventBus getModEventBus()
    {
        return container.eventBus;
    }

    public static FMLJavaModLoadingContext get() {
        return ModLoadingContext.get().extension();
    }
}
