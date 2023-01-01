(ns sudoku-solver.adapters.solver
  (:require
   [clojure.string :as string]
   [schema.core :as s]
   [sudoku-solver.models.solver :as models.solver]
   [sudoku-solver.wire.in.solver :as wire.in.solver]
   [sudoku-solver.wire.out.solver :as wire.out.solver]))

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

(s/defn ->singularity :- wire.out.solver/MatrixResult
  "It adapts back to the vec way to show result"
  [matrix :- models.solver/MatrixSolving]
  (prn matrix)
  #_(vec
     (partition 3 (for [qx (range-3 0)
                        qy (range-3 0)
                        :let [q (keyword (str qx qy))]]
                    q)))                                   ;TODO: logic it!

  [[(-> matrix (nth 0) :values :00) (-> matrix (nth 0) :values :01) (-> matrix (nth 0) :values :02) (-> matrix (nth 3) :values :00) (-> matrix (nth 3) :values :01) (-> matrix (nth 3) :values :02) (-> matrix (nth 6) :values :00) (-> matrix (nth 6) :values :01) (-> matrix (nth 6) :values :02)]
   [(-> matrix (nth 0) :values :10) (-> matrix (nth 0) :values :11) (-> matrix (nth 0) :values :12) (-> matrix (nth 3) :values :10) (-> matrix (nth 3) :values :11) (-> matrix (nth 3) :values :12) (-> matrix (nth 6) :values :10) (-> matrix (nth 6) :values :11) (-> matrix (nth 6) :values :12)]
   [(-> matrix (nth 0) :values :20) (-> matrix (nth 0) :values :21) (-> matrix (nth 0) :values :22) (-> matrix (nth 3) :values :20) (-> matrix (nth 3) :values :21) (-> matrix (nth 3) :values :22) (-> matrix (nth 6) :values :20) (-> matrix (nth 6) :values :21) (-> matrix (nth 6) :values :22)]
   [(-> matrix (nth 1) :values :00) (-> matrix (nth 1) :values :01) (-> matrix (nth 1) :values :02) (-> matrix (nth 4) :values :00) (-> matrix (nth 4) :values :01) (-> matrix (nth 4) :values :02) (-> matrix (nth 7) :values :00) (-> matrix (nth 7) :values :01) (-> matrix (nth 7) :values :02)]
   [(-> matrix (nth 1) :values :10) (-> matrix (nth 1) :values :11) (-> matrix (nth 1) :values :12) (-> matrix (nth 4) :values :10) (-> matrix (nth 4) :values :11) (-> matrix (nth 4) :values :12) (-> matrix (nth 7) :values :10) (-> matrix (nth 7) :values :11) (-> matrix (nth 7) :values :12)]
   [(-> matrix (nth 1) :values :20) (-> matrix (nth 1) :values :21) (-> matrix (nth 1) :values :22) (-> matrix (nth 4) :values :20) (-> matrix (nth 4) :values :21) (-> matrix (nth 4) :values :22) (-> matrix (nth 7) :values :20) (-> matrix (nth 7) :values :21) (-> matrix (nth 7) :values :22)]
   [(-> matrix (nth 2) :values :00) (-> matrix (nth 2) :values :01) (-> matrix (nth 2) :values :02) (-> matrix (nth 5) :values :00) (-> matrix (nth 5) :values :01) (-> matrix (nth 5) :values :02) (-> matrix (nth 8) :values :00) (-> matrix (nth 8) :values :01) (-> matrix (nth 8) :values :02)]
   [(-> matrix (nth 2) :values :10) (-> matrix (nth 2) :values :11) (-> matrix (nth 2) :values :12) (-> matrix (nth 5) :values :10) (-> matrix (nth 5) :values :11) (-> matrix (nth 5) :values :12) (-> matrix (nth 8) :values :10) (-> matrix (nth 8) :values :11) (-> matrix (nth 8) :values :12)]
   [(-> matrix (nth 2) :values :20) (-> matrix (nth 2) :values :21) (-> matrix (nth 2) :values :22) (-> matrix (nth 5) :values :20) (-> matrix (nth 5) :values :21) (-> matrix (nth 5) :values :22) (-> matrix (nth 8) :values :20) (-> matrix (nth 8) :values :21) (-> matrix (nth 8) :values :22)]])
