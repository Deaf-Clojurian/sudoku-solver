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
                (adapters.input/positioned-vector->wire fixture.input/positioned-input))))

  (testing "Adapt positioned input to standard input - starting a game"
    (is (match? [[nil nil 5 nil nil nil 7 nil nil]
                 [8 1 6 nil 4 7 nil 2 nil]
                 [7 nil nil nil 6 5 nil nil 1]
                 [6 5 1 8 2 9 nil nil 3]
                 [4 7 nil nil 3 nil 9 6 nil]
                 [nil nil 2 6 7 4 nil nil nil]
                 [3 8 nil nil 4 2 nil 9 7]
                 [nil nil nil nil 9 3 6 8 1]
                 [nil nil nil 8 5 nil 4 nil nil]]
                (adapters.input/positioned-vector->wire fixture.input/positioned-input-new-game)))))
