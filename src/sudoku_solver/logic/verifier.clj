(ns sudoku-solver.logic.verifier
  (:require [schema.core :as s]
            [sudoku-solver.common :as common]
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
              :values
              (map (fn [[_ v]] (valid-value? v)))
              (reduce (fn [p1 p2] (and p1 p2)))) m)
      count
      (< 9)))

(s/defn collision? :- s/Bool
  [m :- wire.in.verifier/Matrix]
  )

(s/defn correct-solution? :- s/Bool
  [m :- wire.in.verifier/Matrix]
  (mapv #(= 9 (-> (for [matrix m
                        quadrant %
                        :when (= (:quadrant matrix) (:matrix quadrant))]
                    (get (:values matrix) (:value quadrant)))
                  set
                  count)) common/all-traverses))

#_{:quadrant :21
   :values   {:00 nil :01 nil :02 1
              :10 nil :11 4 :12 nil
              :20 nil :21 nil :22 nil}}