(ns com.github.lukebemish.clojurewrapper.init
  (:import (org.apache.logging.log4j Logger LogManager)))

(def ^Logger logger (LogManager/getLogger "clojurewrapper"))

(defn init [] (. logger info "ClojureWrapper API is present!"))