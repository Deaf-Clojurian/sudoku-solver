(ns sudoku-solver.common
  (:require
   [sudoku-solver.adapters.quadrant :as adapters.quadrant]))

(def pos-filler
  (for [i (range 0 3)
        j (range 0 3)]
    (str i j)))

(def vertical-positions
  (partition 3 (map #(apply (comp keyword str) (reverse %)) pos-filler)))

(def horizontal-positions
  (partition 3 (map keyword pos-filler)))

(defn matrix-direction-lines
  [positions]
  (partition 9 (flatten (for [quadrant positions]
                          (map #(for [matrix quadrant
                                      value %]
                                  (adapters.quadrant/->position matrix value)) positions)))))

(def matrix-vertical-lines
  (matrix-direction-lines vertical-positions))

(def matrix-horizontal-lines
  (matrix-direction-lines horizontal-positions))

(def all-quadrants
  (map #(map (fn [v] (adapters.quadrant/->position (keyword %) (keyword v))) pos-filler) pos-filler))

(def all-traverses
  (concat all-quadrants matrix-horizontal-lines matrix-vertical-lines))
