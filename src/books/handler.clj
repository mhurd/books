(ns books.handler
  (:use [org.httpkit.server :only [run-server]])
  (:require [books.views.main :refer [say]]
            [books.views.login :refer [login-page]]
            [books.amazon.amazon-client :refer [find-by-isbn]]
            [books.amazon.amazon-json :refer [to-json]]
            [ring.middleware.reload :as reload]
            [ring.middleware.stacktrace :as stacktrace]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [compojure.core :refer [defroutes GET POST]]
            [cemerick.url :refer [url-decode]]))

;; Set up to use http-kit rather than usual ring Jetty server,
;; see: http://www.http-kit.org/migration.html
;; will be required when getting round to WebSockets:
;; https://github.com/ptaoussanis/sente

;; run the server with 'lein run'

(def access-key (atom nil))
(def associate-tag (atom nil))
(def secret (atom nil))

(defroutes main-routes
           (GET "/" [] (say "I am ready to be initialised!"))
           (GET "/books/:id" [id] (to-json (find-by-isbn @access-key @associate-tag @secret id)))
           (GET "/login" [] (login-page))
           (GET "/echo/:email/:password" [email password] (str "\"" (url-decode email) "'s secret password is " (url-decode password) " (although I probably shouldn't tell you that)\""))
           (route/resources "/")
           (route/not-found "<p>Page not found.</p>"))

(defn in-dev? [args] false)                                  ;; TODO read a config variable from command line, env, or file?

(defn -main [& args]                                        ;; entry point, lein run will pick up and start from here
  (let [handler (if (in-dev? args)
                  (stacktrace/wrap-stacktrace (reload/wrap-reload (site #'main-routes))) ;; only reload when in dev mode
                  (stacktrace/wrap-stacktrace (site main-routes)))]
    (println args)
    (reset! access-key (first args))
    (reset! associate-tag (second args))
    (reset! secret (second (rest args)))
    (run-server handler {:port 3000})))