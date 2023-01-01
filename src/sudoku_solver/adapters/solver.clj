(ns sudoku-solver.adapters.solver
  (:require
   [clojure.string :as string]
   [schema.core :as s]
   [sudoku-solver.logic.solver :as logic.solver]
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

(s/defn ->prettified :- s/Str
  [matrix-result :- wire.out.solver/MatrixResult]
  (str
   "-------------------------
| " (logic.solver/pick-value matrix-result 0 0) " " (logic.solver/pick-value matrix-result 0 1) " " (logic.solver/pick-value matrix-result 0 2) " | " (logic.solver/pick-value matrix-result 0 3) " " (logic.solver/pick-value matrix-result 0 4) " " (logic.solver/pick-value matrix-result 0 5) " | " (logic.solver/pick-value matrix-result 0 6) " " (logic.solver/pick-value matrix-result 0 7) " " (logic.solver/pick-value matrix-result 0 8) " |
| " (logic.solver/pick-value matrix-result 1 0) " " (logic.solver/pick-value matrix-result 1 1) " " (logic.solver/pick-value matrix-result 1 2) " | " (logic.solver/pick-value matrix-result 1 3) " " (logic.solver/pick-value matrix-result 1 4) " " (logic.solver/pick-value matrix-result 1 5) " | " (logic.solver/pick-value matrix-result 1 6) " " (logic.solver/pick-value matrix-result 1 7) " " (logic.solver/pick-value matrix-result 1 8) " |
| " (logic.solver/pick-value matrix-result 2 0) " " (logic.solver/pick-value matrix-result 2 1) " " (logic.solver/pick-value matrix-result 2 2) " | " (logic.solver/pick-value matrix-result 2 3) " " (logic.solver/pick-value matrix-result 2 4) " " (logic.solver/pick-value matrix-result 2 5) " | " (logic.solver/pick-value matrix-result 2 6) " " (logic.solver/pick-value matrix-result 2 7) " " (logic.solver/pick-value matrix-result 2 8) " |
-------------------------
| " (logic.solver/pick-value matrix-result 3 0) " " (logic.solver/pick-value matrix-result 3 1) " " (logic.solver/pick-value matrix-result 3 2) " | " (logic.solver/pick-value matrix-result 3 3) " " (logic.solver/pick-value matrix-result 3 4) " " (logic.solver/pick-value matrix-result 3 5) " | " (logic.solver/pick-value matrix-result 3 6) " " (logic.solver/pick-value matrix-result 3 7) " " (logic.solver/pick-value matrix-result 3 8) " |
| " (logic.solver/pick-value matrix-result 4 0) " " (logic.solver/pick-value matrix-result 4 1) " " (logic.solver/pick-value matrix-result 4 2) " | " (logic.solver/pick-value matrix-result 4 3) " " (logic.solver/pick-value matrix-result 4 4) " " (logic.solver/pick-value matrix-result 4 5) " | " (logic.solver/pick-value matrix-result 4 6) " " (logic.solver/pick-value matrix-result 4 7) " " (logic.solver/pick-value matrix-result 4 8) " |
| " (logic.solver/pick-value matrix-result 5 0) " " (logic.solver/pick-value matrix-result 5 1) " " (logic.solver/pick-value matrix-result 5 2) " | " (logic.solver/pick-value matrix-result 5 3) " " (logic.solver/pick-value matrix-result 5 4) " " (logic.solver/pick-value matrix-result 5 5) " | " (logic.solver/pick-value matrix-result 5 6) " " (logic.solver/pick-value matrix-result 5 7) " " (logic.solver/pick-value matrix-result 5 8) " |
-------------------------
| " (logic.solver/pick-value matrix-result 6 0) " " (logic.solver/pick-value matrix-result 6 1) " " (logic.solver/pick-value matrix-result 6 2) " | " (logic.solver/pick-value matrix-result 6 3) " " (logic.solver/pick-value matrix-result 6 4) " " (logic.solver/pick-value matrix-result 6 5) " | " (logic.solver/pick-value matrix-result 6 6) " " (logic.solver/pick-value matrix-result 6 7) " " (logic.solver/pick-value matrix-result 6 8) " |
| " (logic.solver/pick-value matrix-result 7 0) " " (logic.solver/pick-value matrix-result 7 1) " " (logic.solver/pick-value matrix-result 7 2) " | " (logic.solver/pick-value matrix-result 7 3) " " (logic.solver/pick-value matrix-result 7 4) " " (logic.solver/pick-value matrix-result 7 5) " | " (logic.solver/pick-value matrix-result 7 6) " " (logic.solver/pick-value matrix-result 7 7) " " (logic.solver/pick-value matrix-result 7 8) " |
| " (logic.solver/pick-value matrix-result 8 0) " " (logic.solver/pick-value matrix-result 8 1) " " (logic.solver/pick-value matrix-result 8 2) " | " (logic.solver/pick-value matrix-result 8 3) " " (logic.solver/pick-value matrix-result 8 4) " " (logic.solver/pick-value matrix-result 8 5) " | " (logic.solver/pick-value matrix-result 8 6) " " (logic.solver/pick-value matrix-result 8 7) " " (logic.solver/pick-value matrix-result 8 8) " |
-------------------------"))                                ; TODO: logic it!