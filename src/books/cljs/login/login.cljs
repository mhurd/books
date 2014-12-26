(ns login
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [ajax.core :refer [GET POST]]
            [cemerick.url :refer [url-encode]]
            [jayq.core :refer [$ fade-in fade-out]]))

(enable-console-print!)

(def app-state (atom {:message ""}))

(defn set-message [msg]
  (let [s (str msg)]
    (swap! app-state assoc :message s)
    (-> ($ :#message)
        (.stop)
        (fade-in 1)
        (fade-out 5000)))
  )

(defn nil-or-empty? [s]
  (or (nil? s) (= "" s)))

(defn login [state]
  (let [email (:email state)
        password (:password state)]
    (if (or (nil-or-empty? email) (nil-or-empty? password))
      (set-message "Email or Password cannot be empty!")
      (GET (str
             "http://localhost:3000/echo/"
             (url-encode email)
             "/"
             (url-encode password))
           {:response-format :json,
            :handler         set-message,
            :error-handler   set-message}))))

(defn stripe [text class-name]
  ;; usage: (map stripe (:messages app) (cycle ["stripe-light" "stripe-dark"]))
  (html/html [:dom
              [:label {:class class-name} text]]))

(defn handle-change [e owner key state]
  (let [value (.. e -target -value)]
    (if-not (re-find #"[%\"\\\\]" value)
      (om/set-state! owner key value)
      (do
        (set-message "Cannot use the following characters: % \" \\")
        (om/set-state! owner key (key state))
      )
    )
  ))

(defn login-view [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:email "",
       :password ""})
    om/IRenderState
    (render-state [this state]
      (html/html
        [:div {:class "content"}
         [:div {:class "pure-form"}
          [:fieldset
           [:legend "login"]
           [:input {:type "text" :ref "email" :placeholder "Email" :value (:email state) :on-change #(handle-change % owner :email state)}]
           [:input {:type "password" :ref "password" :placeholder "Password" :value (:password state) :on-change #(handle-change % owner :password state)}]
           [:button {:class "pure-button pure-button-primary" :on-click #(login state)} "Login"]
           ]
          [:div {:id "message" :class "messages"}
           [:label (:message app)]]]]
        ))))

(om/root login-view app-state
         {:target (. js/document (getElementById "login-page"))})