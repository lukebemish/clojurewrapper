(ns com.github.lukebemish.clojurewrapper.impl.registries
  (:import (java.util.function Supplier)
           (net.minecraftforge.registries ForgeRegistries IForgeRegistry DeferredRegister)
           (net.minecraft.resources ResourceLocation)
           (com.github.lukebemish.clojureloader ClojureModLoadingContext)))

(defn register-objects [mapin]
  (doseq [keyval mapin]
    (let [namespaces
          (set (map first (seq (second keyval))))
          ^IForgeRegistry registery
          (case (first keyval)
             :block ForgeRegistries/BLOCKS
             :item ForgeRegistries/ITEMS
             :block-entity ForgeRegistries/BLOCK_ENTITIES)
          get-registry (memoize (fn [^ResourceLocation n]
                                  (if (not (nil? registery)) (DeferredRegister/create ^IForgeRegistry registery ^String (. ^ResourceLocation n getNamespace)))))
          registry-set (set (map (fn [x] (get-registry (first x))) (seq (second keyval))))]
      (if (not (nil? registery))
        (do
          (doseq [rlval (second keyval)]
            (. ^DeferredRegister (get-registry (first rlval)) register ^String (. ^ResourceLocation (first rlval) getNamespace) ^Supplier (second rlval)))
          (map (fn [^DeferredRegister x] (. x register (.getModEventBus (ClojureModLoadingContext/get)))) registry-set))))))
