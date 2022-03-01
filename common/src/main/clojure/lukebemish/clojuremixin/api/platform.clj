(ns lukebemish.clojuremixin.platform
  (:import lukebemish.clojuremixin.Platform))

(defn get-platform [] (Platform/getPlatform))

(defmacro defn-specific [name]
  (list `do
        (list `use (list `quote (symbol (str *ns* (.name (Platform/getPlatform)))))
        (list `defn name (symbol (str name "-impl"))))))