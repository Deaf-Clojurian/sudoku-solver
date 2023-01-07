(ns sudoku-solver.diplomat.http-server
  (:require
    [clojure.data.json :as json]
    [compojure.core :refer [defroutes GET POST]]
    [compojure.route :as route]
    [ring.middleware.json :refer [wrap-json-response]]
    [ring.middleware.content-type :refer [wrap-content-type]]
    [ring.util.response :as response :refer [response]]
    [sudoku-solver.adapters.solver :as adapters.solver]
    [sudoku-solver.controllers.solver :as controllers.solver]
    [sudoku-solver.controllers.verifiers :as controllers.verifiers])
  (:import (clojure.lang ExceptionInfo)))

(defn verify
  [{:keys [body]}]
  (response
    (json/write-str
      {:verified-as (-> body
                        slurp
                        json/read-str
                        controllers.verifiers/check)})))

(defn solve
  [{:keys [body]}]
  (try
    (response
      (json/write-str
        (-> body
            slurp
            json/read-str
            controllers.solver/fill!)))
    (catch ExceptionInfo e
      (response/bad-request
        (ex-data e)))))

(defn solve-pretty
  [{:keys [body]}]
  (try
    (response
      (-> body
          slurp
          json/read-str
          controllers.solver/fill!
          adapters.solver/->prettified))
    (catch ExceptionInfo e
      (response/bad-request
        (:error (ex-data e))))))

(defn show-paths
  [_]
  (response
    {:sudoku-solver {:routes ["verify"
                              "solve"
                              "solve/pretty"]}}))

(defroutes app-routes
           (GET "/" [] (-> show-paths wrap-json-response))

           (POST "/verify" [] (-> verify wrap-json-response (wrap-content-type "application/json")))

           (POST "/solve" [] (-> solve (response/content-type "application/json") wrap-json-response ))

           (POST "/solve/pretty" [] (-> solve-pretty (wrap-content-type "text/plain")))

           (route/not-found "{\"error\" : \"Not Found\"}"))
