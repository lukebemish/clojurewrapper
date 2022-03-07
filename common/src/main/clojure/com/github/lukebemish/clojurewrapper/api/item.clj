(ns com.github.lukebemish.clojurewrapper.api.item
  (:import (com.github.lukebemish.clojurewrapper.wrappers ItemWrapper))
  (:require [com.github.lukebemish.clojurewrapper.api.util.functional :as functional]))

(defn supply-item [mapping] (functional/supplier (fn [] (ItemWrapper. (fn [string] (get mapping string))))))
