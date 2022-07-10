(ns sudoku-solver.controllers.solver
  (:require [schema.core :as s]
            [sudoku-solver.wire.out.solver :as wire.out.solver]
            [sudoku-solver.wire.in.solver :as wire.in.solver]))

(s/defn fill :- wire.out.solver/MatrixResult
  [input :- wire.in.solver/MatrixInput]
  (let [sudoku-matrix (sudoku-solver.adapters.solver/->matrix input)
        sudoku-grid (atom sudoku-matrix)]))
