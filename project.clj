(defproject sudoku-solver "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [nubank/matcher-combinators "3.5.1"]
                 [prismatic/schema "1.4.0"]
                 [ring/ring-core "1.9.6"]
                 [ring/ring-defaults "0.3.3"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-jetty-adapter "1.9.6"]
                 [org.clojure/data.json "2.4.0"]
                 [compojure "1.7.0"]]
  :plugins [[lein-ring "0.12.6"]]
  :ring {:handler sudoku-solver.core/app}
  :main ^:skip-aot sudoku-solver.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
