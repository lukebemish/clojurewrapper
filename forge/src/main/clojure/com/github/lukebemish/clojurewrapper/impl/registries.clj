(ns com.github.lukebemish.clojurewrapper.impl.registries
  (:require [com.github.lukebemish.clojurewrapper.init :as init]
            [com.github.lukebemish.clojurewrapper.api.util.functional :as functional])
  (:import (net.minecraftforge.registries ForgeRegistries IForgeRegistry IForgeRegistryEntry)
           (net.minecraftforge.event RegistryEvent$Register)
           (net.minecraftforge.eventbus.api EventPriority)
           (net.minecraft.resources ResourceLocation)
           (com.github.lukebemish.clojureloader ClojureModLoadingContext)))

(defn register-objects [mapin]
  (doseq [keyval mapin]
    (let [^IForgeRegistry registry
          (case (first keyval)
             :block ForgeRegistries/BLOCKS
             :item ForgeRegistries/ITEMS
             :block-entity ForgeRegistries/BLOCK_ENTITIES)]
      (cond
        (nil? registry) (.error init/logger "Tried to register to unknown registry {}" (str (first keyval)))
        (instance? IForgeRegistry registry) (.addGenericListener
                                              (.getModEventBus (ClojureModLoadingContext/get))
                                              (.getRegistrySuperType registry)
                                              EventPriority/NORMAL
                                              false
                                              RegistryEvent$Register
                                              (functional/consumer
                                                #(let [reg (.getRegistry ^RegistryEvent$Register %)]
                                                   (if (= reg registry)
                                                     (doseq [rlval (second keyval)]
                                                       (.register reg (.setRegistryName ^IForgeRegistryEntry (apply (second rlval) '()) ^ResourceLocation (first rlval))))))))))))