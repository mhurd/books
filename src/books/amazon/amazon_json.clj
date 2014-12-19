(ns amazon.amazon-json
  (:use [clojure.zip :only (xml-zip)]
        [clojure.data.xml :only (parse-str)]
        [clojure.data.zip.xml :only (attr text xml->)]))

(defn to-json [amazon-xml]
  (let [doc (parse-str amazon-xml)
        root (xml-zip doc)
        content (->> root
                     xml-seq
                     (filter #(= (:tag %) "DetailPageURL"))
                     )]
    (println (str "content: " (doall content)))
))