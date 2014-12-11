(ns login
  (:require [clojure.browser.repl]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(def app-state (atom {:user "Please enter your username"
                      :password ""}))

(defn login [app owner]
  (let [email (-> (om/get-node owner "email")
                        .-value)
        password (-> (om/get-node owner "password")
                     .-value)]
  (println (str "Email: " email ", Password: " password))))

(defn login-view [app owner]
  (reify
    om/IRenderState
    (render-state [this state]
      (dom/div nil
               (dom/h2 nil "Login")
               (dom/div nil
                        (dom/input #js {:className "username-input" :type "text" :ref "email"})
                        (dom/input #js {:className "password-input":type "password" :ref "password"})
                        (dom/button #js {:onClick #(login app owner)} "Login"))))))

(om/root login-view app-state
  {:target (. js/document (getElementById "login-page"))})