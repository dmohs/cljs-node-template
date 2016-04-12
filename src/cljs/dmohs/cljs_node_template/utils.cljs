(ns dmohs.cljs-node-template.utils
  (:require
   [cljs.nodejs :as nodejs]
   cljs.pprint
   clojure.string
   ))


(defn log [& args]
  (let [arr (array)]
    (doseq [x args] (.push arr x))
    (js/console.log.apply js/console arr))
  (last args))


(defn cljslog [& args]
  (apply log (map #(with-out-str (cljs.pprint/pprint %)) args))
  (last args))


(defn jslog [& args]
  (apply log (map clj->js args))
  (last args))


(defn json->clj [x]
  (try
    (js->clj (js/JSON.parse x))
    (catch js/Object e
      :json-parse-error)))


(defn clj->json
  ([x] (clj->json x true))
  ([x pretty-print?]
     (if pretty-print?
       (js/JSON.stringify (clj->js x) nil 2)
       (js/JSON.stringify (clj->js x)))))


(defn trim-to-nil [s]
  (when s
    (let [t (clojure.string/trim s)]
      (if (clojure.string/blank? t) nil t))))


(defn merger [& xs]
  (apply merge-with (fn [a b] (if (and (map? a) (map? b)) (merge a b) b)) xs))
