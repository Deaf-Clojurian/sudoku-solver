(ns sudoku-solver.controllers.solver
  (:require
   [schema.core :as s]
   [sudoku-solver.adapters.solver :as adapters.solver]
   [sudoku-solver.logic.solver :as logic.solver]
   [sudoku-solver.wire.in.solver :as wire.in.solver]
   [sudoku-solver.wire.out.solver :as wire.out.solver]))

(def louco (atom 9))

(s/defn fill :- wire.out.solver/MatrixResult
  "This function 'attempts' to fill the square with the solution"
  [input :- wire.in.solver/MatrixInput]
  (let [_ (println "Louco: " @louco)
        sudoku-fill (-> input
                        adapters.solver/->matrix
                        logic.solver/fill-nil
                        logic.solver/uniqued)

        sudoku-grid (atom sudoku-fill)]
    (clojure.pprint/pprint @sudoku-grid)))


