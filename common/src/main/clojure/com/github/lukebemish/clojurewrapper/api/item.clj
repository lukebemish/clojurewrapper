(ns com.github.lukebemish.clojurewrapper.api.item
  (:import (com.github.lukebemish.clojurewrapper.wrappers ExtendedItemProperties)))

(defn item-properties [mapping] (ExtendedItemProperties/props (fn [string default] (get mapping (keyword string) default))))