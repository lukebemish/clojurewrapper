(ns com.github.lukebemish.clojurewrapper.api.util.functional
  (:import (java.lang.reflect Method)
           (java.util.function Supplier)))

; ... this isn't dangerous at all. We reify the object from its class at compile time.
(defmacro functional [funct interface-in]
      (let [^Class interface (eval interface-in)
            ^Method method (get (. interface getDeclaredMethods) 0)
            types (map #(symbol (str "p" %)) (range (. method getParameterCount)))]
        `(reify ~interface-in
           (~(symbol (. method getName)) [this# ~@types] (apply ~funct ~types)))))

(defn supplier [function] (functional function Supplier))