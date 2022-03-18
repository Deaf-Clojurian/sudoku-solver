(ns sudoku-solver.core
  (:gen-class))

(def sudoku-matrix [{:quadrant     :00
                     :fixed-values {:00 7 :01 3 :02 nil
                                    :10 6 :11 nil :12 1
                                    :20 nil :21 4 :22 nil}}
                    {:quadrant     :01
                     :fixed-values {:00 9 :01 nil :02 nil
                                    :10 3 :11 nil :12 nil
                                    :20 nil :21 8 :22 nil}}
                    {:quadrant     :02
                     :fixed-values {:00 nil :01 1 :02 nil
                                    :10 nil :11 nil :12 nil
                                    :20 nil :21 nil :22 nil}}
                    {:quadrant     :10
                     :fixed-values {:00 nil :01 nil :02 nil
                                    :10 nil :11 7 :12 5
                                    :20 9 :21 1 :22 nil}}
                    {:quadrant     :11
                     :fixed-values {:00 nil :01 nil :02 nil
                                    :10 nil :11 nil :12 nil
                                    :20 nil :21 nil :22 3}}
                    {:quadrant     :12
                     :fixed-values {:00 nil :01 nil :02 nil
                                    :10 nil :11 nil :12 nil
                                    :20 2 :21 nil :22 4}}
                    {:quadrant     :20
                     :fixed-values {:00 nil :01 nil :02 nil
                                    :10 5 :11 2 :12 nil
                                    :20 nil :21 nil :22 nil}}
                    {:quadrant     :21
                     :fixed-values {:00 nil :01 nil :02 1
                                    :10 nil :11 4 :12 nil
                                    :20 nil :21 nil :22 nil}}
                    {:quadrant     :22
                     :fixed-values {:00 nil :01 7 :02 nil
                                    :10 nil :11 nil :12 nil
                                    :20 5 :21 nil :22 9}}])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (case (keyword (first args))
    :solver (prn (str "Aaaalliii: " (first args)))
    :verifier (sudoku-solver.controllers.verifiers/check #_(second args) sudoku-matrix)
    (println "Unknown option")))
