(ns sudoku-solver.adapters.solver
  (:require [schema.core :as s]
            [sudoku-solver.wire.in.solver :as wire.in.solver]))

(s/defn ->matrix :- wire.in.solver/Matrix
  [input :- wire.in.solver/MatrixInput]
  )