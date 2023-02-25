(ns pencil
  (:require
   [clojure.test :refer [deftest is]]
   [fixture]
   [ring.mock.request :as mock]
   [sudoku-solver.diplomat.http-server :as diplomat.http-server]))

(deftest hard-pencil-test
  (let [request (-> (mock/request :post "/pencil")
                    (mock/json-body fixture/input-hard-pencil))
        response (diplomat.http-server/app-routes request)]
    (is (= 200 (:status response)))
    (is (= fixture/result-hard-pencil
           (:body response)))))
