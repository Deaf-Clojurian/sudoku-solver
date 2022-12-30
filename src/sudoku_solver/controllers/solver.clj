(ns sudoku-solver.controllers.solver
  (:require
   [schema.core :as s]
   [sudoku-solver.adapters.solver :as adapters.solver]
   [sudoku-solver.logic.solver :as logic.solver]
   [sudoku-solver.wire.in.solver :as wire.in.solver]
   [sudoku-solver.wire.out.solver :as wire.out.solver]))

(s/defn fill :- wire.out.solver/MatrixResult
  "This function 'attempts' to solve the sudoku"
  [input :- wire.in.solver/MatrixInput]
  (-> input
      adapters.solver/->matrix
      logic.solver/fill-nil
      logic.solver/uniqued
      clojure.pprint/pprint))
