package com.github.lukebemish.clojurewrapper.env;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class Sided {
    @ExpectPlatform
    public static boolean isServer() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isClient() {
        throw new AssertionError();
    }
}
