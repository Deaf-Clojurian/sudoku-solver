(ns sudoku-solver.logic.verifier
  (:require [schema.core :as s]
            [sudoku-solver.wire.in.verifier :as wire.in.verifier]))


(s/defn invalid-values? :- s/Bool
  [m :- wire.in.verifier/Matrix]
  (-> (filter
        #(->> %
              :fixed-values
              (map (fn [[_ v]]
                     (reduce (fn [p1 p2]
                               (or p1 p2))
                             (for [r (conj (range 1 10) nil)]
                               (= r v)))))
              (reduce (fn [p1 p2] (and p1 p2)))) m)
      count
      (not= 9)))


#_{:quadrant     :21
   :fixed-values {:00 nil :01 nil :02 1
                  :10 nil :11 4 :12 nil
                  :20 nil :21 nil :22 nil}}