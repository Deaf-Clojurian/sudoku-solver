(ns sudoku-solver.adapters.convert-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [matcher-combinators.test :refer [match?]]
   [sudoku-solver.adapters.convert :as adapters.convert]
   [sudoku-solver.fixture.input :as fixture.input]))

(deftest linear->standard-test
  (testing "Into standard"
    (is (match? fixture.input/incoming-input-linear-easy-converted
                (adapters.convert/linear->standard fixture.input/incoming-input-linear-easy)))))

(deftest standard->linear-test
  (testing "Into linear"
    (is (match? fixture.input/incoming-input-linear-easy
                (adapters.convert/standard->linear fixture.input/incoming-input-linear-easy-converted)))))

