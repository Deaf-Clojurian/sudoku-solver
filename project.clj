(defproject sudoku-solver "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [nubank/matcher-combinators "3.7.0"]
                 [prismatic/schema "1.4.0"]
                 [ring/ring-core "1.9.2"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [org.clojure/data.json "2.3.9"]
                 [compojure "1.6.0"]]
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
  :profiles {:uberjar {:aot :all}})
