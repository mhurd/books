(ns books.schedule.scheduler
  (:require [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.calendar-interval :refer [schedule with-interval-in-hours]]
            [books.mongo.mongo-repository :refer [get-books]]
            [books.amazon.amazon-client :refer [find-offer-summary-by-isbn]]
            [clojure.tools.logging :as log]))

;; updates (map #(do
;; (Thread/sleep 1000)
;; (find-offer-summary-by-isbn @access-key @associate-tag @secret (:isbn %)) books

(defjob UpdateBookDetailsJob
        [ctx]
        (log/info "Updating book details..."))

(defn init-jobs [scheduler]
  (let [job (j/build
              (j/of-type UpdateBookDetailsJob)
              (j/with-identity (j/key "jobs.update-books")))
        trigger (t/build
                  (t/with-identity (t/key "triggers.12-hours"))
                  (t/start-now)
                  (t/with-schedule (schedule
                                     (with-interval-in-hours 12))))]
    (log/info "Initialising scheduled jobs...")
    (qs/schedule scheduler job trigger)))