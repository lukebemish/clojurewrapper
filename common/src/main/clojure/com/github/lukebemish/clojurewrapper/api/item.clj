(ns com.github.lukebemish.clojurewrapper.api.item
  (:import (com.github.lukebemish.clojurewrapper.wrappers ItemWrapper))
  (:require [com.github.lukebemish.clojurewrapper.api.util :as util]))

(defn supply-item [mapping] (util/supplier (fn [] (ItemWrapper. (fn [string] (get mapping string))))))
