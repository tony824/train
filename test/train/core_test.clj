(ns train.core-test
  (:require [clojure.test :refer :all]
            [train.core :refer :all]))

(deftest a-test
  (testing "A rough test"
    (letfn [(gen [s]
              (for [x s y (next (shuffle (disj s x)))] (str x y (rand-nth [6 6 8 9 7 9 5]))))]
      (train-print (apply str (gen #{"A" "B" "C" "D" "E" })))
      (is (= 1 1)))))
