(ns login
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [ajax.core :refer [GET POST]]))

(enable-console-print!)

(def app-state (atom {:user     "Please enter your username",
                      :password "",
                      :messages []}))

(defn add-message [r]
  (let [json (str r)
        new-msgs (cons json (:messages @app-state))]
    (swap! app-state assoc :messages new-msgs)
    )
  )

(defn login [state]
  (let [username (:username state)
        password (:password state)]
    (GET (str "http://localhost:3000/echo/" username "/" password) {:response-format :json, :handler add-message})))

(defn stripe [text className]
  (dom/li #js {:className className} text))

(defn handle-change [e owner key]
  (om/set-state! owner key (.. e -target -value)))

(defn login-view [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:username ""
       :password ""})
    om/IRenderState
    (render-state [this state]
      (dom/div nil
               (dom/h2 nil "Login")
               (dom/div nil
                        (dom/input #js {:className "username-input" :type "text" :ref "email"
                                        :value (:username state) :onChange #(handle-change % owner :username)})
                        (dom/input #js {:className "password-input" :type "password" :ref "password"
                                        :value (:password state) :onChange #(handle-change % owner :password)})
                        (dom/button #js {:onClick #(login state)} "Login"))
               (dom/div nil
                        (apply dom/ul #js {:className "messages"}
                               (map stripe (:messages app) (cycle ["stripe-light" "stripe-dark"]))))))))

(om/root login-view app-state
         {:target (. js/document (getElementById "login-page"))})