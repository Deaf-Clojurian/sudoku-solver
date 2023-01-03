(ns sudoku-solver.controllers.solver
  (:require
    [clojure.set :as set]
    [schema.core :as s]
    [sudoku-solver.adapters.solver :as adapters.solver]
    [sudoku-solver.logic.solver :as logic.solver]
    [sudoku-solver.models.solver :as models.solver]
    [sudoku-solver.wire.in.solver :as wire.in.solver]
    [sudoku-solver.wire.out.solver :as wire.out.solver])
  (:import (clojure.lang Fn)))

(s/def sudoku-ref (atom {}))

(s/defn ^:private retrieve-val!
  [quadrant :- s/Keyword
   value-of :- s/Keyword]
  (some-> (filter #(= quadrant (:quadrant %)) @sudoku-ref)
          first
          (get-in [:values value-of])))

(s/defn ^:private lines-vals! :- (s/maybe s/Int)
  [{matrix-quadrant :matrix reference-pos :value} :- (s/pred map?)]
  (retrieve-val! matrix-quadrant reference-pos))

(s/defn ^:private common-values-by-three-laws! :- #{s/Int}
  "Gather all references that contain further positions throughout vertical and horizontal reference
   lines and in the quadrant, starting with a quadrant and position as point of reference"
  [quadrant :- s/Keyword
   quadrant-pos :- s/Keyword]
  (reduce set/union (map #(-> (map lines-vals! %) set (disj nil)) (logic.solver/gather-references-from-pos quadrant quadrant-pos))))

(s/defn ^:private inject-sets-with-possible-values!
  "It will check if the value is nil, then make calculation and fill the set, else
   just forward value, keeping"
  [quadrant :- s/Keyword
   [quadrant-pos value] :- '(s/Keyword s/Any)]
  {quadrant-pos (if (nil? value)
                  (logic.solver/crude-invert-fill (common-values-by-three-laws! quadrant quadrant-pos))
                  value)})


(s/defn frequent-in-one-law?! :- s/Bool
  "This function returns true if the number appeared more than once in a direction (horizontal, vertical or quadrant).
   If just once, will return false. The 'once' is when the reference is collided of one position, then false, and others,
   remains false. If just one true, it invalidates and is considered as 'frequent'"
  [unique-number :- s/Int
   quadrant :- s/Keyword
   quadrant-pos :- s/Keyword
   one-law-direction :- (s/pred map?)]
  (let [debug (for [{matrix-ref :matrix value-ref :value} one-law-direction]
                (if (and (= matrix-ref quadrant)
                         (= value-ref quadrant-pos))
                  false
                  (let [retrieved-val (retrieve-val! matrix-ref value-ref)]
                    (if (set? retrieved-val)
                      (contains? retrieved-val unique-number)
                      (= retrieved-val unique-number)))))]
    (reduce #(or %1 %2) debug)))


(s/defn at-least-one-law-unique?! :- s/Bool
  "This functions checks if at least once by one of direction - horizontal line, vertical line or quadrant - the number
   appeared just once - inside a set or the number itself"
  [unique-number :- s/Int
   quadrant :- s/Keyword
   quadrant-pos :- s/Keyword
   set-of-values :- (s/pred set?)]
  (and (contains? set-of-values unique-number)
       (not (reduce #(and %1 %2) (map #(frequent-in-one-law?! unique-number quadrant quadrant-pos %) (logic.solver/gather-references-from-pos quadrant quadrant-pos))))))

(s/defn replace-if-unique! :- s/Any
  "It will replace if in one of law the number appeared just once, then it's considered
   the response for this specified position, filling, else, just forwards, keeping"
  [quadrant :- s/Keyword
   quadrant-pos :- s/Keyword
   set-of-values :- (s/pred set?)]
  (if-let [promoted-number (first (for [unique-number (range 1 10)
                                        :when (at-least-one-law-unique?! unique-number quadrant quadrant-pos set-of-values)]
                                    unique-number))]
    promoted-number
    set-of-values))


(s/defn ^:private inject-one-occurrence-value!
  "It will check if the value is nil, then make calculation and fill the set, else
   just forward value, keeping"
  [quadrant :- s/Keyword
   [quadrant-pos value] :- '(s/Keyword s/Any)]
  {quadrant-pos (if (set? value)
                  (replace-if-unique! quadrant quadrant-pos value)
                  value)})


(s/defn ^:private inject-nil-on-sets
  "It will check if the value is a set, then replace it to nil, else
   just forward value, keeping"
  [[quadrant-pos value] :- '(s/Keyword s/Any)]
  {quadrant-pos (if (set? value)
                  nil
                  value)})

(s/defn ^:private replace-set-to-content!
  "It will verify if the value is a set and has only one value,
  then replace with the content itself, else let it as is"
  [[quadrant-pos value] :- '(s/Keyword s/Any)]
  {quadrant-pos (if (and (set? value) (= 1 (count value))) (first value) value)})

(s/defn ^:private replace-nils-with-set-of-candidate-values!
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
  " []
  (swap! sudoku-ref (fn [sudoku-matrix]
                      (mapv (fn [{:keys [quadrant values]}]
                              (let [replenish-candidate-values (into {} (map #(inject-sets-with-possible-values! quadrant %) (partition 2 (logic.solver/map->vec values))))]
                                {:quadrant quadrant :values replenish-candidate-values})) sudoku-matrix))))

(s/defn invalidate-sets-with-nils
  "It will invalidate all sets, setting back to nil values

   Example (take it as a single visualization, the reality is by whole 9 quadrants):

       ----------------------
       |    1    4   [3, 5] |
       | [3, 5]  6     9    |
       |    2    7     8    |
       ----------------------

       into

       -----------------------
       |     1    4    nil   |
       |    nil   6     9    |
       |     2    7     8    |
       -----------------------
  "
  []
  (swap! sudoku-ref (fn [sudoku-matrix]
                      (mapv (fn [{:keys [quadrant values]}]
                              {:quadrant quadrant :values (into {} (map #(inject-nil-on-sets %) (partition 2 (logic.solver/map->vec values))))}) sudoku-matrix))))

#_(s/defn find-and-remove
    [sudoku-ref :- models.solver/MatrixSolving
     at-least-number :- s/Int
     quadrant :- s/Keyword
     quadrant-pos :- s/Keyword]
    (map (fn [{quadrant-ref :quadrant values-ref :values}]
           {:quadrant quadrant :values
            (if (= quadrant quadrant-ref)
              (into {} (map (fn [key-values])))
              (disj values-ref at-least-number) values-ref)}) sudoku-ref))

(s/defn replace-one-sized-sets-to-its-content!
  "If, after replacing all nil'ed to set of values, we get a set with a unique value,
   so the set must become the number of content itself. Example:
  ----------------------
  |     1    4   [3]   |
  |     5    6    9    |
  |     2    7    8    |
  ----------------------

  will become as:

  ----------------------
  |     1    4    3    |
  |     5    6    9    |
  |     2    7    8    |
  ----------------------"
  []
  (swap! sudoku-ref (fn [sudoku-matrix]
                      (mapv (fn [{:keys [quadrant values]}]
                              {:quadrant quadrant :values (into {} (map #(replace-set-to-content! %) (partition 2 (logic.solver/map->vec values))))}) sudoku-matrix))))


(s/defn replace-one-occurrence-to-its-content!
  "Given a setup of set of candidate numbers, we may notice that there is only one occurrence
   regarding at least of one of three sudoku laws, it will bring to front and become as a solution
   number. Example:

  ----------------------
  |     1    4  [3, 5] |
  |     5    6    9    |
  |     2    7    8    |
  ----------------------

  the three is unique result, regarding for quadrant law, so:

  ----------------------
  |     1    4    3    |
  |     5    6    9    |
  |     2    7    8    |
  ----------------------"
  []
  (swap! sudoku-ref (fn [sudoku-matrix]
                      (mapv (fn [{:keys [quadrant values]}]
                              (let [replenish-one-occurrence-values (into {} (map #(inject-one-occurrence-value! quadrant %) (partition 2 (logic.solver/map->vec values))))]
                                {:quadrant quadrant :values replenish-one-occurrence-values})) sudoku-matrix))))

#_(s/defn remove-val-from-cell-sets!
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

(defmacro play
  [fn]
  "Play filling with sets, calculate, invalidating sets"
  `(do
     (replace-nils-with-set-of-candidate-values!)
     (~fn)
     (invalidate-sets-with-nils)))


(s/defn replenish-with-remained-spots! []
  (let [initial-state-sudoku @sudoku-ref]
    (play replace-one-sized-sets-to-its-content!)
    (play replace-one-occurrence-to-its-content!)
    (when (not= initial-state-sudoku @sudoku-ref)
      (recur))))

(s/defn solve!
  [sudoku-matrix :- models.solver/MatrixSolving]
  (reset! sudoku-ref sudoku-matrix)
  (replenish-with-remained-spots!)
  @sudoku-ref)

(s/defn fill! :- wire.out.solver/MatrixResult
  "This function 'attempts' to solve the sudoku"
  [input :- wire.in.solver/MatrixInput]
  (->> input
       adapters.solver/->matrix
       solve!
       adapters.solver/->singularity))
