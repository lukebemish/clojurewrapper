(ns com.github.lukebemish.clojurewrapper.api.platform
  (:import (com.github.lukebemish.clojurewrapper.platform Platform)))

(def forge Platform/FORGE)
(def fabric Platform/FABRIC)

(defn get-platform [] (Platform/getPlatform))
(defn unimpl-error [] (Platform/unimplError))

(defn fabric? [] (= (get-platform) fabric))
(defn forge? [] (= (get-platform) forge))