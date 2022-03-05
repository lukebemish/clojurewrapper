(ns com.github.lukebemish.clojurewrapper.impl.registries
  (:import (java.util.function Supplier)
           (net.minecraftforge.registries ForgeRegistries IForgeRegistry DeferredRegister)
           (net.minecraft.resources ResourceLocation)))

(defn register-objects [mapin]
  (doseq [keyval mapin]
    (let [namespaces
          (set (map first (seq (second keyval))))
          ^IForgeRegistry registery
          (case (first keyval)
             :block ForgeRegistries/BLOCKS
             :item ForgeRegistries/ITEMS
             :block-entity ForgeRegistries/BLOCK_ENTITIES)
          registry-map
          (if (not (nil? registery)) (into {} (map (fn [n] [n (DeferredRegister/create ^IForgeRegistry registery ^String (. ^ResourceLocation n getNamespace))]) namespaces)))]
      (if (not (nil? registry-map))
        (doseq [rlval (second keyval)]
          (. ^DeferredRegister (get registry-map (. ^ResourceLocation (first rlval) getNamespace)) register ^String (. ^ResourceLocation (first rlval) getNamespace) ^Supplier (second rlval)))))))
