(ns verify
  (:require
   [clojure.test :refer [deftest is]]
   [ring.mock.request :as mock]
   [sudoku-solver.diplomat.http-server :as diplomat.http-server]))

(deftest routes-test
  (let [request (mock/request :get "/")
        response (diplomat.http-server/app-routes request)]
    (is (= (:status response) 200))
    (is (= (:headers response) {"Content-Type" "application/json; charset=utf-8"}))
    (is (= (:body response)  "{\"sudoku-solver\":{\"routes\":[\"verify\",\"solve\",\"solve/pretty\",\"solve/linear\",\"solve/linear/pretty\",\"pencil\"]}}"))))
