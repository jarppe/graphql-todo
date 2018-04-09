(defproject jarppe/graphql-todo "0.0.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]

                 ; Syksy:
                 [jarppe/syksy "0.0.4"]

                 ; Common stuff:
                 [org.clojure/core.async "0.4.474"]
                 [prismatic/schema "1.1.9"]
                 [potemkin "0.4.5"]
                 [metosin/potpuri "0.5.1"]
                 [clj-http "3.8.0"]
                 [clj-time "0.14.2"]

                 ; MongoDB:
                 [com.novemberain/monger "3.1.0" :exclusions [[com.google.guava/guava]]]

                 ; GraphQL:
                 [com.walmartlabs/lacinia "0.25.0"]

                 ; Web stuff:
                 [metosin/reitit "0.1.1-SNAPSHOT"]
                 [metosin/reitit-ring "0.1.1-SNAPSHOT"]
                 [metosin/reitit-schema "0.1.1-SNAPSHOT"]
                 [metosin/eines "0.0.9"]

                 ; ClojureScript:
                 [org.clojure/clojurescript "1.10.238"]
                 [rum "0.11.2"]
                 [org.roman01la/citrus "3.0.1"]
                 [cljs-http "0.1.45"]]

  :min-lein-version "2.8.1"
  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj"]
  :target-path "target/dev"
  :auto-clean false

  :sass {:source-paths ["src/sass"]
         :source-map true
         :output-style :compressed}

  :figwheel {:css-dirs ["target/dev/resources/public/css"]
             :repl false}

  :plugins [[lein-pdo "0.1.1"]
            [deraen/lein-sass4clj "0.3.1"]
            [lein-figwheel "0.5.15"]
            [lein-cljsbuild "1.1.7"]]

  :profiles {:dev {:dependencies [[metosin/testit "0.2.1"]]
                   :resource-paths ["target/dev/resources"]
                   :sass {:target-path "target/dev/resources/public/css"}}
             :uberjar {:target-path "target/prod"
                       :source-paths ^:replace ["src/clj" "src/cljs"]
                       :resource-paths ["target/prod/resources"]
                       :sass {:target-path "target/prod/resources/public/css"}
                       :main syksy.main
                       :aot [syksy.main app.components]
                       :uberjar-name "app.jar"}}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :figwheel true
                        :compiler {:main "app.front.main"
                                   :asset-path "asset/js/out"
                                   :external-config {:devtools/config {:features-to-install :all}}
                                   :closure-defines {goog.DEBUG true}
                                   :output-to "target/dev/resources/public/js/main.js"
                                   :output-dir "target/dev/resources/public/js/out"
                                   :optimizations :none
                                   :source-map true
                                   :cache-analysis true
                                   :pretty-print true}}
                       {:id "prod"
                        :source-paths ["src/cljc" "src/cljs"]
                        :compiler {:main "app.front.main"
                                   :output-to "target/prod/resources/public/js/main.js"
                                   :closure-defines {goog.DEBUG false}
                                   :optimizations :advanced}}]}

  :aliases {"dev" ["do"
                   ["clean"]
                   ["pdo"
                    ["sass4clj" "auto"]
                    ["figwheel"]]]
            "uberjar" ["with-profile" "uberjar" "do"
                       ["clean"]
                       ["sass4clj" "once"]
                       ["cljsbuild" "once" "prod"]
                       ["uberjar"]]})
