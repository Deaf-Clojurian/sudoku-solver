(ns sudoku-solver.adapters.input-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [matcher-combinators.test :refer [match?]]
   [sudoku-solver.adapters.input :as adapters.input]
   [sudoku-solver.fixture.input :as fixture.input]))

(deftest positioned-vector->wire-test
  (testing "Adapt positioned input to standard input"
    (is (match? [[4 2 7 1 5 6 3 9 8]
                 [9 8 5 7 3 2 6 1 4]
                 [3 1 6 4 8 9 7 2 5]
                 [7 4 8 3 9 1 5 6 2]
                 [1 5 2 8 6 4 9 3 7]
                 [6 9 3 2 7 5 4 8 1]
                 [2 3 1 9 4 7 8 5 6]
                 [5 7 9 6 1 8 2 4 3]
                 [8 6 4 5 2 3 1 7 9]]
                (adapters.input/positioned-vector->wire fixture.input/positioned-input)))))
  
