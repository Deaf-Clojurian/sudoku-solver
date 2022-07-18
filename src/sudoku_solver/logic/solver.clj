(ns sudoku-solver.logic.solver
  (:require [schema.core :as s]
            [sudoku-solver.wire.in.solver :as wire.in.solver]
            [sudoku-solver.common :as common]
            [clojure.set :as set]))

(s/defn crude-invert-fill :- #{s/Int}
  [values :- #{s/Int}]
  (let [values-without-nil (filter #(not (nil? %)) values)]
    (set (for [missing-number (range 1 10)
               :when (not (some #{missing-number} values-without-nil))]
           missing-number))))

(s/defn cross-lines-from-pos :- '(map?)
  [quadrant :- s/Keyword
   quadrant-pos :- s/Keyword]
  (filter #(first (filter (fn [{:keys [matrix value]}] (and (= value quadrant-pos) (= matrix quadrant))) %)) common/all-traverses))

(s/defn lines-vals :- (s/maybe s/Int)
  [sudoku-matrix :- (s/pred map?)
   {matrix-quadrant :matrix value-pos :value} :- (s/pred map?)]
  (some-> (filter (fn [{:keys [quadrant]}] (= quadrant matrix-quadrant)) sudoku-matrix)
          first
          :values
          (get value-pos)))

(s/defn common-values-all-lines :- #{s/Int}
  [quadrant :- s/Keyword
   quadrant-pos :- s/Keyword
   sudoku-matrix :- (s/pred map?)]
  (reduce set/union (map #(-> (map (partial lines-vals sudoku-matrix) %) set (disj nil)) (cross-lines-from-pos quadrant quadrant-pos))))

(s/defn inject-sets
  [quadrant :- s/Keyword
   [quadrant-pos value] :- '(s/Keyword s/Any)
   sudoku-matrix :- (s/pred map?)]
  (if (nil? value)
    {quadrant-pos (crude-invert-fill (common-values-all-lines quadrant quadrant-pos sudoku-matrix))}
    {quadrant-pos value}))

(s/defn fill-nil :- wire.in.solver/MatrixSolving
  [sudoku-matrix :- wire.in.solver/Matrix]
  (map (fn [{:keys [quadrant values]}]
         {:quadrant quadrant :values (into {} (map #(inject-sets quadrant % sudoku-matrix) (partition 2 (reduce into [] values))))}) sudoku-matrix))

(s/defn replace-unique
  [quadrant :- s/Keyword
   quadrant-pos :- s/Keyword
   value :- s/Any
   sudoku-matrix :- wire.in.solver/MatrixSolving
   {matrix-quadrant :matrix value-pos :value} :- (s/pred map?)]
  (if (set? value)
    (if (and (= quadrant-pos value-pos) (= quadrant matrix-quadrant))
    (reduce #(and %1 %2) ())))))

(s/defn override-unique :- wire.in.solver/MatrixSolving
  [quadrant :- s/Keyword
   [quadrant-pos value] :- '(s/Keyword s/Any)
   sudoku-matrix :- (s/pred map?)]
  (map #(-> (map (partial replace-unique quadrant quadrant-pos value sudoku-matrix) %)) (cross-lines-from-pos quadrant quadrant-pos)))

(s/defn uniqued :- wire.in.solver/MatrixSolving
  [sudoku-matrix :- wire.in.solver/MatrixSolving]
  (map (fn [{:keys [quadrant values]}]
         {:quadrant quadrant :values (into {} (map #(override-unique quadrant % sudoku-matrix) (partition 2 (reduce into [] values))))}) sudoku-matrix)
  )

;[{:quadrant :00
;  :values   {:00 2 :01 1 :02 9
;             :10 5 :11 4 :12 3
;             :20 8 :21 7 :22 6}}
; {:quadrant :01
;  :values   {:00 5 :01 4 :02 3
;             :10 8 :11 7 :12 6
;             :20 2 :21 1 :22 9}}
; {:quadrant :02
;  :values   {:00 6 :01 7 :02 8
;             :10 9 :11 1 :12 2
;             :20 3 :21 4 :22 5}}
; {:quadrant :10
;  :values   {:00 4 :01 3 :02 2
;             :10 7 :11 6 :12 5
;             :20 1 :21 9 :22 8}}
; {:quadrant :11
;  :values   {:00 7 :01 6 :02 5
;             :10 1 :11 9 :12 8
;             :20 4 :21 3 :22 2}}
; {:quadrant :12
;  :values   {:00 8 :01 9 :02 1
;             :10 2 :11 3 :12 4
;             :20 5 :21 6 :22 7}}
; {:quadrant :20
;  :values   {:00 3 :01 2 :02 1
;             :10 6 :11 5 :12 4
;             :20 9 :21 8 :22 7}}
; {:quadrant :21
;  :values   {:00 6 :01 5 :02 4
;             :10 9 :11 8 :12 7
;             :20 3 :21 2 :22 1}}
; {:quadrant :22
;  :values   {:00 7 :01 8 :02 9
;             :10 1 :11 2 :12 3
;             :20 4 :21 5 :22 6}}]