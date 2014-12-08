(defproject books "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :min-lein-version "2.3.4"

  :source-paths ["src" "src/books"]

  :dependencies [[compojure "1.2.1"]
                 [ring "1.3.1"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [om "0.8.0-alpha1"]
                 [hiccup "1.0.5"]
                 [http-kit "2.1.18"]
                 [org.clojure/tools.logging "0.3.1"]]

  :plugins [[lein-ring "0.8.13"]
            [lein-cljsbuild "1.0.3"]]

  :hooks [leiningen.cljsbuild]

  :cljsbuild {
    :builds [{:source-paths ["src/books/cljs"]
              :compiler {:output-to "resources/public/js/books.js"
                         :optimizations :whitespace
                         :pretty-print true
                         ;; :source-map "resources/public/js/books.js.map"
                         }}]}

  :ring {:handler books.handler/app}

  :profiles
    {:dev {:dependencies [[ring-mock "0.1.5"]
                          [javax.servlet/servlet-api "2.5"]]}
     })
