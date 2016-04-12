(ns dmohs.cljs-node-template.main
  (:require
   [cljs.nodejs :as nodejs]
   [dmohs.cljs-node-template.sql :as sql]
   [dmohs.cljs-node-template.utils :as u]
   ))

(def http (nodejs/require "http"))
#_(def pg (nodejs/require "pg"))


(defn- handle-request [req res]
  (.end res "Hello World!"))


#_(defn- exec-query [query f]
  (.connect pg "postgres://postgres@db/postgres"
            (fn [err client done]
              (when err (throw err))
              (let [{:keys [sql params]} query]
                (.query client sql (clj->js params)
                        (fn [err result]
                          (done)
                          (when err (throw err))
                          (f result)))))))


;; Allow the request handler to be hot reloaded (maybe http.createServer hangs on to it?)
(defonce request-handler (atom nil))
(reset! request-handler handle-request)


(defn -main [& args]
  (-> (.createServer http (fn [req res] (@request-handler req res)))
      (.listen 80))
  (u/log "Server running on port 80."))


(set! *main-cli-fn* -main)


#_(exec-query
 (sql/->map (:str "select 1 + " (:$i 4) " as answer"))
 (fn [result] (u/log result)))
