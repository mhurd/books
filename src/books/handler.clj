(ns books.handler
  (:use [org.httpkit.server :only [run-server]])
  (:require [books.views.main :refer [say]]
            [books.views.login :refer [login-page]]
            [books.views.index :refer [index-page]]
            [books.amazon.amazon-client :refer [find-by-isbn]]
            [books.amazon.amazon-json :refer [xml-to-json map-to-json]]
            [mongo.mongo-repository :as mongo]
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

(defn no-mongo? [] false) ;; change this to true to load direct from Amazon API

(def access-key (atom nil))
(def associate-tag (atom nil))
(def secret (atom nil))

(def isbns ["081095415X", "3865216455", "3822856215", "0714846554", "3775727507", "3865214517",
            ;;"3865212336", "093511209X", "190707130X", "1905712022", "0393065642", "3775729941",
            ;;"0224087576", "0316117722", "0870700944", "0810963981", "0822323559", "0810993805",
            ;;"0979918839", "0810945312", "3836503891", "9078909072", "1903796423", "0520204360",
            ;;"0870708120", "1597110922", "386521925X", "3865217168", "1861541384", "0821228765",
            ;;"383652726X", "0375506209", "3865219438", "0714845736", "159711121X", "9070478234",
            ;;"1597110612", "0963470701", "1854379259", "0974886300", "1881337189", "2952410224",
            ;;"2952410216", "0954281365", "1907946136", "0714844306", "3931141969", "8496898423",
            ;;"8836614906", "3836538725", "3882439602", "1904587968", "3775720987", "3775726616",
            ;;"029272649X", "0224089706", "0679404848", "490294300X", "0789313812", "8890981709",
            ;;"157687429X", "3865216013", "1904563724", "0789306336", "2916355006", "1881450279",
            ;;"1855144174", "1597110949", "3865213715", "3941825097", "0500544026", "029273963X",
            ;;"1933045736", "1564660567", "1881337200", "1426203292", "190443844X", "0752226649",
            ;;"0714846376", "0870705156", "1931788545", "8869651657", "1597110582", "0500543992",
            ;;"0500543666", "8496466809", "0847831493", "0253349672", "0912810408", "0870706829",
            ;;"1597110566", "284426364X", "383652077X", "3775726837", "0821221868", "0821221876",
            ;;"386521827X", "0316006939", "0385261225", "0811848655", "0893817465", "3791345206",
            ;;"0199757143", "1933952474", "190379630X", "1903796326", "3836501899", "0525949852",
            ;;"0811843181", "3791324845", "2915359385", "0955739462", "1597111449", "386521584X",
            ;;"1931885516", "159711135X", "1597111627", "3865211399", "3869302569", "0870703382",
            ;;"0500542872", "0714846643", "3829600461", "0321316304", "0714848328", "0821221841",
            ;;"0500542783", "190789327X", "1844003639", "0300099258", "0316730254", "0375422153",
            ;;"0870707213", "1597110930", "1847721109", "0956887201", "087070835X", "2915173826",
            ;;"1426206372", "1931885486", "3865219152", "0870703781", "0300126212", "1931885931",
            ;;;;"3775731482", "0974283673", "1847960006", "1567923593", "0500512515", "0954709128",
            ;;"1576874478", "1907946144", "0957434103", "091501355X", "1907893113", "1597112135",
            ;;"0500301247", "0811872238", "1617751677", "0810998327", "3775727388", "8857204715",
            "8881587904", "3869307889", "B003WF4UCY", "2953451617", "B007RC8EYS"])

(defn get-books [page-size page]
  (if (no-mongo?)
    ((memoize
      (fn [page-size page]
        (let [json (xml-to-json (map #(do
                                   (Thread/sleep 1000)
                                   (find-by-isbn @access-key @associate-tag @secret %)) isbns))]
          ;;(println json)
          json)
        )) page-size page)
    (map-to-json (mongo/get-books))
    )
  )

(defroutes main-routes
           (GET "/" [] (index-page))
           (GET "/login" [] (login-page))
           (GET "/echo/:email/:password" [email password] (str "\"" (url-decode email) "'s secret password is " (url-decode password) " (although I probably shouldn't tell you that)\""))
           (GET "/api/books/:id" [id] (xml-to-json (find-by-isbn @access-key @associate-tag @secret id)))
           (GET "/api/books" [page-size page] (get-books page-size page))
           (route/resources "/")
           (route/not-found "<p>Page not found.</p>"))

(defn -main [& args]                                        ;; entry point, lein run will pick up and start from here
  (let [handler (stacktrace/wrap-stacktrace (site main-routes))]
    (println args)
    (reset! access-key (first args))
    (reset! associate-tag (second args))
    (reset! secret (second (rest args)))
    (run-server handler {:port 3000})))