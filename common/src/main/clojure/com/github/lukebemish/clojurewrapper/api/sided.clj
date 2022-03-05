(ns com.github.lukebemish.clojurewrapper.api.sided
  (:import (com.github.lukebemish.clojurewrapper.env Sided)))

(defn client? [] (Sided/isClient))
(defn server? [] (Sided/isServer))