(ns com.github.lukebemish.clojurewrapper.api.util.functional
  (:import (java.lang.reflect Method Modifier)
           (java.util.function Supplier Function Consumer Predicate BiFunction BiConsumer BinaryOperator BiPredicate UnaryOperator)))

; ... this isn't dangerous at all. We reify the object from its class at compile time. The good news is, if it fails
; terribly, you'll get a compile error. Should work with any functional interface.
(defmacro functional [function interface-in]
  (let [^Class interface (eval interface-in)
        ^Method method (first (filter #(Modifier/isAbstract (.getModifiers ^Method %)) (seq (.getMethods interface))))
        types (map #(symbol (str "p" %)) (range (.getParameterCount method)))]
    `(reify ~interface-in
       (~(symbol (. method getName)) [this# ~@types] (apply ~function [~@types])))))

; general types
(defn supplier [function] (functional function Supplier))
(defn consumer [function] (functional function Consumer))
(defn runnable [function] (functional function Runnable))
(defn function [function] (functional function Function))
(defn predicate [function] (functional function Predicate))
(defn bi-function [function] (functional function BiFunction))
(defn bi-consumer [function] (functional function BiConsumer))
(defn bi-predicate [function] (functional function BiPredicate))
(defn binary-operator [function] (functional function BinaryOperator))
(defn unary-operator [function] (functional function UnaryOperator))

; minecraft types
