(ns sudoku-solver.controllers.solver
  (:require [schema.core :as s]
            [sudoku-solver.wire.out.solver :as wire.out.solver]
            [sudoku-solver.adapters.solver :as adapters.solver]
            [sudoku-solver.logic.solver :as logic.solver]
            [sudoku-solver.wire.in.solver :as wire.in.solver]))

(s/defn fill :- wire.out.solver/MatrixResult
  [input :- wire.in.solver/MatrixInput]
  (let [sudoku-matrix (adapters.solver/->matrix input)
        sudoku-fill (logic.solver/fill-nil sudoku-matrix)
        sudoku-grid (atom sudoku-fill)]
    (clojure.pprint/pprint @sudoku-grid)))
