(ns sudoku-solver.core
  (:require
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [sudoku-solver.adapters.solver :as adapters.solver]
   [sudoku-solver.controllers.solver :as controllers.solver]
   [sudoku-solver.controllers.verifiers :as controllers.verifiers]
   [sudoku-solver.diplomat.http-server :as diplomat.http-server])
  (:gen-class))

#_(def sudoku-matrix
    [{:quadrant :00
      :values   {:00 7 :01 3 :02 nil
                 :10 6 :11 nil :12 1
                 :20 nil :21 4 :22 nil}}
     {:quadrant :01
      :values   {:00 9 :01 nil :02 nil
                 :10 3 :11 nil :12 nil
                 :20 nil :21 8 :22 nil}}
     {:quadrant :02
      :values   {:00 nil :01 1 :02 nil
                 :10 nil :11 nil :12 nil
                 :20 nil :21 nil :22 nil}}
     {:quadrant :10
      :values   {:00 nil :01 nil :02 nil
                 :10 nil :11 7 :12 5
                 :20 9 :21 1 :22 nil}}
     {:quadrant :11
      :values   {:00 nil :01 nil :02 nil
                 :10 nil :11 nil :12 nil
                 :20 nil :21 nil :22 3}}
     {:quadrant :12
      :values   {:00 nil :01 nil :02 nil
                 :10 nil :11 nil :12 nil
                 :20 2 :21 nil :22 4}}
     {:quadrant :20
      :values   {:00 nil :01 nil :02 nil
                 :10 5 :11 2 :12 nil
                 :20 nil :21 nil :22 nil}}
     {:quadrant :21
      :values   {:00 nil :01 nil :02 1
                 :10 nil :11 4 :12 nil
                 :20 nil :21 nil :22 nil}}
     {:quadrant :22
      :values   {:00 nil :01 7 :02 nil
                 :10 nil :11 nil :12 nil
                 :20 5 :21 nil :22 9}}])

#_(def sudoku-matrix-completed
    [{:quadrant :00
      :values   {:00 2 :01 1 :02 9
                 :10 5 :11 4 :12 3
                 :20 8 :21 7 :22 6}}
     {:quadrant :01
      :values   {:00 5 :01 4 :02 3
                 :10 8 :11 7 :12 6
                 :20 2 :21 1 :22 9}}
     {:quadrant :02
      :values   {:00 6 :01 7 :02 8
                 :10 9 :11 1 :12 2
                 :20 3 :21 4 :22 5}}
     {:quadrant :10
      :values   {:00 4 :01 3 :02 2
                 :10 7 :11 6 :12 5
                 :20 1 :21 9 :22 8}}
     {:quadrant :11
      :values   {:00 7 :01 6 :02 5
                 :10 1 :11 9 :12 8
                 :20 4 :21 3 :22 2}}
     {:quadrant :12
      :values   {:00 8 :01 9 :02 1
                 :10 2 :11 3 :12 4
                 :20 5 :21 6 :22 7}}
     {:quadrant :20
      :values   {:00 3 :01 2 :02 1
                 :10 6 :11 5 :12 4
                 :20 9 :21 8 :22 7}}
     {:quadrant :21
      :values   {:00 6 :01 5 :02 4
                 :10 9 :11 8 :12 7
                 :20 3 :21 2 :22 1}}
     {:quadrant :22
      :values   {:00 7 :01 8 :02 9
                 :10 1 :11 2 :12 3
                 :20 4 :21 5 :22 6}}])

#_(def sudoku-matrix-input
    [[nil 4 nil nil nil 3 nil 8 nil]
     [1 7 nil nil nil nil nil nil 6]
     [nil nil 9 2 nil nil nil nil nil]
     [5 nil 2 nil 9 nil nil nil nil]
     [nil nil nil 8 7 nil nil nil nil]
     [nil nil nil nil nil nil nil nil nil]
     [nil nil nil nil 6 nil 3 7 nil]
     [nil 8 nil nil 3 nil nil 2 nil]
     [nil nil 5 nil nil 8 9 nil nil]])

