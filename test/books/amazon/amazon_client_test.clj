(ns books.amazon.amazon-client-test
  (:require [clojure.test :refer :all]
            [books.amazon.amazon-client :refer :all]))

(deftest test-secret-key-spec
  (testing "secret key spec memoization"
    (is (identical? (secret-key-spec "foo") (secret-key-spec "foo"))) ;; request for a secret key spec for the same string should return the same object
    (is (not (identical? (secret-key-spec "foo") (secret-key-spec "bar")))) ;; a different secret shouldn't
    ))

(def test-basic-args
  (sorted-map
    "Service" service-name,
    "Version" api-version,
    "AWSAccessKeyId" "one",
    "AssociateTag" "two",
    "SearchIndex" "Books",
    "Condition" "All",
    "Offer" "All",
    "ResponseGroup" "ItemAttributes,OfferSummary,Images"))

(deftest test-basic-args
  (testing "basic-args memoization"
    (is (identical? (basic-args "foo" "bar") (basic-args "foo" "bar")))
    (is (not (identical? (basic-args "foo" "bar") (basic-args "bar" "foo"))))
    ))

(deftest test-percent-encode-rfc-3986
  (testing "percent-encode-rfc-3986"
    (is (= (percent-encode-rfc-3986 "one two three four") "one%20two%20three%20four"))
    ))

(deftest test-hmac
  (testing "hmac"
    (is (= (hmac (secret-key-spec "foo") "foobar") "VV9yup1tm7ln3qTeERsBUCUQO5t0QKeNELzNEPcb3Oo="))))

(deftest test-merge-and-encode-args
  (testing "merge-and-encode-args"
      (is (=
          (merge-and-encode-args "foo" "bar" (sorted-map "Key1" "value1", "Key2" "value2"))
          "AWSAccessKeyId=foo&AssociateTag=bar&Condition=All&Key1=value1&Key2=value2&Offer=All&ResponseGroup=ItemAttributes%2COfferSummary%2CImages&SearchIndex=Books&Service=AWSECommerceService&Version=2011-08-01"))))
