;; required to enable http access to local repo
;;(require 'cemerick.pomegranate.aether)
;;(cemerick.pomegranate.aether/register-wagon-factory!
;; "http" #(org.apache.maven.wagon.providers.http.HttpWagon.))

(defproject books "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :min-lein-version "2.3.4"

  :source-paths ["src" "src/books"]

  dependencies [[compojure "1.6.1"]
                 [ring "1.7.1"]
                 [org.clojure/clojurescript "1.10.516"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/core.async "0.4.490"]
                 [om "0.8.0-rc1"]
                 [http-kit "2.3.0"]
                 [cljs-ajax "0.8.0"]
                 [com.cemerick/url "0.1.1"]
                 [org.clojure/tools.logging "0.5.0-alpha"]
                 [log4j/log4j "1.2.17"
                  :exclusions [javax.mail/mail
                               javax.jms/jms
                               com.sun.jdkmk/jmxtools
                               com.sun.jmx/jmxri]]
                 [log4j/apache-log4j-extras "1.2.17"]
                 [org.slf4j/slf4j-log4j12 "1.7.9"]
                 [jayq "2.5.5"]
                 [org.clojure/data.zip "0.1.1"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.json "0.2.5"]
                 [secretary "1.2.3"]                        ;; see http://spootnik.org/entries/2014/10/26_from-angularjs-to-om-a-walk-through.html
                 [sablono "0.8.5"]                         ;; see https://github.com/r0man/sablono
                 [prismatic/om-tools "0.5.0"]
                 [com.cognitect/transit-clj "0.8.313"]      ;; see https://github.com/cognitect/transit-format
                 [com.cognitect/transit-cljs "0.8.256"]
                 [com.novemberain/monger "3.5.0"]           ;; see http://clojuremongodb.info/articles/getting_started.html
                 [clojurewerkz/quartzite "2.1.0"]           ;; see http://clojurequartz.info/articles/getting_started.html
                 ]

  :plugins [[lein-ring "0.12.4"]
            [lein-cljsbuild "1.1.7"]
            ;; lein marg --dir resources/public/docs src/gridlife/gamemodel.cljs
            [lein-marginalia "0.9.1"]
            ;; https://github.com/weavejester/cljfmt/
            ;; lein cljfmt check, lein cljfmt fix
            [lein-cljfmt "0.6.4"]]

  :hooks [leiningen.cljsbuild]

  :cljsbuild {
    :builds [{:source-paths ["src/books/cljs/library"]
              :compiler {:output-to     "resources/public/js/books.js"
                         :optimizations :advanced
                         :pretty-print  false
                         :externs       ["externs/externs.js",
                                         "resources/public/lib/jquery-2.1.3.min.js",
                                         "resources/public/lib/bootstrap.min.js"]
                         }}
             {:source-paths ["src/books/cljs/main"]
              :compiler {:output-to "resources/public/js/main.js"
                         :optimizations :advanced
                         :pretty-print false
                         :externs ["externs/externs.js",
                                   "resources/public/lib/jquery-2.1.3.min.js",
                                   "resources/public/lib/bootstrap.min.js"]
                         }}
             {:source-paths ["src/books/cljs/login"]
              :compiler {:output-to     "resources/public/js/login.js"
                         :optimizations :advanced
                         :pretty-print  false
                         :externs ["externs/externs.js",
                                   "resources/public/lib/jquery-2.1.3.min.js",
                                   "resources/public/lib/bootstrap.min.js"]
                         }}]}

  :main books.main
  :aot [books.main]
 
  :warnings false

  :profiles
    {:dev {:dependencies [[ring-mock "0.1.5"]
                          [javax.servlet/servlet-api "2.5"]]}
     })
