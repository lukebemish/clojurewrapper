(ns com.github.lukebemish.clojurewrapper.api.util.functional
  (:import (java.lang.reflect Method)
           (java.util.function Supplier Function Consumer Predicate BiFunction BiConsumer BinaryOperator BiPredicate UnaryOperator)))

; ... this isn't dangerous at all. We reify the object from its class at compile time. The good news is, if it fails
; terribly, you'll get a compile error. Should work with any functoinal interface.
(defmacro functional [function interface-in]
      (let [^Class interface (eval interface-in)
            ^Method method (get (filter #(not (.isDefault ^Method %)) (. interface getDeclaredMethods)) 0)
            types (map #(symbol (str "p" %)) (range (. method getParameterCount)))]
        `(reify ~interface-in
           (~(symbol (. method getName)) [this# ~@types] (apply ~function [~@types])))))

(defn supplier [function] (functional function Supplier))
(defn consumer [function] (functional function Consumer))
(defn function [function] (functional function Function))
(defn predicate [function] (functional function Predicate))
(defn bifunction [function] (functional function BiFunction))
(defn biconsumer [function] (functional function BiConsumer))
(defn bipredicate [function] (functional function BiPredicate))
(defn binop [function] (functional function BinaryOperator))
(defn unop [function] (functional function UnaryOperator))