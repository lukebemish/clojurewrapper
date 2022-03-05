package com.github.lukebemish.clojurewrapper.util;

import clojure.lang.IFn;

import java.util.function.Supplier;

public class SupplierIFn implements Supplier {
    private final IFn fn;
    public SupplierIFn(IFn fn) {
        this.fn = fn;
    }

    @Override
    public Object get() {
        return fn.invoke();
    }
}
