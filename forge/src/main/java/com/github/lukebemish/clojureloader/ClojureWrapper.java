package com.github.lukebemish.clojureloader;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClojureWrapper {
    private final static Logger LOGGER = LogManager.getLogger(ClojureWrapper.class);
    public ClojureWrapper(String clojure) {
        String[] parts = clojure.split("/");
        if (parts.length == 2) {
            String ns = parts[0];
            String path = parts[1];
            IFn require = Clojure.var("clojure.core", "require");
            require.invoke(Clojure.read(ns));
            IFn funct = Clojure.var(ns,path);
            funct.invoke();
        } else {
            LOGGER.error("Couldn't expand to clojure path: {}", clojure);
        }
    }
}
