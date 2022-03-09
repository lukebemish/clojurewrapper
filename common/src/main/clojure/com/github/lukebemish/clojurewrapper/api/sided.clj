(ns com.github.lukebemish.clojurewrapper.api.sided
  (:require [com.github.lukebemish.clojurewrapper.api.platform :as platform])
  (:import (com.github.lukebemish.clojurewrapper.env Sided)))

(defn client? [] (Sided/isClient))
(defn server? [] (Sided/isServer))

(def run-sided platform/run-platform)