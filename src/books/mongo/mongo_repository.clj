(ns books.mongo.mongo-repository
  (:require [monger.core :as m]
            [monger.collection :as mc]))

(defn get-books []
  "Gets all the books from the mongo DB as a seq of maps"
  (let [conn (m/connect)
        db (m/get-db conn "library")
        coll "books"
        results (mc/find-maps db coll {})]
    (seq (map #(assoc % :_id (.toString (get % :_id))) results)))) ;; we have to change the _id to something that Transit can serialise
