(ns sudoku-solver.adapters.verifier
  (:require
   [schema.core :as s]
   [sudoku-solver.models.solver :as models.solver]
   [sudoku-solver.wire.out.solver :as wire.out.solver]))

(s/defn ->singularity :- wire.out.solver/MatrixResult
  "It adapts back to the JSON way to show result"
  [matrix :- models.solver/MatrixSolving]
  (mapv #(-> % :values vals vec) matrix))
