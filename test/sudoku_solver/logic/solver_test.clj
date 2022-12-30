(ns sudoku-solver.logic.solver-test
  (:require
   [clojure.test :refer :all]
   [matcher-combinators.test :refer [match?]]
   [sudoku-solver.logic.solver :as logic.solver]))

(deftest fill-nil-test
  (testing "Fill nil into possible values"
    (is (match? '({:quadrant :00
                   :values   {:01 #{4 5} :12 #{7 4 9 5} :11 #{4 9 5} :10 6 :21 #{4 9 8} :22 #{7 1 4 9 8} :20 2 :00 #{1 4 5} :02 3}}
                  {:quadrant :10
                   :values   {:01 1 :12 #{4 6 2 5 8} :11 7 :10 3 :21 #{4 6 2 5 8} :22 #{4 6 2 5 8} :20 #{4 5 8} :00 9 :02 #{4 2 5 8}}}
                  {:quadrant :20
                   :values   {:01 #{4 6 3 9 5 8} :12 #{7 1 4 2 8} :11 #{4 3 2 8} :10 #{7 1 4 8} :21 #{4 6 3 2 9} :22 #{7 1 4 6 2 9} :20 #{7 1 4} :00 #{7 4 5 8} :02 #{7 4 6 9 5 8}}}
                  {:quadrant :01
                   :values   {:01 #{1 4 6 2} :12 3 :11 #{7 4 2} :10 #{7 4 2 9 5} :21 #{7 1 4 6} :22 #{7 1 4 6} :20 #{7 4 9} :00 8 :02 #{1 4 6 2 5}}}
                  {:quadrant :11
                   :values   {:01 #{4 3 2} :12 #{1 4 6 2 5 8} :11 9 :10 #{4 2 5} :21 #{7 4 6 3 2} :22 #{7 4 6 2 5 8} :20 #{7 4 3 2 5} :00 #{4 3 2 5} :02 #{4 2 5 8}}}
                  {:quadrant :21
                   :values   {:01 #{7 4 3} :12 9 :11 5 :10 6 :21 8 :22 #{7 4 2} :20 #{7 4 3 2} :00 1 :02 #{7 4}}}
                  {:quadrant :02
                   :values   {:01 9 :12 1 :11 #{4 2} :10 8 :21 #{4 6 3} :22 #{4 6 3} :20 5 :00 #{4 2} :02 7}}
                  {:quadrant :12
                   :values   {:01 7 :12 #{4 5 8} :11 #{4 2 8} :10 #{4 2} :21 #{4 3 2 8} :22 #{4 3 9 5 8} :20 1 :00 6 :02 #{4 3 5 8}}}
                  {:quadrant :22
                   :values   {:01 #{4 6 3 8} :12 #{4 3 8} :11 #{1 4 3 8} :10 #{7 4 3} :21 5 :22 #{4 6 3 9} :20 #{7 4 3 9} :00 #{7 4 3 9} :02 2}})
                (logic.solver/fill-nil [{:quadrant :00 :values {:01 nil :12 nil :11 nil :10 6 :21 nil :22 nil :20 2 :00 nil :02 3}}
                                        {:quadrant :10 :values {:01 1 :12 nil :11 7 :10 3 :21 nil :22 nil :20 nil :00 9 :02 nil}}
                                        {:quadrant :20 :values {:01 nil :12 nil :11 nil :10 nil :21 nil :22 nil :20 nil :00 nil :02 nil}}
                                        {:quadrant :01 :values {:01 nil :12 3 :11 nil :10 nil :21 nil :22 nil :20 nil :00 8 :02 nil}}
                                        {:quadrant :11 :values {:01 nil :12 nil :11 9 :10 nil :21 nil :22 nil :20 nil :00 nil :02 nil}}
                                        {:quadrant :21 :values {:01 nil :12 9 :11 5 :10 6 :21 8 :22 nil :20 nil :00 1 :02 nil}}
                                        {:quadrant :02 :values {:01 9 :12 1 :11 nil :10 8 :21 nil :22 nil :20 5 :00 nil :02 7}}
                                        {:quadrant :12 :values {:01 7 :12 nil :11 nil :10 nil :21 nil :22 nil :20 1 :00 6 :02 nil}}
                                        {:quadrant :22 :values {:01 nil :12 nil :11 nil :10 nil :21 5 :22 nil :20 nil :00 nil :02 2}}])))))