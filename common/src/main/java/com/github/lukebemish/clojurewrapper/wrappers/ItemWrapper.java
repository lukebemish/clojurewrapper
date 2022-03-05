package com.github.lukebemish.clojurewrapper.wrappers;

import clojure.lang.IFn;
import net.minecraft.world.item.Item;

public class ItemWrapper extends Item {
    private final IFn functGetter;
    public ItemWrapper(IFn getter) {
        super(new Properties());
        this.functGetter = getter;
    }
}
