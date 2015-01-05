(ns books.mongo.mongo-repository
  (:require [monger.core :as m]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [clojure.tools.logging :as log]))

(defn get-books []
  "Gets all the books from the mongo DB as a seq of maps"
  (let [conn (m/connect)
        db (m/get-db conn "library")
        coll "books"
        results (mc/find-maps db coll {})]
    (seq (map #(assoc % :_id (.toString (get % :_id))) results)))) ;; we have to change the _id to something that Transit can serialise

(defn update-offer [db coll offer]
  (log/info (str "Updating offer summary for ASIN: " (:asin offer)))
  (mc/update db coll {:asin (:asin offer)} {$set {:lastPriceUpdateTimestamp (System/currentTimeMillis)
                                                  :lowestNewPrice (:lowestNewPrice offer)
                                                  :lowestPrice (:lowestPrice offer)
                                                  :lowestUsedPrice (:lowestUsedPrice offer)
                                                  :totalAvailable (:totalAvailable offer)
                                                  :totalNew (:totalNew offer)
                                                  :totalUsed (:totalUsed offer)}})
  )

(defn update-offers [offer-summaries]
  (let [conn (m/connect)
        db (m/get-db conn "library")
        coll "books"]
    (log/info "Updating offers...")
    (doseq [summary offer-summaries] (update-offer db coll summary))
  ))