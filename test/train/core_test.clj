(ns train.core-test
  (:require [clojure.test :refer :all]
            [train.core :as train]))

(deftest test-train-path-print
  (testing "A rough test"
    (letfn [(gen [s]
              (for [x s y (next (shuffle (disj s x)))] (str x y (rand-nth [6 6 8 9 7 9 5]))))]
      (train/train-path-print (apply str (gen #{"A" "B" "C" "D" "E" })))
      (is (= 1 1)))))

(deftest test-explore-n-paths
  (testing "Should retrun nil when graph is empty"
    (nil? (train/explore-n-paths [] 1 "A")))

  (testing "Should retrun nil when src is not in the graph"
    (nil? (train/explore-n-paths [{:src "A" :dst "B" :distance 3}] 1 "B")))

  (testing "Should retrun an empty list when n is not positive"
    (empty? (train/explore-n-paths [{:src "A" :dst "B" :distance 3}] 0 "A")))

  (testing "Should retrun a list of explored paths"

    (= '({:src "A" :dst "B" :distance 3})
       (train/explore-n-paths [{:src "A" :dst "B" :distance 3}] 1 "A")
       (train/explore-n-paths [{:src "A" :dst "B" :distance 3}] 2 "A"))))

(deftest test-parse-input-route

  (testing "Should retrun an empty list when route is empty"
    (empty? (train/parse-input-route "")))

  (testing "Should retrun a list of list"
    (= '(("A" "B") ("B" "C"))
       (train/parse-input-route "A-B-C"))))

(deftest test-parse-input-graph

  (testing "Should retrun an empty list when graph str is empty"
    (empty? (train/parse-input-graph "")))

  (testing "Should retrun a list of list"
    (= '({:src "A" :dst "B" :distance 5})
       (train/parse-input-graph "AB5"))))
