(ns sudoku-solver.common)

(def pos-filler
  (for [i (range 0 3)
        j (range 0 3)]
    (str i j)))

(def vertical-positions
  (mapv vec (partition 3 (for [i (range 0 3)
                               j (range 0 3)]
                           (keyword (str j i))))))

(def horizontal-positions
  (mapv vec (partition 3 (for [i (range 0 3)
                               j (range 0 3)]
                           (keyword (str i j))))))

(defn matrix-direction-lines
  [positions]
  (partition 9
             (flatten (for [quadrant positions]
                        (map #(vec
                                (for [matrix quadrant
                                      value %]
                                  {:matrix matrix :value value})) positions)))))

(def matrix-vertical-lines
  (matrix-direction-lines vertical-positions))

(def matrix-horizontal-lines
  (matrix-direction-lines horizontal-positions))

(def all-quadrants
  (mapv #(mapv (fn [v] {:matrix (keyword %) :value (keyword v)}) pos-filler) pos-filler))

(def all-traverses
  (concat all-quadrants matrix-horizontal-lines matrix-vertical-lines))
