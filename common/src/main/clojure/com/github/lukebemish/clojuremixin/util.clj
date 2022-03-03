(ns com.github.lukebemish.clojuremixin.util)

(defn eval-literal [form]
  (try (eval form) (catch Exception exception form)))