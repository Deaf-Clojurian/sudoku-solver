(ns sudoku-solver.wire.in.verifier
  (:require
   [schema.core :as s]
   [sudoku-solver.wire.common :as wire.common]))

(s/defschema Matrix wire.common/skeleton-internal)
