(ns sudoku-solver.common)

(def pos-filler
  (for [i (range 0 3)
        j (range 0 3)]
    (str i j)))

(def all-quadrants
  (mapv #(mapv (fn [v] {:matrix (keyword %) :value (keyword v)}) pos-filler) pos-filler))
