(ns sudoku-solver.adapters.input
  (:require
    [schema.core :as s]
    [sudoku-solver.wire.in.solver :as wire.in.solver]))

(s/defn positioned-vector->wire :- wire.in.solver/MatrixInput
  [input]
  (vec (for [i (range 0 3)
             value-pos (range 0 3)
             :let [quadrant-pos (* 3 i)]]
         (vec (flatten (for [j (range 0 3)]
                         (-> input
                             (nth (+ quadrant-pos j))
                             (nth value-pos))))))))
