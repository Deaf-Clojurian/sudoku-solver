(ns sudoku-solver.adapters.quadrant)

(defn ->position
  [matrix value]
  {:matrix matrix :value value})
