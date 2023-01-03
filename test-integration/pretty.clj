(ns pretty
  (:require
   [clojure.test :refer [deftest is]]
   [ring.mock.request :as mock]
   [sudoku-solver.diplomat.http-server :as diplomat.http-server]))

(def prettyfied-response
  "-------------------------
| 6 2 3 | 1 9 4 | 5 8 7 |
| 4 1 5 | 8 7 3 | 2 6 9 |
| 7 9 8 | 2 5 6 | 3 1 4 |
-------------------------
| 8 6 4 | 3 2 7 | 9 5 1 |
| 3 5 2 | 9 4 1 | 8 7 6 |
| 9 7 1 | 6 8 5 | 4 2 3 |
-------------------------
| 2 8 7 | 4 6 9 | 1 3 5 |
| 5 3 9 | 7 1 8 | 6 4 2 |
| 1 4 6 | 5 3 2 | 7 9 8 |
-------------------------")

(deftest happy-way-test
  (let [request (-> (mock/request :post "/solve/pretty")
                    (mock/json-body [[6 2 nil 1 nil nil nil 8 7]
                                     [4 nil 5 nil nil nil nil nil 9]
                                     [7 9 nil nil 5 nil 3 1 nil]
                                     [nil 6 nil nil nil 7 9 5 1]
                                     [nil nil 2 nil nil nil 8 7 nil]
                                     [9 nil 1 nil nil 5 nil nil 3]
                                     [2 nil 7 nil nil 9 nil 3 5]
                                     [5 nil nil nil nil nil 6 4 nil]
                                     [nil 4 6 nil nil nil 7 9 8]]))
        response (diplomat.http-server/app-routes request)]
    (is (= 200 (:status response)))
    (is (= prettyfied-response (:body response)))))
