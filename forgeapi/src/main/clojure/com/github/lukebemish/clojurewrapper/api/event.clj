(ns com.github.lukebemish.clojurewrapper.api.event
  (:require [com.github.lukebemish.clojurewrapper.api.util.functional :as functional])
  (:import (net.minecraftforge.fml.event IModBusEvent)
           (net.minecraftforge.eventbus.api IGenericEvent EventPriority)
           (com.github.lukebemish.clojureloader ClojureModLoadingContext)
           (net.minecraftforge.common MinecraftForge)))

(defmacro listen [event function & {:keys [type priority cancelled] :or {priority EventPriority/NORMAL cancelled false}}]
  (let [evclass (eval event)]
    (if (isa? evclass IModBusEvent)
      (if (isa? evclass IGenericEvent)
         `(.addGenericListener
            (.getModEventBus (ClojureModLoadingContext/get))
            ~(eval type)
            ~priority
            ~cancelled
            ~evclass
            ~(functional/consumer function))
         `(.addListener
            (.getModEventBus (ClojureModLoadingContext/get))
            ~priority
            ~cancelled
            ~evclass
            ~(functional/consumer function)))
      (if (isa? evclass IGenericEvent)
        `(.addGenericListener
           MinecraftForge/EVENT_BUS
           ~(eval type)
           ~priority
           ~cancelled
           ~evclass
           ~(functional/consumer function))
        `(.addListener
           MinecraftForge/EVENT_BUS
           ~priority
           ~cancelled
           ~evclass
           ~(functional/consumer function))))))
