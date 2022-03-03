(ns com.github.lukebemish.clojurewrapper.util)

(defn eval-literal [form]
  (try (eval form) (catch Exception exception form)))