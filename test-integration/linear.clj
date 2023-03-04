(ns linear
  (:require
   [clojure.test :refer [deftest is]]
   [fixture]
   [ring.mock.request :as mock]
   [sudoku-solver.adapters.convert :as adapters.convert]
   [sudoku-solver.diplomat.http-server :as diplomat.http-server]))

(deftest easy-level-test
  (let [request (-> (mock/request :post "/solve/linear")
                    (mock/json-body (adapters.convert/standard->linear fixture/input-easy-level)))
        response (diplomat.http-server/app-routes request)]
    (is (= 200 (:status response)))
    (is (= fixture/result-easy-level
           (:body response)))))

(deftest medium-level-test
  (let [request (-> (mock/request :post "/solve/linear")
                    (mock/json-body (adapters.convert/standard->linear fixture/input-medium-level)))
        response (diplomat.http-server/app-routes request)]
    (is (= 200 (:status response)))
    (is (= fixture/result-medium-level
           (:body response)))))

(deftest hard-level-test
  (let [request (-> (mock/request :post "/solve/linear")
                    (mock/json-body (adapters.convert/standard->linear fixture/input-hard-level)))
        response (diplomat.http-server/app-routes request)]
    (is (= 200 (:status response)))
    (is (= fixture/result-hard-level
           (:body response)))))
