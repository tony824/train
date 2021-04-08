(ns train.core)

(defn path-distance
  "Calculate the distance of a path"
  [path]
  (->> path
       (map :distance)
       (reduce +)))

(defn show-path
  [path]
  (if (seq path)
    (path-distance path)
    "NO SUCH ROUTE"))

(defn path-destination
  "Return the destination of the given path.
   Path: a list of map contains keys :src, :dst and :distance,
   e.g. ({:src 'A' :dst 'B' :distance 5} {:src 'B' :dst 'C' :distance 3})"
  [path]
  (->> path
       last
       :dst))

(defn paths-from
  [graph src]
  "Given a src, return all paths that start from src, each path is a list of one map"
  (->> graph
       (filter #(= src (:src %)))
       (map list)))

(defn move
  "Start from the destination of a path, append all possible paths to the path.
   Return a list of new paths for a given path, each new path is one step further than the given path"
  [graph path]
  (->> path
       path-destination
       (paths-from graph)
       (map (partial concat path))))

(defn explore-n-paths
  "Start from src, move n steps forward and explore all paths for each step."
  [graph n src]
  (let [src-set (set (map :src graph))]
    (when (src-set src)
      (->> src
           (paths-from graph)
           (iterate #(mapcat (partial move graph) %))
           ;; iterate is returning [x, (f x), (f (f x)), ...],
           ;; x is a list of paths with one stop,
           ;; (f x) is a list of paths with 2 stops.
           (take n)
           (mapcat identity)))))

(defn parse-input-route
  "Parse input route: A-B-C, return a list of list like ((A B) (B C))"
  [route]
  (->> route
       (re-seq #"\w")
       (partition 2 1)))

(defn matching-path?
  "Check src and dst between the parsed input route and an explored path (a list of map)"
  [parsed-route path]
  (= (mapcat identity parsed-route)
     (mapcat (juxt :src :dst) path)))

(defn match-input-route
  "Match input route like A-B-C and then explore paths"
  [graph input-route]
  (let [parsed-route (parse-input-route input-route)
        stops        (count parsed-route)]
    (->> (ffirst parsed-route)
         (explore-n-paths graph stops)
         (filter (partial matching-path? parsed-route))
         first)))

(defn max-n-stops
  "Given a srt and a dst, return the number of paths who have up to n stops"
  [graph src dst n]
  (let [dst-set (set (map :dst graph))]
    (when (dst-set dst)
      (->> (explore-n-paths graph n src)
           (filter #(= dst (path-destination %)))
           count))))

(defn exact-n-stops
  "Given a srt and a dst, return the number of paths who have exact n stops"
  [graph src dst n]
  (let [dst-set (set (map :dst graph))]
    (when (dst-set dst)
      (->> (explore-n-paths graph n src)
           (filter #(and (= n (count %))
                         (= dst (path-destination %))))
           count))))

(defn shortest-path
  "Given a srt and a dst, return the shortest path from src to dst.
   The worst scenario: we need n-1 stops to reach dst"
  [graph src dst]
  (let [dst-set (set (map :dst graph))
        n       (count graph)]
    (when (dst-set dst)
      (->> (explore-n-paths graph (dec n) src)
           (filter #(= dst (path-destination %)))
           first))))

(defn less-than-n
  "Given a src, a dst and a maximum distance max-path-distance,
   return the number of paths whose distance is less than max-distance"
  [graph src dst max-path-distance]
  (let [dst-set (set (map :dst graph))]
    (when (dst-set dst)
      (let [min-distance (apply min (map :distance graph))
            ;; when having max-stops, the distance will be greater than n
            max-stops    (quot max-path-distance min-distance)]
        (->> (explore-n-paths graph max-stops src)
             (filter #(and (> max-path-distance (path-distance %))
                           (= dst (path-destination %))))
             count)))))

(defn parse-input-graph
  "Convert input to a list of map like {:src 'A' :dst 'B' :distance 5}"
  [graph-str]
  (->> graph-str
       (re-seq #"[a-zA-Z]{2}\d+")
       (map (fn [s]
              {:src      (-> s first str)
               :dst      (-> s second str)
               :distance (-> s last str Integer/parseInt)}))))

(defn train-path-print
  "INPUT Graph: AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7"
  [graph-str]
  (let [g (parse-input-graph graph-str)]
    (println "Output #1:"  (show-path (match-input-route g "A-B-C")))
    (println "Output #2:"  (show-path (match-input-route g "A-D")))
    (println "Output #3:"  (show-path (match-input-route g "A-D-C")))
    (println "Output #4:"  (show-path (match-input-route g "A-E-B-C-D")))
    (println "Output #5:"  (show-path (match-input-route g "A-E-D")))
    (println "Output #6:"  (max-n-stops g "C" "C" 3))
    (println "Output #7:"  (exact-n-stops g "A" "C" 4))
    (println "Output #8:"  (show-path (shortest-path g "A" "C")))
    (println "Output #9:"  (show-path (shortest-path g "B" "B")))
    (println "Output #10:" (less-than-n g "C" "C" 30))))

(defn -main [& args]
  []

  ;; path:  ({:src 'A' :dst 'B' :distance 5} {:src 'B' :dst 'C' :distance 3})
  ;; route: A-B-C
  (train-path-print (or (first args) "Graph: AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7")))
