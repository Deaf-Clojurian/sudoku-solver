(ns sudoku-solver.logic.verifier-test
  (:require [clojure.test :refer :all]
            [sudoku-solver.fixture.input :as fixture.input]
            [sudoku-solver.logic.verifier :refer [invalid-values?]]))



(deftest invalid-values?-test
  (testing "Valid values"
    (is (= false (invalid-values? fixture.input/sudoku-matrix-valid))))

  (testing "Invalid values"
    (is (= true (invalid-values? fixture.input/sudoku-matrix-invalid)))))
