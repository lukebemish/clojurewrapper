(ns com.github.lukebemish.clojurewrapper.api.item
  (:import (com.github.lukebemish.clojurewrapper.wrappers ItemWrapper)))

(defn item-properties [mapping] (ItemWrapper/props (fn [string default] (get mapping (keyword string) default))))