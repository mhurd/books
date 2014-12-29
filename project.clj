(defproject books "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :min-lein-version "2.3.4"

  :source-paths ["src" "src/books"]

  :dependencies [[compojure "1.3.1"]
                 [ring "1.3.2"]
                 [org.clojure/clojurescript "0.0-2511"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [om "0.8.0-beta5"]
                 [http-kit "2.1.18"]
                 [cljs-ajax "0.3.3"]
                 [com.cemerick/url "0.1.1"]
                 [org.clojure/tools.logging "0.3.1"]
                 [jayq "2.5.2"]
                 [org.clojure/data.zip "0.1.1"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.json "0.2.5"]
                 [secretary "1.2.1"]                        ;; see http://spootnik.org/entries/2014/10/26_from-angularjs-to-om-a-walk-through.html
                 [sablono "0.2.22"]                         ;; see https://github.com/r0man/sablono
                 [cljs-ajax "0.3.3"]
                 [prismatic/om-tools "0.3.6"]
                 [com.cognitect/transit-clj "0.8.259"]      ;; see https://github.com/cognitect/transit-format
                 [com.cognitect/transit-cljs "0.8.194"]
                 [com.novemberain/monger "2.0.0"]           ;; see http://clojuremongodb.info/articles/getting_started.html
                 ]

  :plugins [[lein-ring "0.8.13"]
            [lein-cljsbuild "1.0.3"]]

  :hooks [leiningen.cljsbuild]

  :cljsbuild {
    :builds [{:source-paths ["src/books/cljs/index"]
              :compiler {:output-to     "resources/public/js/index.js"
                         :optimizations :whitespace
                         :pretty-print  true
                         :externs       ["lib/jquery-2.1.3.min.js", "lib/bootstrap.min.js"]
                         }}
             {:source-paths ["src/books/cljs/main"]
              :compiler {:output-to "resources/public/js/main.js"
                         :optimizations :whitespace
                         :pretty-print true
                         :externs ["lib/jquery-2.1.3.min.js", "lib/bootstrap.min.js"]
                         }}
             {:source-paths ["src/books/cljs/login"]
              :compiler {:output-to     "resources/public/js/login.js"
                         :optimizations :whitespace
                         :pretty-print  true
                         :externs ["lib/jquery-2.1.3.min.js", "lib/bootstrap.min.js"]
                         }}]}

  :main books.handler

  :profiles
    {:dev {:dependencies [[ring-mock "0.1.5"]
                          [javax.servlet/servlet-api "2.5"]]}
     })
