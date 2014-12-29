(ns books.views.index
  (:require
    [hiccup
     [page :refer [html5 include-js include-css]]]))

(defn with-css []
  (list
    (include-css "/css/index.css")
    (include-css "/css/bootstrap.min.css")
    (include-css "/css/bootstrap-theme.min.css")))

(defn with-js []
  (list
    (include-js "/lib/jquery-2.1.3.min.js")
    (include-js "/lib/react-0.12.2.min.js")
    (include-js "/lib/bootstrap.min.js")
    (include-js "/js/index.js")))

(defn index-page []
  (html5
    [:head
     [:title "index"]
     [:meta {"name" "viewport" "content" "width=device-width, initial-scale=1.0"}]
     (with-css)]
    [:body
     [:div {:id "index-page"}]
     (with-js)]))