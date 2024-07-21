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
  (case type
    :f (vec (map <-idx-group rest))
    :g nil
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
              (if (and type val)
                (update spec type (comp vec conj) val)
                spec))
            {} (map <-line lines))))

(defn- sort-verts [faces]
  (->> (flatten faces)
       (reduce (fn [verts {:keys [v vt vn]}]
                 (assoc verts v {:vt vt :vn vn})) {})
       (into (sorted-map))
       vals))

(defn- <-attrib [slices attribs key]
  (-> (map #(nth attribs (dec (get % key))) slices)
      flatten))

(defn align-idxs
  "Prepares obj components for loading into a VBO"
  [{:keys [v vt vn f]}]
  (let [slices (sort-verts f)]
    (cond->
      {:vertices (apply concat v)
       :idxs     (mapcat (partial map (comp dec :v)) f)}
      vt (assoc :tex-coords (<-attrib slices vt :vt))
      vn (assoc :normals (<-attrib slices vn :vn)))))