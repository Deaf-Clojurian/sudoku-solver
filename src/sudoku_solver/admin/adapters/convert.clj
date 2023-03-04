(ns sudoku-solver.admin.adapters.convert
  (:require [schema.core :as s]))


(s/defn linear->standard :- sudoku-solver.wire.in.solver/MatrixInput
  "Convert linear to the output of standard - a vector of vectors of squares"
  [input :- [(s/maybe s/Int)]]
  (->> input
       (partition 9)
       (map vec)
       vec))
