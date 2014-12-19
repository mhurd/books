(ns books.amazon.amazon-json-test
  (:require [clojure.test :refer :all]
            [books.amazon.amazon-client :refer :all]
            [amazon.amazon-json :refer :all]))

(deftest test-convert
  (testing "convert XML to JSON"
    (is (= (to-json
             (find-by-isbn
               "AKIAIUZ3CEXC5EF5RJUQ"
               "fleetiphotobl-20"
               "kvQ8Z4ov0WBYHT3yk30ZZSZE9cF144pl+yDMVjNI"
               "3775727388"))  ""
           ))))