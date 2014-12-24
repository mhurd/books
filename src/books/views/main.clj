(ns books.views.main
  (:require
    [hiccup
     [page :refer [html5 include-js include-css]]]))

(defn with-css []
  (list
    (include-css "/css/main.css")))

(defn with-js []
  (list
    (include-js "http://fb.me/react-0.9.0.js")
    (include-js "/js/main.js")))

(defn say [content]
  (html5
    [:head
     [:title "books"]
     [:meta {"name" "viewport" "content" "width=device-width, initial-scale=1.0"}]
     (with-css)]
    [:body
     [:div {:id "root-page"} content]
     (with-js)]))