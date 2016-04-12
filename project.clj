(defproject dmohs/cljs-node-template "0.0.1"
  :dependencies [[org.clojure/clojure "1.8.0"] ; keep up-to-date with clojurescript dep
                 [org.clojure/clojurescript "1.8.40"]]

  :plugins [[lein-cljsbuild "1.1.3"] [lein-figwheel "0.5.2"]]
  :profiles {:dev {:cljsbuild
                   {:builds
                    {:client
                     {:figwheel {:websocket-url ~(str "ws://server-build:3449/figwheel-ws")}}}}}}
  :cljsbuild {:builds {:client {:source-paths ["src/cljs"]
                                :compiler
                                {:target :nodejs
                                 :main dmohs.cljs-node-template.main
                                 :output-dir "target"
                                 :output-to "target/main.js"
                                 :optimizations :none
                                 :source-map true}}}})
