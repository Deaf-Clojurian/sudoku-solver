(defproject sudoku-solver "0.5.2"
  :description "A Sudoku solver (or spoiler?) in Clojure!"
  :url "https://github.com/deafjava"
  :license {:name "Creative Commons Attribution 4.0 International"
            :url "http://creativecommons.org/licenses/by/4.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [nubank/matcher-combinators "3.8.7"]
                 [prismatic/schema "1.4.1"]
                 [ring/ring-core "1.10.0"]
                 [ring/ring-defaults "0.3.4"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-mock "0.4.0"]
                 [ring/ring-jetty-adapter "1.10.0"]
                 [org.clojure/data.json "2.4.0"]
                 [compojure "1.7.0"]]
  :plugins [[lein-ring "0.12.6"]
            [lein-ancient "1.0.0-RC3"]
            [com.github.clojure-lsp/lein-clojure-lsp "1.3.17"]]
  :ring {:handler sudoku-solver.core/app}
  :clojure-lsp {:settings {:clean {:ns-inner-blocks-indentation :same-line}}} ;; API options
  :aliases {"clean-ns" ["clojure-lsp" "clean-ns" "--dry"]          ;; check if namespaces are clean
            "format" ["clojure-lsp" "format" "--dry"]              ;; check if namespaces are formatted
            "diagnostics" ["clojure-lsp" "diagnostics"]            ;; check if project has any diagnostics (clj-kondo findings)
            "lint" ["do" ["clean-ns"] ["format"] ["diagnostics"]]  ;; check all above
            "clean-ns-fix" ["clojure-lsp" "clean-ns"]              ;; Fix namespaces not clean
            "format-fix" ["clojure-lsp" "format"]                  ;; Fix namespaces not formatted
            "lint-fix" ["do" ["clean-ns-fix"] ["format-fix"]]}     ;; Fix both
  :main ^:skip-aot sudoku-solver.core
  :target-path "target/%s"
  :test-paths ["test" "test-integration"]
  :profiles {:uberjar {:aot :all}})
