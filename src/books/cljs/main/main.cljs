(ns main
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(def app-state (atom {:text "Hello world!"}))

(defn widget [data owner]
  (reify
    om/IRender
    (render [this]
      (html/html (:text data)))))

(om/root widget app-state
         {:target (. js/document (getElementById "root-page"))})
