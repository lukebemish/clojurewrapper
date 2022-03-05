(ns com.github.lukebemish.clojurewrapper.api.platform
  (:require [clojure.string :as string])
  (:import (com.github.lukebemish.clojurewrapper.env Platform)
           (com.github.lukebemish.clojurewrapper.util RunPlatform)
           (clojure.lang Symbol)))

(def forge Platform/FORGE)
(def fabric Platform/FABRIC)

(defn get-platform [] (Platform/getPlatform))
(defn unimpl-error [] (Platform/unimplError))

(defn fabric? [] (= (get-platform) fabric))
(defn forge? [] (= (get-platform) forge))

(def fn-platform (memoize (fn [symb] (RunPlatform/runPlatform (str symb)))))
(defn run-platform [^Symbol symb & context]
  (let [found (fn-platform symb)]
    (if (nil? found)
      (unimpl-error)
      (apply found context))))