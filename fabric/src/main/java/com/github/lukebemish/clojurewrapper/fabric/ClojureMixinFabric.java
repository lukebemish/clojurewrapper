package com.github.lukebemish.clojurewrapper.fabric;

import com.github.lukebemish.clojurewrapper.ClojureWrapper;
import net.fabricmc.api.ModInitializer;

public class ClojureWrapperFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ClojureWrapper.init();
    }
}
