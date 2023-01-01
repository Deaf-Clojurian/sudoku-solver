(ns sudoku-solver.logic.solver-test
  (:require
   [clojure.test :refer :all]
   [matcher-combinators.test :refer [match?]]
   [sudoku-solver.logic.solver :as logic.solver]
   [sudoku-solver.fixture :as fixture]))

(deftest pick-value-test
  (testing "Get 2"
    (is (match? 2
                (logic.solver/pick-value fixture/plain-json-incomplete 0 0))))
  (testing "Get empty"
    (is (match? " "
                (logic.solver/pick-value fixture/plain-json-incomplete 2 5)))))
