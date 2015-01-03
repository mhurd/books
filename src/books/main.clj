 (ns books.main
  (:require
   [org.httpkit.server :refer [run-server]]
   [books.views.main :refer [say]]
   [books.views.login :refer [login-page]]
   [books.views.index :refer [index-page]]
   [books.schedule.scheduler :as scheduler]
   [books.handler :as handler]
   [clojure.tools.logging :as log]
   [clojurewerkz.quartzite.scheduler :as qs])
  (:gen-class))

 (defn -main [& args]
  (let [access-key (first args)
        associate-tag (second args)
        secret (second (rest args))
        handler (handler/get-handler access-key associate-tag secret)
        scheduler (-> (qs/initialize) qs/start)]
   (log/info "Starting main...")
   (scheduler/init-jobs scheduler)
   (run-server handler {:port 3000})))
