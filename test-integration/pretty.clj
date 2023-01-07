(ns pretty
  (:require
   [clojure.test :refer [deftest is]]
   [ring.mock.request :as mock]
   [fixture]
   [sudoku-solver.diplomat.http-server :as diplomat.http-server]))



(deftest happy-way-test
  (let [request (-> (mock/request :post "/solve/pretty")
                    (mock/json-body fixture/input-for-pretty))
        response (diplomat.http-server/app-routes request)]
    (is (= 200 (:status response)))
    (is (= fixture/prettyfied-response (:body response)))))

(deftest odd-test
  (let [request (-> (mock/request :post "/solve/pretty")
                    (mock/json-body fixture/odd-input-for-pretty))
        response (diplomat.http-server/app-routes request)]
    (is (= 200 (:status response)))
    (is (= fixture/prettyfied-response (:body response)))))
