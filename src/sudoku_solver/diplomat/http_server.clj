(ns sudoku-solver.diplomat.http-server
  (:require
   [clojure.data.json :as json]
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]
   [ring.middleware.json :refer [wrap-json-response]]
   [ring.util.response :refer [response]]
   [sudoku-solver.adapters.solver :as adapters.solver]
   [sudoku-solver.controllers.solver :as controllers.solver]
   [sudoku-solver.controllers.verifiers :as controllers.verifiers]))

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
  (response
   (json/write-str (-> body
                       slurp
                       json/read-str
                       controllers.solver/fill!))))

(defn solve-pretty
  [{:keys [body]}]
  (response
   (-> body
       slurp
       json/read-str
       controllers.solver/fill!
       adapters.solver/->prettified)))

(defroutes app-routes
  (GET "/" [] "{\"sudoku-solver\": {\"routes\" : [\"verify\", \"solve\", \"solve/pretty\"]}}")

  (POST "/verify" [] (-> verify wrap-json-response))

  (POST "/solve" [] (-> solve wrap-json-response))

  (POST "/solve/pretty" [] (-> solve-pretty))

  (route/not-found "{\"error\" : \"Not Found\"}"))
