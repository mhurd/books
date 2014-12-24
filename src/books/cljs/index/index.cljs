(ns index
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [ajax.core :refer [GET POST]]
            [cemerick.url :refer [url-encode]]
            [jayq.core :refer [$ fade-in fade-out]]))

(enable-console-print!)

(def app-state (atom {:message "",
                      :books-json ""}))

(defn set-message [msg]
  (let [s (str msg)]
    (swap! app-state assoc :message s)
    (-> ($ :#message)
        (.stop)
        (fade-in 1)
        (fade-out 10000)))
  )

(defn set-books-json [json]
  (let [s (str json)]
    (swap! app-state assoc :books-json s))
  )

(defn get-books []
  (println "Getting books...")
  (GET "http://localhost:3000/books"
       {:response-format :json,
        :handler         set-books-json,
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
      (dom/div #js {:className "content"}
               (dom/div #js {:id "message" :className "messages"} (dom/label nil (:message app)))
               (dom/div #js {:id "json" :className "json"} (dom/label nil (:books-json app)))))))

(om/root index-view app-state
         {:target (. js/document (getElementById "index-page"))})