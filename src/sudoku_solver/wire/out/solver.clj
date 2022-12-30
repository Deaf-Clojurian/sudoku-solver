(ns sudoku-solver.wire.out.solver
  (:require
   [schema.core :as s]
   [sudoku-solver.wire.common :as wire.common]))

(s/defschema MatrixResult wire.common/skeleton-io)
