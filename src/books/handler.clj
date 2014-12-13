(ns books.handler
  (:use [org.httpkit.server :only [run-server]])
  (:require [books.views.main :refer [say]]
            [books.views.login :refer [login-page]]
            [ring.middleware.reload :as reload]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [compojure.core :refer [defroutes GET POST]]
            [cemerick.url :refer [url-decode]]))

;; Set up to use http-kit rather than usual ring Jetty server,
;; see: http://www.http-kit.org/migration.html
;; will be required when getting round to WebSockets:
;; https://github.com/ptaoussanis/sente

;; run the server with 'lein run'

(defn decode [s]
  (url-decode (clojure.string/replace s "%" "%25")))

(defroutes main-routes
           (GET "/" [] (say "I am ready to be initialised!"))
           (GET "/login" [] (login-page))
           (GET "/echo/:email/:password" [email password] (str "\"" (decode email) "'s secret password is " (decode password) " (although I probably shouldn't tell you that)\""))
           (route/resources "/")
           (route/not-found "<p>Page not found.</p>"))

(defn in-dev? [args] true)                                  ;; TODO read a config variable from command line, env, or file?

(defn -main [& args]                                        ;; entry point, lein run will pick up and start from here
  (let [handler (if (in-dev? args)
                  (reload/wrap-reload (site #'main-routes)) ;; only reload when dev
                  (site main-routes))]
    (run-server handler {:port 3000})))
