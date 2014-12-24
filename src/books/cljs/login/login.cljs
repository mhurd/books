(ns login
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
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

(defn stripe [text className]
  ;; usage: (map stripe (:messages app) (cycle ["stripe-light" "stripe-dark"]))
  (dom/div nil
           (dom/label #js {:className className} text)))

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
      {:email    ""
       :password ""})
    om/IRenderState
    (render-state [this state]
      (dom/div #js {:className "content"}
               (dom/div #js {:className "pure-form"}
                        (dom/fieldset nil
                                      (dom/legend nil "Login")
                                      (dom/input #js {:type "text" :ref "email" :placeholder "Email" :value (:email state) :onChange #(handle-change % owner :email state)})
                                      (dom/input #js {:type "password" :ref "password" :placeholder "Password" :value (:password state) :onChange #(handle-change % owner :password state)})
                                      (dom/button #js {:className "pure-button pure-button-primary" :onClick #(login state)} "Login"))
                        (dom/div #js {:id "message" :className "messages"}
                               (dom/label nil (:message app))))))))

(om/root login-view app-state
         {:target (. js/document (getElementById "login-page"))})