(ns sudoku-solver.models.solver
  (:require
   [schema.core :as s]
   [sudoku-solver.wire.common :as wire.common]))

(s/defschema Matrix wire.common/skeleton-internal)
