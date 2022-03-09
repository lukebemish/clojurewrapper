package com.github.lukebemish.clojurewrapper.util;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

public class RunPlatform {
    public static IFn runPlatform(String combined) {
        String[] parts = combined.split("/");
        if (parts.length == 2) {
            String ns = parts[0];
            String path = parts[1];
            Clojure.var("clojure.core", "require").invoke(Clojure.read(ns));
            return Clojure.var(ns, path);
        }
        return null;
    }
}
