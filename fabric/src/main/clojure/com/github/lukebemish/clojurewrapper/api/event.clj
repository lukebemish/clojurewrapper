(ns com.github.lukebemish.clojurewrapper.api.event
  (:require [com.github.lukebemish.clojurewrapper.api.util.functional :as functional])
  (:import (net.fabricmc.fabric.api.event Event)
           (net.minecraft.resources ResourceLocation)))

(defmacro listen [event callback function & {:keys [phase] :or {phase `Event/DEFAULT_PHASE}}]
  `(.register ^Event ~event ^ResourceLocation ~phase (functional/functional ~function ~callback)))