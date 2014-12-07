(ns books.handler
  (:require [books.views.layout :refer [say]]
            [books.routes.home :refer [home-routes]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(def app
  (handler/site home-routes))
