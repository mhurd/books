(ns library.books
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [ajax.core :refer [GET POST]]
            [cemerick.url :refer [url-encode]]
            [jayq.core :refer [$ fade-in fade-out]]))

(enable-console-print!)

(def app-state (atom {:message "",
                      :books []}))

(defn set-message [msg]
  (let [s (str msg)]
    (swap! app-state assoc :message s)
    (-> ($ :#message)
        (.stop)
        (fade-in 1)
        (fade-out 30000)))
  )

(defn set-books [books]
  (let [sorted (sort-by (partial get "title") books)]
  (swap! app-state assoc :books (vec (filter #(nil? (get % "error")) sorted))) ;; filter out any errors
  (doseq [b (filter #(comp not nil? (get % "error")) sorted)] (set-message (get b "error"))) ;; display them though
  ))

(defn get-books []
  (println "Getting books...")
  (GET "/api/books"
       {:response-format :transit,
        :handler         set-books,
        :error-handler   set-message})
  )

(defn get-price [book price-key]
  (let [price (get book price-key)]
    (if (nil? price)
      "no-data"
      (str "£" (.toFixed (/ (js/parseFloat price) 100) 2))
      )
    ))

(defn get-attribute [book key]
  (let [val (get book key)]
    (if (nil? val)
      "no-data"
      val
      )
    )
  )

(defn has-increased-in-value [book]
  (let [listPrice (get book :listPrice)
        lowestPrice (get book :lowestPrice)]
    (if (or (nil? lowestPrice) (nil? listPrice))
      false
      (> lowestPrice listPrice)
    )
    ))

(defn format-timestamp [timestamp]
  (let [date (js/Date. (js/parseInt timestamp))
        formatted (.toUTCString date)]
    formatted
    )
  )

(defn light-book-view [book owner]
  (reify
    om/IRender
    (render [_]
      (html
        [:div {:class "book-div"}
         [:legend (str (get-attribute book :title))]
         [:table {:class "table"}
          [:tr {:class (if (has-increased-in-value book) "increased-value" "decreased-value")}
           [:td {:class "book-img-td" :align "right"}
            [:a {:href (get book :amazonPageUrl) :target "_blank"}
             [:img {:class "book-img" :src (get book :mediumImage)}]]]
           [:td {:class "book-details-td" :align: "left"}
            [:dl {:class "dl-horizontal details"}
             [:dt "Author(s):"] [:dd (get-attribute book :authors)]
             [:dt "Publisher:"] [:dd (get-attribute book :publisher)]
             [:dt "Publication Date:"] [:dd (get-attribute book :publicationDate)]
             [:dt "ASIN:"] [:dd (get-attribute book :asin)]
             [:dt "Total Available:"] [:dd (get-attribute book :totalAvailable)]
             [:dt "List price:"] [:dd (get-price book :listPrice)]
             [:dt "Lowest Price:"] [:dd (get-price book :lowestPrice)]
             [:dt "Price's Updated:"] [:dd (format-timestamp (get-attribute book :lastPriceUpdateTimestamp))]
             ]]
           ]]]))))

(defn single-book-view [book owner]
  (reify
    om/IRender
    (render [_]
      (html
        [:div {:class "book-div"}
          [:legend (get-attribute book :title)]
          [:table {:class "table"}
           [:tr {:class (if (has-increased-in-value book) "increased-value" "decreased-value")}
            [:td {:class "book-img-td" :align "right"}
             [:a {:href (str "/api/books/" (get book :isbn)) :target "_blank"}
              [:img {:class "book-img" :src (get book :largeImage)}]]]
            [:td {:class "book-details-td" :align: "left"}
             [:dl {:class "dl-horizontal details"}
              [:dt "Title:"] [:dd (get-attribute book :title)]
              [:dt "Author(s):"] [:dd (get-attribute book :authors)]
              [:dt "Publisher:"] [:dd (get-attribute book :publisher)]
              [:dt "Publication Date:"] [:dd (get-attribute book :publicationDate)]
              [:dt "Binding:"] [:dd (get-attribute book :binding)]
              [:dt "Edition:"] [:dd (get-attribute book :edition)]
              [:dt "Format:"] [:dd (get-attribute book :format)]
              [:dt "No. of Pages:"] [:dd (get-attribute book :numberOfPages)]
              [:dt "ASIN:"] [:dd (get-attribute book :asin)]
              [:dt "ISBN:"] [:dd (get-attribute book :isbn)]
              [:dt "EAN:"] [:dd (get-attribute book :ean)]
              [:dt "List price:"] [:dd (get-price book :listPrice)]
              [:dt "Price's Updated:"] [:dd (format-timestamp (get-attribute book :lastPriceUpdateTimestamp))]
              [:dt "Lowest Price:"] [:dd (get-price book :lowestPrice)]
              [:dt "Total Available:"] [:dd (get-attribute book :totalAvailable)]
              [:dt] [:dd
                     [:a {:href (get book :amazonPageUrl) :target "_blank"}
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
              (om/build-all light-book-view (:books app))]
             ]))))

(om/root index-view app-state
         {:target (. js/document (getElementById "index-page"))})