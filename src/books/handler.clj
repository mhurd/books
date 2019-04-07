(ns books.handler
  (:require
   [org.httpkit.server :refer [run-server]]
   [books.views.books :refer [index-page]]
   [books.amazon.amazon-client :refer [find-by-isbn]]
   [books.amazon.amazon-json :refer [xml-to-json map-to-json]]
   [books.mongo.mongo-repository :as mongo]
   [ring.middleware.stacktrace :as stacktrace]
   [compojure.handler :refer [site]]
   [compojure.route :as route]
   [compojure.core :refer [routes GET POST]]
   [cemerick.url :refer [url-decode]]
   [clojure.tools.logging :as log])
  (:gen-class))

;; Set up to use http-kit rather than usual ring Jetty server,
;; see: http://www.http-kit.org/migration.html
;; will be required when getting round to WebSockets:
;; https://github.com/ptaoussanis/sente

;; run the server with 'lein run'

(def example-isbns ["081095415X", "3865216455", "3822856215", "0714846554", "3775727507", "3865214517",
                    "8881587904", "3869307889", "B003WF4UCY", "2953451617", "B007RC8EYS", "3829028911",
                    "187331924X", "1873319150"])

(defn no-mongo? [] false) ;; change this to true to load examples direct from Amazon API

(defn get-books [access-key associate-tag secret page-size page api-sleep]
  (log/info "Getting books...")
  (if (no-mongo?)
    ((memoize
      (fn [page-size page]
        (let [json (xml-to-json (map #(do
                                        (Thread/sleep api-sleep)
                                        (find-by-isbn access-key associate-tag secret %)) example-isbns))]
          json))) page-size page)
    (map-to-json (mongo/get-books))))

;; don't use the defroutes macro as there is no nice way to inject app-state into it without globals
(defn main-routes [access-key associate-tag secret api-sleep]
  (routes
   (GET "/" [] (index-page))
   (GET "/api/books/:id" [id] (xml-to-json (find-by-isbn access-key associate-tag secret id)))
   (GET "/api/books" [page-size page] (get-books access-key associate-tag secret page-size page api-sleep))
   (route/resources "/")
   (route/not-found "<p>Page not found.</p>")))

(defn get-handler [access-key associate-tag secret api-sleep]
  (stacktrace/wrap-stacktrace (site (main-routes access-key associate-tag secret api-sleep))))
