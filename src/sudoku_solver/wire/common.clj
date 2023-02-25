(ns sudoku-solver.wire.common
  (:require
   [schema.core :as s]))

(def xy-pos-enum #{:00 :01 :02
                   :10 :11 :12
                   :20 :21 :22})

(s/defschema XYPos (apply s/enum xy-pos-enum))

(def skeleton-internal
  {:quadrant XYPos
   :values   {XYPos (s/maybe s/Int)}})

(def skeleton-solving
  {:quadrant XYPos
   :values   (s/conditional map? {XYPos s/Int}
                            :else #{s/Int})})

(def skeleton-io
  [[(s/maybe s/Int)]])

(def skeleton-universal-io
  [[(s/conditional set? [s/Int]
                   :else (s/maybe s/Int))]])
