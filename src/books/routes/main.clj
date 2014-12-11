(ns books.routes.main
  (:require
            [books.views.main :refer [say]]
            [books.views.login :refer [login-page]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes main-routes

  (GET "/" [] (say "I am ready to be initialised!"))
  (GET "/login" [] (login-page))

  (route/resources "/")
  (route/not-found "not found"))
