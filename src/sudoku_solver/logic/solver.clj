(ns sudoku-solver.logic.solver
  (:require
   [schema.core :as s]
   [sudoku-solver.common :as common]
   [sudoku-solver.wire.out.solver :as wire.out.solver]))

(s/defn crude-invert-fill :- #{s/Int}
  "With the all availables values, just bring the inversed values, those
   that are missing and should be the options to solve the Sudoku. Example:
   By three laws, given a position, we get resulted as #{1 2 3 4}, so to fill in, it must be one of:
   #{5 6 7 8 9}, then using this as reference"
  [values :- #{s/Int}]
  (let [values-without-nil (filter #(not (nil? %)) values)]
    (set (for [missing-number (range 1 10)
               :when (not (some #{missing-number} values-without-nil))]
           missing-number))))

(s/defn gather-references-from-pos :- '(map?)
  "Get vertical lines, horizontal lines that cross and
   contains in current quadrant. Example, given:
   00------01------02-------
   | R o o | o o o | o o o |   By the position represented by the R
   | o o o | . . . | . . . |   letter, that would the position 00 of quadrant 00,
   | o o o | . . . | . . . |   with this, the function brings all references (NOT the content!!)
   10------11------12-------   that represents the vertical line throughout whole matrix that contains the R and
   | o . . | . . . | . . . |   the horizontal line that also contains the R and lastly
   | o . . | . . . | . . . |   the all ref in the quadrant where is the R, obeying the Sudoku laws. We can visualize by 'o' marks.
   | o . . | . . . | . . . |   By example, it will bring as the response for position 00 of quadrant 00:
   20------21------22-------
   | o . . | . . . | . . . |   (({:matrix :00, :value :00}
   | o . . | . . . | . . . |     {:matrix :00, :value :01}
   | o . . | . . . | . . . |     {:matrix :00, :value :02}
   -------------------------     {:matrix :00, :value :10}
                                 {:matrix :00, :value :11}
                                 {:matrix :00, :value :12}
                                 {:matrix :00, :value :20}
                                 {:matrix :00, :value :21}
                                 {:matrix :00, :value :22})
                                ({:matrix :00, :value :00}
                                 {:matrix :00, :value :01}
                                 {:matrix :00, :value :02}
                                 {:matrix :01, :value :00}
                                 {:matrix :01, :value :01}
                                 {:matrix :01, :value :02}
                                 {:matrix :02, :value :00}
                                 {:matrix :02, :value :01}
                                 {:matrix :02, :value :02})
                                ({:matrix :00, :value :00}
                                 {:matrix :00, :value :10}
                                 {:matrix :00, :value :20}
                                 {:matrix :10, :value :00}
                                 {:matrix :10, :value :10}
                                 {:matrix :10, :value :20}
                                 {:matrix :20, :value :00}
                                 {:matrix :20, :value :10}
                                 {:matrix :20, :value :20}))

                                 P.S.: ':value' is the value of reference. A refactoring to change it to :reference is in plans :grimacing: "
  [quadrant :- s/Keyword
   quadrant-pos :- s/Keyword]
  (filter #(first (filter (fn [{:keys [matrix value]}] (and (= value quadrant-pos) (= matrix quadrant))) %)) common/all-traverses))

(s/defn detect-one-occurrence :- (s/conditional #{s/Int} s/Int)
  "Detect if the number that contains in a set is the unique throughout whole column, line
   or quadrant, then replace it, else, go next"
  [value :- (s/pred set?)
   quadrant :- s/Keyword
   quadrant-pos :- s/Keyword]
  (map (fn [v]) (gather-references-from-pos quadrant quadrant-pos)))

(s/defn map->vec :- (s/pred vector?)
  "It takes a map, e.g., {a: 5 :b 'hello'} and transform into a vector:
  [:a 5 :b 'hello'], sequentially as values in a vector."
  [values :- (s/pred map?)]
  (reduce into [] values))

#_(s/defn find-and-replace
    [sudoku-ref :- models.solver/MatrixSolving
     at-least-number :- s/Int
     quadrant :- s/Keyword
     quadrant-pos :- s/Keyword]
    (map (fn [{quadrant-ref :quadrant values-ref :values}]
           {:quadrant quadrant-ref :values
            (if (= quadrant quadrant-ref)
              (assoc values-ref quadrant-pos at-least-number)
              values-ref)}) sudoku-ref))

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

(s/defn pick-value :- s/Str
  [matrix-result :- wire.out.solver/MatrixResult
   line :- s/Int
   col :- s/Int]
  (or (-> matrix-result (nth line) (nth col)) " "))
