package com.github.lukebemish.clojureloader;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClojureModWrapper implements ModInitializer {
    private final static Logger LOGGER = LogManager.getLogger(ClojureModWrapper.class);
    private IFn funct;
    private final String clojure;

    public ClojureModWrapper(String clojure) {
        this.clojure = clojure;
        String[] parts = clojure.split("/");
        if (parts.length == 2) {
            String ns = parts[0];
            String path = parts[1];
            Clojure.var("clojure.core", "require").invoke(Clojure.read(ns));
            this.funct = Clojure.var(ns,path);
        } else {
            LOGGER.error("Couldn't expand to clojure path: {}. Failing softly during mod load...", clojure);
        }
    }

    @Override
    public void onInitialize() {
        if (this.funct != null) {
            this.funct.invoke();
        } else {
            LOGGER.error("Couldn't expand to clojure path: {}. Failing softly during mod initialization...", this.clojure);
        }
    }
}
