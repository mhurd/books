(ns books.amazon.amazon-json-test
  (:require [clojure.test :refer :all]
            [books.amazon.amazon-client :refer :all]
            [amazon.amazon-json :refer :all]))

(def doc (find-by-isbn
           "AKIAIUZ3CEXC5EF5RJUQ"
           "fleetiphotobl-20"
           "kvQ8Z4ov0WBYHT3yk30ZZSZE9cF144pl+yDMVjNI"
           "1576874672"))

(deftest test-convert
  (testing "convert XML to JSON"
    (println (to-json doc))))