(ns books.views.books
  (:require
    [hiccup
     [page :refer [html5 include-js include-css]]]))

(defn with-css []
  (list
    (include-css "/css/books.css")
    (include-css "/css/bootstrap.min.css")))

(defn with-js []
  (list
    (include-js "/lib/jquery-2.1.3.min.js")
    (include-js "/lib/react-0.12.2.min.js")
    (include-js "/lib/bootstrap.min.js")
    (include-js "/js/books.js")))

(defn index-page []
  (html5
    [:head
     [:title "Mike's Books"]
     [:meta {"name" "viewport" "content" "width=device-width, initial-scale=1.0"}]
     (with-css)
     (include-js "/lib/google-analytics.js")]
    [:body
     [:div {:id "book-list"}]
     [:div {:id "book" :class "content"}]
     (with-js)]))