#_(def sudoku-matrix-input-expert-level
    [[nil nil 3 8 nil nil nil 9 7]
     [6 nil nil nil nil 3 8 nil 1]
     [2 nil nil nil nil nil 5 nil nil]
     [9 1 nil nil nil nil 6 7 nil]
     [3 7 nil nil 9 nil nil nil nil]
     [nil nil nil nil nil nil 1 nil nil]
     [nil nil nil 1 nil nil nil nil 2]
     [nil nil nil 6 5 9 nil nil nil]
     [nil nil nil nil 8 nil nil 5 nil]])

(def sudoku-matrix-input-expert-level-2
  [[nil nil nil 6 nil nil 2 1 nil]
   [nil nil 8 nil nil nil nil 6 nil]
   [nil 7 nil nil nil nil nil nil nil]
   [9 nil nil nil 6 nil 4 nil nil]
   [nil nil 1 nil nil nil nil nil 9]
   [nil 2 nil nil nil 4 nil nil nil]
   [nil 5 nil nil 3 7 nil 8 nil]
   [nil nil nil nil 9 8 nil 7 1]
   [nil nil 9 nil nil nil nil nil nil]])

#_(def sudoku-matrix-input-easy-level
    [[1 4 2 nil 9 7 nil 6 3]
     [7 nil 3 4 6 nil nil nil nil]
     [nil nil 5 3 8 2 nil 4 1]
     [3 7 4 nil nil 6 nil nil 8]
     [5 2 9 8 7 3 6 1 4]
     [nil 6 1 9 nil nil 3 nil 7]
     [nil 1 6 7 nil nil nil 3 9]
     [nil 5 nil nil nil nil 4 7 6]
     [9 nil 7 6 4 nil nil 8 nil]])

#_(def sudoku-matrix-input-easy-level-2
    [[7 nil 9 nil nil 1 6 3 nil]
     [nil 5 1 6 8 4 9 nil nil]
     [nil 8 6 nil nil 7 nil nil nil]
     [5 1 nil 7 2 nil 3 nil 9]
     [4 6 7 8 3 9 2 1 5]
     [9 3 nil nil nil 5 7 nil 6]
     [nil nil 5 nil nil nil nil 7 3]
     [8 7 nil nil 6 nil nil 9 1]
     [1 9 4 5 7 3 8 nil 2]])

#_(def sudoku-matrix-input-medium-level
    [[7 nil nil nil nil 4 3 5 8]
     [nil 4 nil nil 2 8 7 6 9]
     [nil 9 8 nil nil nil nil nil 1]
     [nil nil nil nil 7 2 nil nil nil]
     [nil nil nil 9 6 nil 8 2 7]
     [nil nil nil 4 8 nil 9 3 6]
     [nil 8 nil 2 1 9 5 nil nil]
     [nil nil 5 nil 4 7 1 nil 2]
     [2 1 7 8 5 nil 6 nil nil]])

#_(def sudoku-matrix-input-hard-level
    [[nil nil nil 1 nil 6 3 nil 8]
     [9 8 5 nil nil nil nil nil 4]
     [3 nil 6 nil 8 9 7 2 5]
     [7 4 nil nil nil nil nil nil nil]
     [nil nil nil nil nil nil nil nil 7]
     [nil 9 3 2 nil nil nil nil nil]
     [2 3 1 9 nil 7 nil nil 6]
     [5 7 9 6 nil 8 nil 4 nil]
     [nil nil 4 nil nil 3 1 7 9]])

(def plain-json-correct
  [[2 1 9 5 4 3 6 7 8]
   [5 4 3 8 7 6 9 1 2]
   [8 7 6 2 1 9 3 4 5]
   [4 3 2 7 6 5 8 9 1]
   [7 6 5 1 9 8 2 3 4]
   [1 9 8 4 3 2 5 6 7]
   [3 2 1 6 5 4 7 8 9]
   [6 5 4 9 8 7 1 2 3]
   [9 8 7 3 2 1 4 5 6]])

(defn -main
  "Use it to run through 'lein run <arg>'"
  [& args]
  (case (keyword (first args))
    :solver (prn (controllers.solver/fill! sudoku-matrix-input-expert-level-2))
    :solver-pretty (println (adapters.solver/->prettified (controllers.solver/fill! sudoku-matrix-input-expert-level-2)))
    :verifier (prn (controllers.verifiers/check plain-json-correct))
    (prn "Unknown option")))

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(def app
  (wrap-defaults diplomat.http-server/app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
