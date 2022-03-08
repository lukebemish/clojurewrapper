(ns com.github.lukebemish.clojurewrapper.impl.registries
  (:require [com.github.lukebemish.clojurewrapper.api.platform :as platform])
  (:import (net.minecraft.core Registry)
           (net.minecraft.resources ResourceLocation)
           (java.util.function Supplier)))

(defn register [rname, ^ResourceLocation location, supplier]
  (let [^Registry registry
        (case rname
          :block Registry/BLOCK
          :item Registry/ITEM
          :block-entity Registry/BLOCK_ENTITY_TYPE)]
    (if (not (nil? registry)) (Registry/register registry location (apply supplier '())))))

(defn register-objects [mapin]
  (doseq [keyval mapin]
    (doseq [rlval (second keyval)]
      (register (first keyval) (first rlval) (second rlval)))))