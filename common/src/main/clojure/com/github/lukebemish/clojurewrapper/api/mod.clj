(ns com.github.lukebemish.clojurewrapper.api.mod
  (:require [com.github.lukebemish.clojurewrapper.api.sided :as sided]
            [com.github.lukebemish.clojurewrapper.api.platform :as platform]))

(defn mod-load [mod-vals]
  (do
    (if (not (nil? (mod-vals :before-registries)))
      (apply (mod-vals :before-registries) '()))
    (if (not (nil? (mod-vals :registries)))
      (platform/run-platform 'com.github.lukebemish.clojurewrapper.impl.registries/register-objects
                             (mod-vals :registries)))
    (if (not (nil? (mod-vals :main)))
      (apply (mod-vals :main) '()))
    (if (and (sided/client?) (not (nil? (mod-vals :client))))
      (apply (mod-vals :client) '()))
    (if (and (sided/server?) (not (nil? (mod-vals :server))))
      (apply (mod-vals :server) '()))))