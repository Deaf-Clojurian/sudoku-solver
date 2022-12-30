(ns sudoku-solver.adapters.solver
  (:require
   [clojure.string :as string]
   [schema.core :as s]
   [sudoku-solver.models.solver :as models.solver]
   [sudoku-solver.wire.in.solver :as wire.in.solver]))

(s/defn derange-3 :- s/Int
  [pos :- s/Int
   i :- s/Int]
  (- pos (* i 3)))

(s/defn range-3 :- (s/pred coll?)
  [i :- s/Int]
  (let [offset-i (* i 3)]
    (range offset-i (+ offset-i 3))))

(s/defn get-values :- (s/pred map?)
  [quadrant :- s/Keyword
   input :- wire.in.solver/MatrixInput]
  (let [[y x] (as-> (name quadrant) $
                (string/split $ #"")
                (map #(Integer/parseInt %) $))]

    (into {}
          (for [posY (range-3 y)
                posX (range-3 x)]
            {(keyword (str (derange-3 posY y) (derange-3 posX x))) (-> (nth input posY) (nth posX))}))))

(s/defn ->matrix :- models.solver/Matrix
  "Adapts to an internal model to then work over this to (try) to find result"
  [input :- wire.in.solver/MatrixInput]
  (vec
   (flatten
    (for [qx (range-3 0)
          qy (range-3 0)
          :let [q (keyword (str qy qx))]]
      {:quadrant q
       :values   (get-values q input)}))))
