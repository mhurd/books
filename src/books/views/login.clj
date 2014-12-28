(ns books.views.login
  (:require
    [hiccup
     [page :refer [html5 include-js include-css]]]))

(defn with-css []
  (list
    (include-css "/css/login.css")
    (include-css "/css/bootstrap.min.css")
    (include-css "/css/bootstrap-theme.min.css")))

(defn with-js []
  (list
    (include-js "/lib/jquery-2.1.3.min.js")
    (include-js "//fb.me/react-0.9.0.min.js")
    (include-js "/lib/bootstrap.min.js")
    (include-js "/js/login.js")))

(defn login-page []
  (html5
    [:head
     [:title "login"]
     [:meta {"name" "viewport" "content" "width=device-width, initial-scale=1.0"}]
     (with-css)]
    [:body
     [:div {:id "login-page"}]
     (with-js)]))