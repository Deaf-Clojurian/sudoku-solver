(ns sudoku-solver.admin.adapters.convert-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [matcher-combinators.test :refer [match?]]
    [sudoku-solver.fixture.input :as fixture.input]
    [sudoku-solver.logic.solver :as logic.solver]))

(deftest linear->standard-test
  (testing "All filled"
    (is (match?
                (logic.solver/pick-value fixture.input/plain-json-incomplete 0 0))))
  (testing "To solve"
    (is (match? " "
                (logic.solver/pick-value fixture.input/plain-json-incomplete 2 5)))))

