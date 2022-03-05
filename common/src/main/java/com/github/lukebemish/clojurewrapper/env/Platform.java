package com.github.lukebemish.clojurewrapper.env;

import dev.architectury.injectables.annotations.ExpectPlatform;

public enum Platform {
    FORGE("forge"),
    FABRIC("fabric");

    private final String name;

    @ExpectPlatform
    public static Platform getPlatform() {
        throw new AssertionError();
    }

    public static Object unimplError() {
        throw new AssertionError();
    }

    Platform(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
