(ns sudoku-solver.wire.in.verifier
  (:require [schema.core :as s]))

(def xy-pos-enum #{:00 :01 :02
                   :10 :11 :12
                   :20 :21 :22})

(s/defschema XYPos (apply s/enum xy-pos-enum))

(def skeleton
  {:quadrant XYPos
   :values   {XYPos (s/maybe s/Int)}})

(s/defschema Matrix skeleton)
