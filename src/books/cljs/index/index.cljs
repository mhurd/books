(ns index
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [ajax.core :refer [GET POST]]
            [cemerick.url :refer [url-encode]]
            [jayq.core :refer [$ fade-in fade-out]]))

(enable-console-print!)

(def app-state (atom {:message "",
                      :books nil}))

(defn set-message [msg]
  (let [s (str msg)]
    (swap! app-state assoc :message s)
    (-> ($ :#message)
        (.stop)
        (fade-in 1)
        (fade-out 10000)))
  )

(defn set-books [books]
  (swap! app-state assoc :books books)
  (doseq [b books] (println (get b "mediumImage"))))

(defn get-books []
  (println "Getting books...")
  (GET "http://localhost:3000/books"
       {:response-format :transit,
        :handler         set-books,
        :error-handler   set-message})
  )

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
             [:div {:id "json" :class "json"}
              [:label (:books app)]]
             ]))))

(om/root index-view app-state
         {:target (. js/document (getElementById "index-page"))})