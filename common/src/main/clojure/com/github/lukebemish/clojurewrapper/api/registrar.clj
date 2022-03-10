(ns com.github.lukebemish.clojurewrapper.api.registrar
  (:require [com.github.lukebemish.clojurewrapper.api.platform :as platform]))

(defn register [key object_dict]
  (platform/run-platform 'com.github.lukebemish.clojurewrapper.impl.registries/register-objects
                         {key object_dict}))
