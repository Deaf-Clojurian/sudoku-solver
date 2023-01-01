(ns sudoku-solver.logic.solver-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [matcher-combinators.test :refer [match?]]
   [sudoku-solver.fixture.input :as fixture.input]
   [sudoku-solver.logic.solver :as logic.solver]))

(deftest pick-value-test
  (testing "Get 2"
    (is (match? 2
                (logic.solver/pick-value fixture.input/plain-json-incomplete 0 0))))
  (testing "Get empty"
    (is (match? " "
                (logic.solver/pick-value fixture.input/plain-json-incomplete 2 5)))))
