(ns books.routes.home
  (:require [books.views.layout :refer [say]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes home-routes

  (GET "/" [] (say "I am ready to be initialised!"))

  (route/resources "/")
  (route/not-found "not found"))
