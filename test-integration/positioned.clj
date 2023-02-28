(ns positioned
  (:require
    [clojure.test :refer [deftest is]]
    [fixture]
    [ring.mock.request :as mock]
    [sudoku-solver.diplomat.http-server :as diplomat.http-server]))



(deftest medium-level-test
  (let [request (-> (mock/request :post "/solve/positioned")
                    (mock/json-body fixture/positioned-input-medium-level))
        response (diplomat.http-server/app-routes request)]
    (is (= 200 (:status response)))
    (is (= fixture/result-positioned-medium-level
           (:body response)))))
