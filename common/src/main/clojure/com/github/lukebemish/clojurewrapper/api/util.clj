(ns com.github.lukebemish.clojurewrapper.api.util
  (:import (net.minecraft.resources ResourceLocation)))

(defn resource-location
  ([names path] (ResourceLocation. names path))
  ([fullname] (ResourceLocation. fullname)))