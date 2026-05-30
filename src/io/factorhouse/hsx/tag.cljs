(ns io.factorhouse.hsx.tag
  "Adapted from https://github.com/reagent-project/reagent/blob/master/src/reagent/impl/template.cljs"
  (:require [clojure.string :as str]
            [goog.object :as obj]))

;; From Weavejester's Hiccup, via pump:
(def ^{:doc "Regular expression that parses a CSS-style id and class
             from a tag name."}
  re-tag
  #"([^\s\.#]+)((?:[#\.][^\s\.#]+)*)")

(def ^{:doc "Regular expression that parses each id or class segment from a tag name suffix."}
  re-tag-segment
  #"([#\.])([^\s\.#]+)")

(defonce tag-name-cache
  #js {})

(defn cache-get
  [^js o k]
  (let [v (goog.object/get o k)]
    (when-not (undefined? v) v)))

(defn- parse-tag
  [hiccup-tag]
  (let [tag-name (str/replace (name hiccup-tag) ".#" "#")
        tag-name (if (contains? #{\. \#} (first tag-name))
                   (str "div" tag-name)
                   tag-name)
        [tag suffix] (->> tag-name (re-matches re-tag) next)
        segments (map rest (re-seq re-tag-segment (or suffix "")))
        id (some (fn [[kind value]]
                   (when (= kind "#")
                     value))
                 segments)
        className (->> segments
                       (keep (fn [[kind value]]
                               (when (= kind ".")
                                 value)))
                       (str/join " ")
                       not-empty)]
    {:tag       (str tag)
     :id        id
     :className className}))

(defn cached-parse
  [hiccup-tag]
  (if-some [s (cache-get tag-name-cache hiccup-tag)]
    s
    (let [v (parse-tag hiccup-tag)]
      (obj/set tag-name-cache hiccup-tag v)
      v)))
