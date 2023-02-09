(ns sudoku-solver.adapters.input 
  (:require [schema.core :as s]
            [sudoku-solver.wire.in.solver :as wire.in.solver]))

(s/defn positioned-vector->wire :- wire.in.solver/MatrixInput
  [input] 
  )