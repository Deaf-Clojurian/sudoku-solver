(ns sudoku-solver.diplomat.http-server
  (:require
   [clojure.data.json :as json]
   [compojure.core :refer [defroutes GET POST]]
   [compojure.route :as route]
   [ring.middleware.json :refer [wrap-json-response]]
   [ring.util.response :refer [response]]
   [sudoku-solver.adapters.convert :as adapters.convert]
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

(defn solve-linear
  [{:keys [body]}]
  (response
   (json/write-str (-> body
                       slurp
                       json/read-str
                       adapters.convert/linear->standard
                       controllers.solver/fill!))))

(defn solve-pretty
  [{:keys [body]}]
  (response
   (-> body
       slurp
       json/read-str
       controllers.solver/fill!
       adapters.solver/->prettified)))

(defn solve-linear-pretty
  [{:keys [body]}]
  (response
   (-> body
       slurp
       json/read-str
       adapters.convert/linear->standard
       controllers.solver/fill!
       adapters.solver/->prettified)))

(defn show-paths
  [_]
  (response
   {:sudoku-solver {:routes ["verify"
                             "solve"
                             "solve/pretty"
                             "solve/linear"
                             "solve/linear/pretty"
                             "pencil"]}}))

(defn pencil
  [{:keys [body]}]
  (response
   (json/write-str (-> body
                       slurp
                       json/read-str
                       controllers.solver/fill-candidate-values!))))

(defroutes app-routes
  (GET "/" [] (-> show-paths wrap-json-response))

  (POST "/verify" [] (-> verify wrap-json-response))

  (POST "/solve" [] (-> solve wrap-json-response))

  (POST "/solve/linear" [] (-> solve-linear wrap-json-response))

  (POST "/solve/linear/pretty" [] (-> solve-linear-pretty wrap-json-response))

  (POST "/solve/pretty" [] (-> solve-pretty))

  (POST "/pencil" [] (-> pencil))

  (route/not-found "{\"error\" : \"Not Found\"}"))
