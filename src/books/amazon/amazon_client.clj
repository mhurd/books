(ns books.amazon.amazon-client
  (:import (java.text SimpleDateFormat)
           (java.util Base64 Date)
           (javax.crypto.spec SecretKeySpec)
           (javax.crypto Mac)
           (java.net URLEncoder))
  (:require [org.httpkit.client :as http-kit]
            [clojure.tools.logging :as log]))

;; Amazon API Constants
(def api-version "2011-08-01")
(def service-name "AWSECommerceService")
(def api-host "ecs.amazonaws.co.uk")
(def api-url "/onca/xml")

(def sha-256 "HmacSHA256")
(def utf8-charset "UTF-8")
(def base-64-encoder (. Base64 getEncoder))
(def iso-8601-timestamp-formatter (SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss'Z'"))

(def secret-key-spec
  "Memoized version of the SecretKeySpec"
  (memoize (fn [secret]
             "Creates a secret key spec by sha-256-ing the supplied secret"
             (log/info (str "Creating a SecretKeySpec from secret: " secret))
             (SecretKeySpec. (. secret getBytes utf8-charset) sha-256))))

(def mac
  "Memoized verison of the MAC"
  (memoize (fn [secret-key-spec]
             "Creates a message authentication code initialised with the supplied SecretKeySpec"
             (let [mac-instance (. Mac getInstance sha-256)]
               (. mac-instance init secret-key-spec)
               mac-instance
               ))))

(def basic-args
  (memoize (fn [accessKey associateTag]
             (sorted-map
               "Service" service-name,
               "Version" api-version,
               "AWSAccessKeyId" accessKey,
               "AssociateTag" associateTag,
               "SearchIndex" "Books",
               "Condition" "All",
               "Offer" "All",
               "ResponseGroup" "ItemAttributes,OfferSummary,Images"))))

(defn format-iso-8601-timestamp [date]
  (. iso-8601-timestamp-formatter format date)
  )

(defn current-iso-8601-timestamp []
  (format-iso-8601-timestamp (Date.)))

(defn percent-encode-rfc-3986 [s]
  "Encodes the URL additionally encoding '+' to spaces,
  See http://stackoverflow.com/questions/2678551/when-to-encode-space-to-plus-or-20"
  (clojure.string/replace (. URLEncoder encode s utf8-charset) "+" "%20")
  )

(defn hmac [secret-key-spec to-encode]
  "Creates a hashed message authentication code from the supplied
  SecretKeySpec (holds the secret) and the string to encode. Its base-64 encoded 
  to save space (both hex encoding and base64 will turn a hash into a valid ASCII string. 
  However, a hex string (where bytes are each represented as two ASCII characters between 0 and F) 
  will take twice as much space as the original, while the base64 version will only 
  take four thirds as much space. A hex-encoded SHA-256 is 64 bytes, while a 
  base64-encoded SHA-256 is more or less 43 bytes)"
  (let [bytes (. to-encode getBytes utf8-charset)
        rawHmac (. (mac secret-key-spec) doFinal bytes)
        encoded (. base-64-encoder encode rawHmac)]
    (String. encoded)
    )
  )

(defn merge-and-encode-args
  "Take the access key and associate tag and the map of sorted args and merge them
  all together, URL encoded"
  [access-key associate-tag args]
  (let [merged (merge (basic-args access-key associate-tag) args)
        encoded-args (map (fn [[k, v]] (str (percent-encode-rfc-3986 k) "=" (percent-encode-rfc-3986 v))) merged)]
    (apply str (interpose "&" encoded-args)))
  )

(defn merge-and-encode-args-with-timestamp [access-key associate-tag args]
  "Take the access key and associate tag and the map of sorted args and merge them
  all together, URL encoded and add a Timestamp arg (current date in ISO 8601 format)"
  (merge-and-encode-args access-key associate-tag (merge args (sorted-map "Timestamp" (current-iso-8601-timestamp)))))

(defn create-signed-url [access-key associate-tag secret args]
  (let [merged (merge-and-encode-args-with-timestamp access-key associate-tag args)
        to-sign (str "GET\n" api-host "\n" api-url "\n" merged)
        hmac-result (hmac (secret-key-spec secret) to-sign)
        sig (percent-encode-rfc-3986 hmac-result)]
    (str api-url "?" merged "&Signature=" sig)
    )
  )

(defn find-on-amazon [access-key associate-tag secret args]
  (let [response (http-kit/get (str "https://" api-host (create-signed-url access-key associate-tag secret args)))]
    (clojure.string/replace (:body @response) "<?xml version=\"1.0\" ?>" "")
    ))

(defn find-by-isbn [access-key associate-tag secret isbn]
  (find-on-amazon access-key associate-tag secret (sorted-map "Operation" "ItemLookup" "ItemId" isbn "IdType" "ISBN"))
  )

(defn find-offer-summary-by-isbn [access-key associate-tag secret isbn]
  (find-on-amazon access-key associate-tag secret (sorted-map "ResponseGroup" "OfferSummary" "Operation" "ItemLookup" "ItemId" isbn "IdType" "ISBN"))
  )
