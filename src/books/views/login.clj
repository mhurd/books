(ns books.views.login
  (:require
    [hiccup
     [page :refer [html5 include-js include-css]]]))

(defn with-css []
  (list
    ;; more css e.g. (include-css "/css/bootstrap.css")
    (include-css "/css/login.css")
    (include-css "http://yui.yahooapis.com/pure/0.5.0/pure-min.css")))

(defn with-js []
  (list
    (include-js "/lib/jquery-2.1.3.min.js")
    (include-js "//fb.me/react-0.9.0.js")
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