package com.github.lukebemish.clojuremixin.fabric;

import com.github.lukebemish.clojuremixin.ClojureMixin;
import net.fabricmc.api.ModInitializer;

public class ClojureMixinFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ClojureMixin.init();
    }
}
