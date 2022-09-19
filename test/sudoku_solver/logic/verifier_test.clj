(ns sudoku-solver.logic.verifier-test
  (:require [clojure.test :refer :all]
            [sudoku-solver.fixture.input :as fixture.input]
            [sudoku-solver.logic.verifier :refer [invalid-values? correct-solution?]]))


(deftest invalid-values?-test
  (testing "Valid values"
    (is (= false (invalid-values? fixture.input/sudoku-matrix-valid))))

  (testing "Invalid values"
    (is (= true (invalid-values? fixture.input/sudoku-matrix-invalid)))))

(deftest correct-solution?-test
  (testing "Correct solution"
    (is (= true (correct-solution? fixture.input/sudoku-matrix-completed))))

  (testing "Incorrect solution - wrong values"
    (is (= false (correct-solution? fixture.input/sudoku-matrix-completed-but-incorrect))))

  (testing "Incorrect solution - incomplete"
    (is (= false (correct-solution? fixture.input/sudoku-matrix-incomplete)))))
