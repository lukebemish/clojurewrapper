(ns com.github.lukebemish.clojurewrapper.api.util
  (:import (net.minecraft.resources ResourceLocation)
           (com.github.lukebemish.clojurewrapper.util SupplierIFn)))

(defn resource-location
  ([names path] (ResourceLocation. names path))
  ([fullname] (ResourceLocation. fullname)))

(defn supplier [function] (SupplierIFn. function))