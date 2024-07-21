(ns obj-utils.core
  (:require [c3kit.apron.schema :as s]
            [clojure.string :as str]))

(defn- comment? [line]
  (str/starts-with? line "#"))

(defn- <-line [line]
  (let [[type & rest] (str/split line #" ")]
    [(keyword type) (vec (map s/->float rest))]))

(defn parse [obj-str]
  (let [lines (->> (str/split-lines obj-str)
                   (filter (comp not comment?))
                   (filter (comp not str/blank?)))]
    (reduce (fn [spec [type val]]
              (update spec type (comp vec conj) val))
            {} (map <-line lines))))