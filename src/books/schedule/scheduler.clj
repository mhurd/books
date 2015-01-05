(ns books.schedule.scheduler
  (:require [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.conversion :as qc]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.calendar-interval :refer [schedule with-interval-in-hours]]
            [books.mongo.mongo-repository :refer [get-books update-offers]]
            [books.amazon.amazon-client :refer [find-offer-summary-by-isbn]]
            [books.amazon.amazon-json :refer [offer-summary-to-map]]
            [clojure.tools.logging :as log]))

(defjob UpdateBookDetailsJob [ctx]
        (let [context (qc/from-job-data ctx)
              access-key (get context "access-key")
              associate-tag (get context "associate-tag")
              secret (get context "secret")
              books (get-books)
              asins (map #(:asin %) books)
              offer-summaries (map #(do
                                     (Thread/sleep 1000)    ;; Amazon complains if you fire API calls too fast
                                     (offer-summary-to-map (find-offer-summary-by-isbn access-key associate-tag secret %))) asins)]
          (update-offers offer-summaries)
          )
        )

(defn init-jobs [scheduler access-key associate-tag secret]
  (let [job (j/build
              (j/of-type UpdateBookDetailsJob)
              (j/using-job-data {"access-key" access-key,
                                 "associate-tag" associate-tag,
                                 "secret" secret})
              (j/with-identity (j/key "jobs.update-books")))
        trigger (t/build
                  (t/with-identity (t/key "triggers.12-hours"))
                  (t/start-now)
                  (t/with-schedule (schedule
                                     (with-interval-in-hours 12))))]
    (log/info "Initialising scheduled jobs...")
    (qs/schedule scheduler job trigger)))