(ns sudoku-solver.wire.in.solver
  (:require
   [schema.core :as s]
   [sudoku-solver.wire.common :as wire.common]))

(s/defschema MatrixInput wire.common/skeleton-io)

(s/defschema MatrixSolving wire.common/skeleton-solving)
