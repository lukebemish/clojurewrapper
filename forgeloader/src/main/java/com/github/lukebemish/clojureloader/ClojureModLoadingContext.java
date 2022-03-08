package com.github.lukebemish.clojureloader;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;

public class ClojureModLoadingContext {
    private final ClojureModContainer container;

    public ClojureModLoadingContext(ClojureModContainer container) {
        this.container = container;
    }

    public IEventBus getModEventBus()
    {
        return container.getEventBus();
    }

    public static ClojureModLoadingContext get() {
        return ModLoadingContext.get().extension();
    }
}
