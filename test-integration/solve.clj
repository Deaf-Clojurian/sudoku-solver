(ns solve
  (:require
   [clojure.test :refer [deftest is]]
   [fixture]
   [ring.mock.request :as mock]
   [sudoku-solver.diplomat.http-server :as diplomat.http-server]))

(deftest easy-level-test
  (let [request (-> (mock/request :post "/solve")
                    (mock/json-body fixture/input-easy-level))
        response (diplomat.http-server/app-routes request)]
    (is (= 200 (:status response)))
    (is (= fixture/result-easy-level
           (:body response)))))

(deftest medium-level-test
  (let [request (-> (mock/request :post "/solve")
                    (mock/json-body fixture/input-medium-level))
        response (diplomat.http-server/app-routes request)]
    (is (= 200 (:status response)))
    (is (= fixture/result-medium-level
           (:body response)))))

(deftest hard-level-test
  (let [request (-> (mock/request :post "/solve")
                    (mock/json-body fixture/input-hard-level))
        response (diplomat.http-server/app-routes request)]
    (is (= 200 (:status response)))
    (is (= fixture/result-hard-level
           (:body response)))))

#_(deftest specialist-level-test
    (let [request (-> (mock/request :post "/solve")
                      (mock/json-body fixture/input-easy-level))
          response (diplomat.http-server/app-routes request)]
      (is (= 200 (:status response)))
      (is (= fixture/result-easy-level
             (:body response)))))
