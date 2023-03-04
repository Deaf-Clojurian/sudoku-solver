(ns sudoku-solver.adapters.convert
  (:require
   [schema.core :as s]
   [sudoku-solver.wire.in.solver :as wire.in.solver]))

(s/defn linear->standard :- wire.in.solver/MatrixInput
  "Convert linear to the output of standard - a vector of vectors of squares"
  [input :- [(s/maybe s/Int)]]
  (->> input
       (partition 9)
       (map vec)
       vec))

(s/defn standard->linear :- [(s/maybe s/Int)]
  "Convert back to linear froms standard"
  [input :- wire.in.solver/MatrixInput]
  (-> input
      flatten
      vec))
