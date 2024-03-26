(defproject persondothing "0.1.0-SNAPSHOT"
  :description "Person do thing!"
  :url "http://pdt.crtep.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure     "1.10.1"]
                 [ring/ring-core           "1.9.1"]
                 [ring/ring-jetty-adapter  "1.9.1"]
                 [ring/ring-defaults "0.3.4"]
                 [compojure                "1.5.2"]
                 [hiccup/hiccup            "1.0.5"]
                 [com.github.seancorfield/next.jdbc "1.3.925"]
                 [com.h2database/h2 "2.1.214"]
                 [com.taoensso/sente "1.19.2"]
                 ]
  :repl-options {:init-ns persondothing.core}
  :main ^:skip-aot persondothing.core
  :target-path "target/%s"
  ;:javac-options ["-source" "1.8" "-target" "1.8"]
  :profiles {:uberjar {:aot :all
                       :javac-options ["-source" "1.8" "-target" "1.8"]}}
  )

