(ns login
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cemerick.url :refer [url-encode]]
            [ajax.core :refer [GET POST]]))

(enable-console-print!)

(def app-state (atom {:messages []}))

(defn add-message [r]
  (let [json (str r)
        new-msgs (cons json (:messages @app-state))]
    (swap! app-state assoc :messages new-msgs)
    )
  )

(defn login [state]
  (let [email (:email state)
        password (:password state)]
    (GET (str
           "http://localhost:3000/echo/"
           (url-encode email)
           "/"
           (url-encode password))
         {:response-format :json,
          :handler         add-message,
          :error-handler   add-message})))

(defn stripe [text className]
  (dom/div nil
           (dom/label #js {:className className} text)))

(defn handle-change [e owner key]
  (om/set-state! owner key (.. e -target -value)))

(defn login-view [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:email    ""
       :password ""})
    om/IRenderState
    (render-state [this state]
      (dom/div #js {:className "content"}
               (dom/div #js {:className "pure-form"}
                        (dom/fieldset nil
                                      (dom/legend nil "Login")
                                      (dom/input #js {:type "text" :ref "email" :placeholder "Email" :value (:email state) :onChange #(handle-change % owner :email)})
                                      (dom/input #js {:type "password" :ref "password" :placeholder "Password" :value (:password state) :onChange #(handle-change % owner :password)})
                                      (dom/button #js {:className "pure-button pure-button-primary" :onClick #(login state)} "Login"))
                        (apply dom/div #js {:className "messages"}
                               (map stripe (:messages app) (cycle ["stripe-light" "stripe-dark"]))))))))

(om/root login-view app-state
         {:target (. js/document (getElementById "login-page"))})