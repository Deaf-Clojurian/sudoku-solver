(ns sudoku-solver.controllers.verifiers
  (:require [schema.core :as s]
            [sudoku-solver.logic.verifier :as logic.verifier]
            [sudoku-solver.wire.in.verifier :as wire.in.verifier]))

(s/defn check :- s/Keyword
  [map :- wire.in.verifier/Matrix]
  (cond
    (logic.verifier/invalid-values? map) :invalid-values
    (logic.verifier/collision? map) :invalid-position
    (logic.verifier/correct-solution? map) :correct
    :else :ooops))
