(ns obj-utils.core
  (:require [c3kit.apron.corec :as ccc]
            [c3kit.apron.schema :as s]
            [clojure.string :as str]))

(defn- comment? [line]
  (str/starts-with? line "#"))

(defn <-idx-group [group]
  (let [[v vt vn] (map s/->int (str/split group #"/"))]
    (cond-> {:v v}
            vt (assoc :vt vt)
            vn (assoc :vn vn))))

(defn <-values [type rest]
  (if (= :f type)
    (vec (map <-idx-group rest))
    (vec (map s/->float rest))))

(defn- <-line [line]
  (let [[type & rest] (str/split (str/trim line) #"\s+")
        type (keyword type)]
    [type (<-values type rest)]))

(defn parse
  "Parses a wavefront .obj formatted string to a Clojure map."
  [obj-str]
  (let [lines (->> (str/split-lines obj-str)
                   (filter (comp not (some-fn str/blank? comment?))))]
    (reduce (fn [spec [type val]]
              (update spec type (comp vec conj) val))
            {} (map <-line lines))))