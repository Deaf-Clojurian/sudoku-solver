(ns sudoku-solver.adapters.solver-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [matcher-combinators.test :refer [match?]]
   [sudoku-solver.adapters.solver :as adapters.solver]
   [sudoku-solver.fixture.input :as fixture.input]))

(deftest get-values-test
  (testing "Retrieve values according to quadrant :00"
    (is (match? {:00 nil :01 4 :02 nil
                 :10 1 :11 7 :12 nil
                 :20 nil :21 nil :22 9}
                (adapters.solver/get-values :00 fixture.input/incoming-input))))

  (testing "Retrieve values according to quadrant :22"
    (is (match? {:00 3 :01 7 :02 nil
                 :10 nil :11 2 :12 nil
                 :20 9 :21 nil :22 nil}
                (adapters.solver/get-values :22 fixture.input/incoming-input)))))

(deftest ->matrix-test
  (testing "Adapted as expected"
    (is (match? [{:quadrant :00 :values {:00 nil :01 4 :02 nil :10 1 :11 7 :12 nil :20 nil :21 nil :22 9}}
                 {:quadrant :10 :values {:00 5 :01 nil :02 2 :10 nil :11 nil :12 nil :20 nil :21 nil :22 nil}}
                 {:quadrant :20 :values {:00 nil :01 nil :02 nil :10 nil :11 8 :12 nil :20 nil :21 nil :22 5}}
                 {:quadrant :01 :values {:00 nil :01 nil :02 3 :10 nil :11 nil :12 nil :20 2 :21 nil :22 nil}}
                 {:quadrant :11 :values {:00 nil :01 9 :02 nil :10 8 :11 7 :12 nil :20 nil :21 nil :22 nil}}
                 {:quadrant :21 :values {:00 nil :01 6 :02 nil :10 nil :11 3 :12 nil :20 nil :21 nil :22 8}}
                 {:quadrant :02 :values {:00 nil :01 8 :02 nil :10 nil :11 nil :12 6 :20 nil :21 nil :22 nil}}
                 {:quadrant :12 :values {:00 nil :01 nil :02 nil :10 nil :11 nil :12 nil :20 nil :21 nil :22 nil}}
                 {:quadrant :22 :values {:00 3 :01 7 :02 nil :10 nil :11 2 :12 nil :20 9 :21 nil :22 nil}}]
                (adapters.solver/->matrix fixture.input/incoming-input)))))

(deftest ->singularity-test
  (testing "Adapt to single output"
    (is (match? [[2 1 9 4 3 2 3 2 1]
                 [5 4 3 7 6 5 6 5 4]
                 [8 7 6 1 9 8 9 8 7]
                 [5 4 3 7 6 5 6 5 4]
                 [8 7 6 1 9 8 9 8 7]
                 [2 1 9 4 3 2 3 2 1]
                 [6 7 8 8 9 1 7 8 9]
                 [9 1 2 2 3 4 1 2 3]
                 [3 4 5 5 6 7 4 5 6]]
                (adapters.solver/->singularity fixture.input/sudoku-matrix-completed)))))
