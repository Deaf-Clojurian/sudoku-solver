(ns fixture)

(def input-easy-level
  [[1 4 2 nil 9 7 nil 6 3]
   [7 nil 3 4 6 nil nil nil nil]
   [nil nil 5 3 8 2 nil 4 1]
   [3 7 4 nil nil 6 nil nil 8]
   [5 2 9 8 7 3 6 1 4]
   [nil 6 1 9 nil nil 3 nil 7]
   [nil 1 6 7 nil nil nil 3 9]
   [nil 5 nil nil nil nil 4 7 6]
   [9 nil 7 6 4 nil nil 8 nil]])

(def input-medium-level
  [[7 nil nil nil nil 4 3 5 8]
   [nil 4 nil nil 2 8 7 6 9]
   [nil 9 8 nil nil nil nil nil 1]
   [nil nil nil nil 7 2 nil nil nil]
   [nil nil nil 9 6 nil 8 2 7]
   [nil nil nil 4 8 nil 9 3 6]
   [nil 8 nil 2 1 9 5 nil nil]
   [nil nil 5 nil 4 7 1 nil 2]
   [2 1 7 8 5 nil 6 nil nil]])

(def input-hard-level
  [[nil nil nil 1 nil 6 3 nil 8]
   [9 8 5 nil nil nil nil nil 4]
   [3 nil 6 nil 8 9 7 2 5]
   [7 4 nil nil nil nil nil nil nil]
   [nil nil nil nil nil nil nil nil 7]
   [nil 9 3 2 nil nil nil nil nil]
   [2 3 1 9 nil 7 nil nil 6]
   [5 7 9 6 nil 8 nil 4 nil]
   [nil nil 4 nil nil 3 1 7 9]])

(def input-hard-pencil
  [[nil nil 7 nil nil 6 3 nil 9]
   [nil 4 nil nil nil nil 6 1 8]
   [nil 3 8 nil nil 9 nil nil nil]
   [1 7 6 2 8 5 nil 3 nil]
   [nil nil nil nil nil nil nil nil 7]
   [8 9 nil nil nil 7 5 2 nil]
   [nil nil 3 1 9 nil 4 5 nil]
   [nil 6 nil nil nil 4 nil nil 2]
   [4 2 nil 7 6 8 1 nil nil]])

(def result-easy-level
  "[[1,4,2,5,9,7,8,6,3],[7,8,3,4,6,1,2,9,5],[6,9,5,3,8,2,7,4,1],[3,7,4,2,1,6,9,5,8],[5,2,9,8,7,3,6,1,4],[8,6,1,9,5,4,3,2,7],[4,1,6,7,2,8,5,3,9],[2,5,8,1,3,9,4,7,6],[9,3,7,6,4,5,1,8,2]]")

(def result-medium-level
  "[[7,2,6,1,9,4,3,5,8],[3,4,1,5,2,8,7,6,9],[5,9,8,7,3,6,2,4,1],[8,6,9,3,7,2,4,1,5],[4,5,3,9,6,1,8,2,7],[1,7,2,4,8,5,9,3,6],[6,8,4,2,1,9,5,7,3],[9,3,5,6,4,7,1,8,2],[2,1,7,8,5,3,6,9,4]]")

(def result-hard-level
  "[[4,2,7,1,5,6,3,9,8],[9,8,5,7,3,2,6,1,4],[3,1,6,4,8,9,7,2,5],[7,4,8,3,9,1,5,6,2],[1,5,2,8,6,4,9,3,7],[6,9,3,2,7,5,4,8,1],[2,3,1,9,4,7,8,5,6],[5,7,9,6,1,8,2,4,3],[8,6,4,5,2,3,1,7,9]]")

(def result-hard-pencil
  "[[[2,5],[1,5],7,[4,5,8],[1,4,2,5],6,3,[4],9],[[2,9,5],4,[2,9,5],[3,5],[7,3,2,5],[3,2],6,1,8],[[6,2,5],3,8,[4,5],[7,1,4,2,5],9,[7,2],[7,4],[4,5]],[1,7,6,2,8,5,[9],3,[4]],[[3,2,5],[5],[4,2,5],[4,6,3,9],[1,4,3],[1,3],[9,8],[4,6,9,8],7],[8,9,[4],[4,6,3],[1,4,3],7,5,2,[1,4,6]],[[7],[8],3,1,9,[2],4,5,[6]],[[7,9,5],6,[1,9,5],[3,5],[3,5],4,[7,9,8],[7,9,8],2],[4,2,[9,5],7,6,8,1,[9],[3]]]")
