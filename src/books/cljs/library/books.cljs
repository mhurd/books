(ns library.books
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [ajax.core :refer [GET POST]]
            [cemerick.url :refer [url-encode]]
            [jayq.core :refer [$]]
            [goog.events :as events])
  (:import goog.History))

(enable-console-print!)

;; Browser History
;; https://github.com/fmw/vix/blob/master/src/cljs/src/util.cljs
;; https://github.com/fmw/vix/blob/master/src/cljs/src/core.cljs

(def app-state (atom {:sorted-books [],
                      :indexed-books {},
                      :display {},
                      :scroll-pos 0}))

(defn get-attribute [book key]
  (let [val (get book key)]
    (if (nil? val)
      "no-data"
      val)))

(defn display-book [book]
  (let [current-scroll (.scrollTop ($ js/document))]
    ;; save the current scroll position for when we return
    (swap! app-state assoc :scroll-pos current-scroll)
    (swap! app-state assoc :display book)))

(defn display-index []
  (swap! app-state assoc :display {}))

(defn set-books [books]
  (let [sorted (sort-by :title books)
        indexed (zipmap (map #(:asin %) sorted) sorted)]
    (swap! app-state assoc :sorted-books sorted)
    (swap! app-state assoc :indexed-books indexed)
    ;; now we've loaded the books completely honour the location hash if present.
    (let [location (.-hash js/window.location)]
      (if (not (or (nil? location) (empty? location)))
        ;; for some reason this location hasj includes the '#', remove it.
        (let [trimmed (.substring location 1)]
          (display-book (get indexed trimmed)))))))

(defn handle-error [error]
  (println (str error)))

(defn get-books []
  (GET "/api/books"
    {:response-format :transit,
     :handler         set-books,
     :error-handler   handle-error}))

(defn get-price [book price-key]
  (let [price (get book price-key)]
    (if (nil? price)
      "no-data"
      (str "£" (.toFixed (/ (js/parseFloat price) 100) 2)))))

(defn has-increased-in-value [book]
  (let [listPrice (get book :listPrice)
        lowestPrice (get book :lowestPrice)]
    (if (or (nil? lowestPrice) (nil? listPrice))
      false
      (> lowestPrice listPrice))))

(defn get-price-change [book]
  (let [listPrice (get book :listPrice)
        lowestPrice (get book :lowestPrice)
        difference (- lowestPrice listPrice)]
    (if (or (nil? lowestPrice) (nil? listPrice))
      "?"
      (str "£" (.toFixed (/ (js/parseFloat difference) 100) 2)))))

(defn format-timestamp [timestamp]
  (let [date (js/Date. (js/parseInt timestamp))
        formatted (.toUTCString date)]
    formatted))

(defn get-image [book key]
  (let [url (get book key)]
    (if (nil? url) "/img/no-image.jpg" url)))

(defn light-book-view [book owner]
  (reify
    om/IRender
    (render [_]
      (html/html
       [:div {:class "book-div"}
        [:legend (str (get-attribute book :title))]
        [:table {:class "table"}
         [:tr {:class "book-row"}
          [:td {:class (str "book-img-td " (if (has-increased-in-value book) "increased-value" "decreased-value")) :align "right"}
           [:a {:href (str "#" (get-attribute book :asin))}
            [:img {:class "book-img" :src (get-image book :smallImage)}]]]
          [:td {:class "book-details-td" :align: "left"}
           [:dl {:class "dl-horizontal details"}
            [:dt "Author(s):"] [:dd (get-attribute book :authors)]
            [:dt "ASIN:"] [:dd (get-attribute book :asin)]
            [:dt "Publisher:"] [:dd (get-attribute book :publisher)]
            [:dt "Publication Date:"] [:dd (get-attribute book :publicationDate)]
            [:dt "Price Change:"] [:dd (get-price-change book)]]]]]]))))

(defn full-book-view [app owner]
  (reify
    om/IRender
    (render [_]
      (let [book (:display app)]
        (html/html
         (if (empty? (:display app))
           [:div {:class "book-div"}]
           [:div {:class "book-div"}
            [:legend (get-attribute (:display app) :title)]
            [:table {:class "table"}
             [:tr {:class "book-row"}
              [:td {:class (str "large-book-img-td " (if (has-increased-in-value book) "increased-value" "decreased-value")) :align "right"}
               [:img {:class "large-book-img" :src (get-image book :largeImage)}]]
              [:td {:class "book-details-td" :align: "left"}
               [:dl {:class "dl-horizontal details"}
                [:dt "Author(s):"] [:dd (get-attribute book :authors)]
                [:dt "ASIN:"] [:dd (get-attribute book :asin)]
                [:dt "ISBN:"] [:dd (get-attribute book :isbn)]
                [:dt "EAN:"] [:dd (get-attribute book :ean)]
                [:dt "Publisher:"] [:dd (get-attribute book :publisher)]
                [:dt "Publication Date:"] [:dd (get-attribute book :publicationDate)]
                [:dt "Binding:"] [:dd (get-attribute book :binding)]
                [:dt "Edition:"] [:dd (get-attribute book :edition)]
                [:dt "Format:"] [:dd (get-attribute book :format)]
                [:dt "No. of Pages:"] [:dd (get-attribute book :numberOfPages)]
                [:dt "List price:"] [:dd (get-price book :listPrice)]
                [:dt "Lowest Price:"] [:dd (get-price book :lowestPrice)]
                [:dt "Price Change:"] [:dd (get-price-change book)]
                [:dt "Total Available:"] [:dd (get-attribute book :totalAvailable)]
                [:dt "Price's Updated:"] [:dd (format-timestamp (get-attribute book :lastPriceUpdateTimestamp))]
                [:dt] [:dd
                       [:a {:href (get book :amazonPageUrl) :target "_blank"}
                        [:img {:src "/img/buy-from-amazon-button.gif" :caption "Buy from Amazon" :alt "Buy from Amazon"}]]]]]]]]))))))

(defn index-view [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {})
    om/IDidUpdate
    (did-update [this prev-props prev-state]
      ;; return to our saved scroll position for when we
      ;; come back to the index from viewing a book's full details
      (let [saved-scroll (:scroll-pos @app-state)]
        (.scrollTop ($ js/document) saved-scroll)))
    om/IRender
    (render [_]
      (html/html
       (if (empty? (:display app))
         [:div (om/build-all light-book-view (:sorted-books app))]
         [:div ])))))

(om/root index-view app-state
         {:target (. js/document (getElementById "book-list"))})

(om/root full-book-view app-state
         {:target (. js/document (getElementById "book"))})

(get-books)

;; Attach event listener to history instance.
(let [history (History.)]
  (events/listen history "navigate"
                 (fn [event]
                   (let [location (.-token event)]
                     (if (or (nil? location) (empty? location))
                       (display-index)
                       (display-book (get (:indexed-books @app-state) location))))))
  (.setEnabled history true))