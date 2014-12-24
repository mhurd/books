(ns books.amazon.amazon-json
  (:require [clojure.zip :refer (xml-zip)]
        [clojure.data.json :refer (write-str)]
        [clojure.data.xml :refer (parse-str)]
        [clojure.data.zip.xml :refer (text xml1->)]
        [cemerick.url :refer [url-decode]]))

(defn String->Number [str]
  (if (nil? str)
    nil
    (let [n (read-string str)]
      (if (number? n) n nil))))

(defn get-item-element [root]
  (xml1-> root :Items :Item))

(defn get-asin [item-element]
  (xml1-> item-element :ASIN text)
  )

(defn get-detail-page-url [item-element]
  (url-decode (xml1-> item-element :DetailPageURL text))
  )

(defn get-author [item-element]
  (xml1-> item-element :ItemAttributes :Author text)
  )

(defn get-binding [item-element]
  (xml1-> item-element :ItemAttributes :Binding text)
  )

(defn get-ean [item-element]
  (xml1-> item-element :ItemAttributes :EAN text)
  )

(defn get-format [item-element]
  (xml1-> item-element :ItemAttributes :Format text)
  )

(defn get-edition [item-element]
  (xml1-> item-element :ItemAttributes :Edition text)
  )

(defn get-isbn [item-element]
  (xml1-> item-element :ItemAttributes :ISBN text)
  )

(defn get-list-price [item-element]
  (String->Number (xml1-> item-element :ItemAttributes :ListPrice :Amount text))
  )

(defn get-number-of-pages [item-element]
  (String->Number (xml1-> item-element :ItemAttributes :NumberOfPages text))
  )

(defn get-title [item-element]
  (xml1-> item-element :ItemAttributes :Title text)
  )

(defn get-lowest-new-price [item-element]
  (String->Number (xml1-> item-element :OfferSummary :LowestNewPrice :Amount text))
  )

(defn get-lowest-used-price [item-element]
  (String->Number (xml1-> item-element :OfferSummary :LowestUsedPrice :Amount text))
  )

(defn get-total-new [item-element]
  (String->Number (xml1-> item-element :OfferSummary :TotalNew text))
  )

(defn get-total-used [item-element]
  (String->Number (xml1-> item-element :OfferSummary :TotalUsed text))
  )

(defn get-small-image [item-element]
  (url-decode (xml1-> item-element :SmallImage :URL text))
  )

(defn get-medium-image [item-element]
  (url-decode (xml1-> item-element :MediumImage :URL text))
  )

(defn get-large-image [item-element]
  (url-decode (xml1-> item-element :LargeImage :URL text))
  )

(defn get-publisher [item-element]
  (xml1-> item-element :ItemAttributes :Publisher text)
  )

(defn get-publication-date [item-element]
  (xml1-> item-element :ItemAttributes :PublicationDate text)
  )

(defn safe-min [x y]
  (if-not (or x y) ;; both nil
    nil
    (let [sx (or x Integer/MAX_VALUE)
          sy (or y Integer/MAX_VALUE)]
      (min sx sy))
    )
  )

(defn to-map [amazon-xml]
  (let [doc (parse-str amazon-xml)
        root (xml-zip doc)
        item (get-item-element root)]
    (sorted-map
     :asin (get-asin item),
     :authors (get-author item),
     :binding (get-binding item),
     :amazonPageUrl (get-detail-page-url item),
     :ean (get-ean item),
     :edition (get-edition item),
     :format (get-format item),
     :isbn (get-isbn item),
     :listPrice (get-list-price item),
     :lowestPrice (safe-min (get-lowest-new-price item) (get-lowest-used-price item)), ;; compatibility
     :lowestNewPrice (get-lowest-new-price item),
     :lowestUsedPrice (get-lowest-used-price item),
     :numberOfPages (get-number-of-pages item),
     :title (get-title item),
     :totalAvailable (+ (get-total-new item) (get-total-used item))
     :totalNew (get-total-new item),
     :totalUsed (get-total-used item),
     :smallImage (get-small-image item),
     :mediumImage (get-medium-image item),
     :largeImage (get-large-image item),
     :smallBookCover (get-medium-image item),
     :largeBookCover (get-large-image item),
     :lastPriceUpdateTimestamp (System/currentTimeMillis),
     :publisher (get-publisher item),
     :publicationDate (get-publication-date item)))
  )

(defn to-json [amazon-xml]
  (let [jmap (to-map amazon-xml)]
    (write-str jmap)
    ))