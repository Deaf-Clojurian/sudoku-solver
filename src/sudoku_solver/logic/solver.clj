(ns sudoku-solver.logic.solver
  (:require
   [clojure.set :as set]
   [schema.core :as s]
   [sudoku-solver.common :as common]
   [sudoku-solver.wire.in.solver :as wire.in.solver]))

(s/def sudoku-ref (atom {}))

(s/defn retrieve-val
  [quadrant :- s/Keyword
   value-of :- s/Keyword]
  (some-> (filter #(= quadrant (:quadrant %)) @sudoku-ref)
          first
          (get-in [:values value-of])))

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
  [{matrix-quadrant :matrix value-pos :value} :- (s/pred map?)]
  (retrieve-val matrix-quadrant value-pos))

(s/defn common-values-all-lines :- #{s/Int}
  [quadrant :- s/Keyword
   quadrant-pos :- s/Keyword]
  (reduce set/union (map #(-> (map lines-vals %) set (disj nil)) (cross-lines-from-pos quadrant quadrant-pos))))

(s/defn inject-sets
  [quadrant :- s/Keyword
   [quadrant-pos value] :- '(s/Keyword s/Any)]
  (if (nil? value)
    {quadrant-pos (crude-invert-fill (common-values-all-lines quadrant quadrant-pos))}
    {quadrant-pos value}))

(s/defn fill-nil :- wire.in.solver/MatrixSolving
  [sudoku-matrix :- wire.in.solver/Matrix]
  (map (fn [{:keys [quadrant values]}]
         {:quadrant quadrant :values (into {} (map #(inject-sets quadrant %) (partition 2 (reduce into [] values))))}) sudoku-matrix))

(s/defn replace-unique
  [quadrant :- s/Keyword
   quadrant-pos :- s/Keyword
   value :- s/Any
   {matrix-quadrant :matrix value-pos :value} :- (s/pred map?)]
  (or (and (= quadrant-pos value-pos)
           (= quadrant matrix-quadrant))
      (let [retrieved-val (retrieve-val matrix-quadrant value-pos)]
        (if (set? retrieved-val)
          (not (contains? retrieved-val value))
          (not= retrieved-val value)))))

(s/defn remove-val-from-cell-sets
  [at-least-number :- s/Int
   quadrant :- s/Keyword
   quadrant-pos :- s/Keyword]
  (doseq [{:keys [matrix value]} (cross-lines-from-pos quadrant quadrant-pos)]
    (let [retrieved-val (retrieve-val matrix value)]
      (if (set? retrieved-val)
        (swap! sudoku-ref #(assoc-in % quadrant-pos at-least-number))))))

(s/defn override-unique
  [quadrant :- s/Keyword
   [quadrant-pos value] :- '(s/Keyword s/Any)]
  (if (set? value)
    (let [at-least-number (first (filter (fn [value-pos]
                                           (reduce #(and %1 %2) (map (partial replace-unique quadrant quadrant-pos value-pos) (cross-lines-from-pos quadrant quadrant-pos)))) value))]
      (if (int? at-least-number)
        (do
          (remove-val-from-cell-sets at-least-number quadrant quadrant-pos)
          {quadrant-pos at-least-number})
        {quadrant-pos value}))

    {quadrant-pos value}))

(s/defn uniqued :- wire.in.solver/MatrixSolving
  [sudoku-matrix :- wire.in.solver/MatrixSolving]
  (reset! sudoku-ref sudoku-matrix)
  (map (fn [{:keys [quadrant values]}]
         {:quadrant quadrant :values (into {} (map #(override-unique quadrant %) (partition 2 (reduce into [] values))))})
       @sudoku-ref))

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