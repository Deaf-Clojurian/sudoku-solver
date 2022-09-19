(ns sudoku-solver.controllers.verifiers
  (:require [schema.core :as s]
            [sudoku-solver.adapters.solver :as adapters.solver]
            [sudoku-solver.logic.verifier :as logic.verifier]
            [sudoku-solver.wire.in.verifier :as wire.in.verifier]))

(s/defn check :- s/Keyword
  [map :- wire.in.verifier/Matrix]
  (let [sudoku-matrix (adapters.solver/->matrix map)]
    (cond
      (logic.verifier/invalid-values? sudoku-matrix) :invalid-values
      (logic.verifier/collision? sudoku-matrix) :invalid-position
      (logic.verifier/correct-solution? sudoku-matrix) :correct
      :else :incorrect)))
