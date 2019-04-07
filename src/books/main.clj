 (ns books.main
   (:require
    [org.httpkit.server :refer [run-server]]
    [books.schedule.scheduler :as scheduler]
    [books.handler :as handler]
    [clojure.tools.logging :as log]
    [clojurewerkz.quartzite.scheduler :as qs])
   (:gen-class))

(defn -main [& args]
  (let [access-key (first args)
        associate-tag (second args)
        secret (second (rest args))
        api-sleep (Integer/parseInt (second (rest (rest args))))
        handler (handler/get-handler access-key associate-tag secret api-sleep)
        scheduler (-> (qs/initialize) qs/start)]
    (log/info "Starting main...")
    (scheduler/init-jobs scheduler access-key associate-tag secret api-sleep)
    (run-server handler {:port 3000})))
