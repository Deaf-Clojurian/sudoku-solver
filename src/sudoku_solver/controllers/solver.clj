(ns sudoku-solver.controllers.solver
  (:require
    [clojure.set :as set]
    [schema.core :as s]
    [sudoku-solver.adapters.solver :as adapters.solver]
    [sudoku-solver.logic.solver :as logic.solver]
    [sudoku-solver.models.solver :as models.solver]
    [sudoku-solver.wire.in.solver :as wire.in.solver]
    [sudoku-solver.wire.out.solver :as wire.out.solver]))

(s/def sudoku-ref (atom {}))

(s/defn retrieve-val!
  [quadrant :- s/Keyword
   value-of :- s/Keyword]
  (some-> (filter #(= quadrant (:quadrant %)) @sudoku-ref)
          first
          (get-in [:values value-of])))


(s/defn lines-vals! :- (s/maybe s/Int)
  [{matrix-quadrant :matrix reference-pos :value} :- (s/pred map?)]
  (retrieve-val! matrix-quadrant reference-pos))

(s/defn common-values-by-three-laws! :- #{s/Int}
  "Gather all references that contain further positions throughout vertical and horizontal reference
   lines and in the quadrant, starting with a quadrant and position as point of reference"
  [quadrant :- s/Keyword
   quadrant-pos :- s/Keyword]
  (reduce set/union (map #(-> (map lines-vals! %) set (disj nil)) (logic.solver/gather-references-from-pos quadrant quadrant-pos))))


(s/defn inject-sets-with-possible-values!
  "It will check if the value is nil, then make calculation and fill the set, else
   just forward value, keeping"
  [quadrant :- s/Keyword
   [quadrant-pos value] :- '(s/Keyword s/Any)]
  (if (nil? value)
    {quadrant-pos (logic.solver/crude-invert-fill (common-values-by-three-laws! quadrant quadrant-pos))}
    {quadrant-pos value}))

(s/defn replace-nils-with-set-of-candidate-values!
  "It will fill replacing all nil values into a vector of 1 to 9 values,
   but according to whole game start sudoku that should make sense, with possible
   values to solve.

   Example (take it as a single visualization, the reality is by whole 9 quadrants):

       ---------------
       |  1   4  nil |
       | nil  6   9  |
       |  2   7   8  |
       ---------------

       into

       -----------------------
       |     1    4   [3, 5] |
       |  [3, 5]  6     9    |
       |     2    7     8    |
       -----------------------
  "
  [sudoku-matrix :- models.solver/Matrix]
  (reset! sudoku-ref sudoku-matrix)
  (reset! sudoku-ref (mapv (fn [{:keys [quadrant values]}]
                             {:quadrant quadrant :values (into {} (map #(inject-sets-with-possible-values! quadrant %) (partition 2 (logic.solver/map->vec values))))}) sudoku-matrix)))


(s/defn invalidate-sets-with-nils)
#_(s/defn replace-unique!? :- s/Bool
    [quadrant :- s/Keyword
     quadrant-pos :- s/Keyword
     value :- (s/pred set?)
     {matrix-quadrant :matrix value-pos :value} :- (s/pred map?)]
    (or (and (= quadrant-pos value-pos)
             (= quadrant matrix-quadrant))
        (let [retrieved-val (retrieve-val! matrix-quadrant value-pos)
              _ (clojure.pprint/pprint {:value value :retrieved retrieved-val})]
          (if (set? retrieved-val)
            (not= retrieved-val value)
            (not (contains? value retrieved-val))))))


#_(s/defn find-and-remove
    [sudoku-ref :- models.solver/MatrixSolving
     at-least-number :- s/Int
     quadrant :- s/Keyword
     quadrant-pos :- s/Keyword]
    (map (fn [{quadrant-ref :quadrant values-ref :values}]
           {:quadrant quadrant :values
            (if (= quadrant quadrant-ref)
              (into {} (map (fn [key-values]
                              )))
              (disj values-ref at-least-number) values-ref)}) sudoku-ref))


#_(s/defn override-unique!
    [quadrant :- s/Keyword
     [quadrant-pos value] :- '(s/Keyword s/Any)]
    (if (and (set? value) (= 1 (count value)))
      (let [exact-only-number (first (filter (fn [value-pos]
                                               (reduce #(and %1 %2) (map (partial replace-unique!? quadrant quadrant-pos value-pos) (gather-references-from-pos quadrant quadrant-pos)))) value))]
        (if (int? exact-only-number)
          (do
            (remove-val-from-cell-sets exact-only-number quadrant quadrant-pos)
            {quadrant-pos exact-only-number})
          {quadrant-pos value}))

      {quadrant-pos value}))

(s/defn remove-val-from-cell-sets!
  [at-least-number :- s/Int
   quadrant :- s/Keyword
   quadrant-pos :- s/Keyword]
  (doseq [{:keys [matrix value]} (logic.solver/gather-references-from-pos quadrant quadrant-pos)]
    (let [retrieved-val (retrieve-val! matrix value)]
      (if (set? retrieved-val)
        (swap! sudoku-ref #(logic.solver/find-and-replace % at-least-number quadrant quadrant-pos))))))



#_(s/defn uniqued :- models.solver/MatrixSolving
    [sudoku-matrix :- models.solver/MatrixSolving]
    (reset! sudoku-ref sudoku-matrix)
    (map (fn [{:keys [quadrant values]}]
           {:quadrant quadrant :values (into {} (map #(override-unique! quadrant %) (partition 2 (map->vec values))))})
         sudoku-matrix))


(s/defn solve!
  [sudoku-matrix :- models.solver/MatrixSolving]
  (let [atom-sorted-sudoku (sort-by :quadrant @sudoku-ref)
        fresh-sorted-sudoku (sort-by :quadrant sudoku-matrix)]
    (if (= atom-sorted-sudoku fresh-sorted-sudoku)
      (let [correct? (sudoku-solver.logic.verifier/correct-solution? fresh-sorted-sudoku)]
        (if correct?))
      (do
        (reset! sudoku-ref sudoku-matrix)
        ))))


(s/defn fill! :- wire.out.solver/MatrixResult
  "This function 'attempts' to solve the sudoku"
  [input :- wire.in.solver/MatrixInput]
  (-> input
      adapters.solver/->matrix
      replace-nils-with-set-of-candidate-values!
      solve!))
