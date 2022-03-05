package com.github.lukebemish.clojureloader;

import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.api.LanguageAdapterException;
import net.fabricmc.loader.api.ModContainer;

public class ClojureLanguageAdapter implements LanguageAdapter {
    @Override
    public <T> T create(ModContainer mod, String value, Class<T> type) throws LanguageAdapterException {
        if (type.isAssignableFrom(ClojureModWrapper.class)) {
            return type.cast(new ClojureModWrapper(value));
        }
        throw new LanguageAdapterException("Non-compatible entrypoint found for "+value+"!");
    }
}
