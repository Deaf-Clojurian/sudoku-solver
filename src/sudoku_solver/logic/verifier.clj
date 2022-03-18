(ns sudoku-solver.logic.verifier
  (:require [schema.core :as s]
            [sudoku-solver.wire.in.verifier :as wire.in.verifier]))

(s/defn valid-value? :- s/Bool
  "In a Sudoku game, valid value are between 1 to 9 and the 'null' value"
  [value :- (s/maybe s/Int)]
  (reduce #(or %1 %2)
          (for [r (conj (range 1 10) nil)]
            (= r value))))

(s/defn invalid-values? :- s/Bool
  [m :- wire.in.verifier/Matrix]
  (-> (filter
        #(->> %
              :fixed-values
              (map (fn [[_ v]] (valid-value? v)))
              (reduce (fn [p1 p2] (and p1 p2)))) m)
      count
      (< 9)))

(s/defn collision? :- s/Bool
  [m :- wire.in.verifier/Matrix]
  )
#_{:quadrant     :21
   :fixed-values {:00 nil :01 nil :02 1
                  :10 nil :11 4 :12 nil
                  :20 nil :21 nil :22 nil}}