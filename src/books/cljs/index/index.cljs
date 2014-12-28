(ns index
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [ajax.core :refer [GET POST]]
            [cemerick.url :refer [url-encode]]
            [jayq.core :refer [$ fade-in fade-out]]))

(enable-console-print!)

(def app-state (atom {:message "",
                      :books '()}))

(defn set-message [msg]
  (let [s (str msg)]
    (swap! app-state assoc :message s)
    (-> ($ :#message)
        (.stop)
        (fade-in 1)
        (fade-out 30000)))
  )

(defn set-books [books]
  (swap! app-state assoc :books (filter #(nil? (get % "error"))  books)) ;; filter out any errors
  (doseq [b (filter #(comp not nil? (get % "error")) books)] (set-message (get b "error"))) ;; display them though
  )

(defn get-books []
  (println "Getting books...")
  (GET "http://localhost:3000/api/books"
       {:response-format :transit,
        :handler         set-books,
        :error-handler   set-message})
  )

(defn get-list-price [book]
  (str "£" (/ (js/parseFloat (get book "listPrice")) 100)))

(defn get-lowest-price [book]
  (str "£" (/ (js/parseFloat (get book "lowestPrice")) 100)))

(defn single-book-view [book owner]
  (println (str "Rendering book: " (get book "title")))
  (reify
    om/IRender
    (render [_]
      (html
        [:div {:class "book-div"}
        [:table {:class "table"}
         [:tr
          [:td {:class "book-img-td" :align "right"}
           [:a {:href (str "http://localhost:3000/api/books/" (get book "isbn")) :target "_blank"}
            [:img {:class "book-img" :src (get book "largeImage")}]]]
          [:td {:class "book-details-td" :align: "left"}
           [:dl {:class "dl-horizontal details"}
            [:dt "Title:"] [:dd (get book "title")]
            [:dt "Author(s):"] [:dd (get book "authors")]
            [:dt "Publisher:"] [:dd (get book "publisher")]
            [:dt "Publication Date:"] [:dd (get book "publicationDate")]
            [:dt "Binding:"] [:dd (get book "binding")]
            [:dt "Edition:"] [:dd (get book "edition")]
            [:dt "No. of Pages:"] [:dd (get book "numberOfPages")]
            [:dt "ASIN:"] [:dd (get book "asin")]
            [:dt "ISBN:"] [:dd (get book "isbn")]
            [:dt "EAN:"] [:dd (get book "ean")]
            [:dt "List price:"] [:dd (get-list-price book)]
            [:dt "Lowest Price:"] [:dd (get-lowest-price book)]
            [:dt "Total Available:"] [:dd (get book "totalAvailable")]
            [:dt] [:dd
                   [:a {:href (get book "amazonPageUrl") :target "_blank"}
                        [:img {:src "/img/buy-from-amazon-button.gif" :caption "Buy from Amazon" :alt "Buy from Amazon"}]]]]]
          ]]]))))

(defn index-view [app owner]
  (reify
    om/IInitState
    (init-state [_]
      (get-books)
      {})
    om/IRenderState
    (render-state [this state]
      (html/html [:div {:class "content"}
             [:div {:id "message" :class "messages"}
              [:label (:message app)]]
             [:div
              (om/build-all single-book-view (:books app))]
             ]))))

(om/root index-view app-state
         {:target (. js/document (getElementById "index-page"))})