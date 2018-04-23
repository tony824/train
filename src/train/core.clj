(ns train.core
  (:require [clojure.string :as clj-str]))

(defn path-destination
  "Path: list of [distance :src :dst], for instance ([5 :A :B] [4 :B :C] [8 :C :D] "
  [path]
  (->> path
       last
       last))

(defn paths-from
  [graph src]
  "Paths: a list of path to save all possible paths, for instance (([8 :C :D]) ([2 :C :E]))"
  (->> graph
       (filter #(= src (second %)))
       (map list)))

(defn move
  "Append all possiable next paths for a path"
  [graph path]
  (->> path
       path-destination
       (paths-from graph)
       (map (partial concat path))))

(defn- explore-paths
  "Explore paths for a src"
  [graph src]
  (let [src-set (set (map second graph))]
    (when (src-set src)
      (->> src
           (paths-from graph)
           (iterate #(mapcat (partial move graph) %))
           (mapcat identity)))))

(defn parse-input-path
  "Parse input path: A-B-C"
  [input]
  (->> input
       (re-seq #"\w")
       (map keyword)
       (partition 2 1)))

(defn check
  "Compare two lists, check src and dst between input path and explored path"
  [input path]
  (= (mapcat identity input)
     (mapcat rest path)))

(defn match-input-path
  "Match input path in explored paths"
  [graph input]
  (let [in (parse-input-path input)
        in-stops (count in)]
    (->> (ffirst in)
         (explore-paths graph)
         (take-while #(>= in-stops (count %)))
         (filter (partial check in))
         first)))

(defn distance
  "Calculate the distance of a path"
  [path]
  (->> path
       (map first)
       (reduce +)))

(defn show-path
  [path]
  (if (seq path)
    (distance path)
    "NO SUCH ROUTE"))

(defn max-n-stops
  [graph src dst n]
  (let [dst-set (set (map last graph))]
    (when (dst-set dst)
      (->> (explore-paths graph src)
           (take-while #(>= n (count %)))
           (filter #(= dst (path-destination %)))
           count))))

(defn exact-n-stops
  [graph src dst n]
  (let [dst-set (set (map last graph))]
    (when (dst-set dst)
      (->> (explore-paths graph src)
           (take-while #(>= n (count %)))
           (filter #(and (= n (count %))
                         (= dst (path-destination %))))
           count))))

(defn shortest-path
  "Worst scenario: we need n-1 stops to reach dst"
  [graph src dst]
  (let [dst-set (set (map last graph))
        n (count graph)]
    (when (dst-set dst)
      (->> (explore-paths graph src)
           (take-while #(> n (count %)))
           (filter #(= dst (path-destination %)))
           first))))

(defn less-than-n
  "The sum of distance is less than n
   When having max-count stops, the distance will be greater than n"
  [graph src dst n]
  (let [dst-set (set (map last graph))]
    (when (dst-set dst)
      (let [min-distance (apply min (map first graph))
            max-count (inc (quot n min-distance))]
        (->> (explore-paths graph src)
             (take-while #(> max-count (count %)))
             (filter #(and (> n (distance %))
                           (= dst (path-destination %))))
             count)))))

(defn parse-input
  "Convert input to list"
  [input]
  (letfn [(process [s]
            (let [l (map str s)
                  n (Integer/parseInt (apply str (nnext l)))]
              (->> l
                   (take 2)
                   (map (comp keyword clj-str/upper-case))
                   (cons n))))]
    (->> input
         (re-seq #"[a-zA-Z]{2}\d+")
         (map process))))

(defn train-print
  "INPUT Graph: AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7"
  [in]
  (let [g (parse-input in)]
    (println "Output #1:" (show-path (match-input-path g "A-B-C")))
    (println "Output #2:" (show-path (match-input-path g "A-D")))
    (println "Output #3:" (show-path (match-input-path g "A-D-C")))
    (println "Output #4:" (show-path (match-input-path g "A-E-B-C-D")))
    (println "Output #5:" (show-path (match-input-path g "A-E-D")))
    (println "Output #6:" (max-n-stops g :C :C 3))
    (println "Output #7:" (exact-n-stops g :A :C 4))
    (println "Output #8:" (show-path (shortest-path g :A :C)))
    (println "Output #9:" (show-path (shortest-path g :B :B)))
    (println "Output #10:" (less-than-n g :C :C 30))))

(defn -main [& args]
  []
  (train-print (or (first args) "Graph: AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7")))
