(ns books.handler
  (:require [books.routes.main :refer [main-routes]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]))

(def app
  (handler/site main-routes))
