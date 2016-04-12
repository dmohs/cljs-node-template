(ns dmohs.cljs-node-template.sql
  (:require [clojure.walk :as w]))


(defmacro ->map [x]
  `(dmohs.cljs-node-template.sql/from-list
    ~(w/postwalk #(if (sequential? %) (cons 'list %) %) x)))
