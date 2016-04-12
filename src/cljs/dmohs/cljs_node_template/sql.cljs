(ns dmohs.cljs-node-template.sql
  (:require-macros [dmohs.cljs-node-template.sql :refer [->map]]))


(defn- parse [statement]
  (let [sql-interpose (fn [value & paren-wrap?]
                        (let [substatements (map parse (rest statement))
                              sql (interpose value (map :sql substatements))]
                          {:sql (if paren-wrap? (concat (list "(") sql (list ")")) sql)
                           :params (apply concat (map :params substatements))}))]
    (if (sequential? statement)
      (let [op (first statement)]
        (case op
          :newlines (sql-interpose "\n")
          :and (sql-interpose " and " true)
          :or (sql-interpose " or " true)
          :str (let [substatements (map parse (rest statement))]
                 {:sql (map :sql substatements)
                  :params (apply concat (map :params substatements))})
          :$i (do (assert (= 1 (count (rest statement))) (str "invalid value: " (rest statement)))
                  (let [value (second statement)]
                    {:sql :$i :params (list value)}))
          :? (do (assert (= 1 (count (rest statement))) (str "invalid value: " (rest statement)))
               (let [value (second statement)]
                 {:sql "?" :params (list value)}))
          (let [substatements (map parse statement)]
            {:sql (concat (list "(") (interpose " " (map :sql substatements)) (list ")"))
             :params (apply concat (map :params substatements))})))
      {:sql statement :params (list)})))


(defn- join-with-param-indexes [sql]
  (let [index (atom 0)]
    (reduce (fn [r x] (str r (if (= x :$i) (str "$" (swap! index inc)) x))) "" sql)))


(defn from-list [statement]
  (update-in (parse statement) [:sql] #(-> % flatten join-with-param-indexes)))
