(set-env!
  :resource-paths #{"html"}
  :dependencies '[[tolitius/boot-check        "0.1.9" ]
                  [cljs-ajax                  "0.7.3"]
                  [org.clojure/core.async     "0.4.474" ]
                  [org.clojure/clojurescript  "1.10.238" ]
                  [com.stuartsierra/component "0.3.2" ]
                  [reagent                    "0.8.1"]
                  [adzerk/boot-cljs           "2.1.4" ]
                  [adzerk/boot-cljs-repl      "0.3.3" ]
                  [adzerk/boot-reload         "0.5.2" ]
                  [pandeiro/boot-http         "0.8.3" ]
                  [selmer                     "1.11.7" ]
                  [environ                    "1.1.0" ]
                  [adzerk/boot-test           "1.2.0" ]
                  [com.cemerick/piggieback    "0.2.1"]
                  [weasel                     "0.7.0"]
                  [org.clojure/tools.nrepl    "0.2.12"]
                  [cheshire                   "5.6.3" ]
                  [clj-http                   "3.9.0" ]
                  [org.clojars.magomimmo/domina "2.0.0-SNAPSHOT"]
                  [digest                     "1.4.8"]
                  [yada                       "1.2.11"]]
  :source-paths #{"src/clj" "src/cljs"})

(require '[tolitius.boot-check   :as check]
         '[ishorty.main          :as main]
         '[pandeiro.boot-http    :refer [serve]]
         '[adzerk.boot-cljs      :refer [cljs]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
         '[adzerk.boot-reload    :refer [reload]]
         '[adzerk.boot-test :as test])

(deftask check-sources []
  (comp
    (check/with-kibit)))

(deftask start-serving []
  (comp
   (with-pass-thru _
     (main/start-all))))

(deftask stop-serving []
  (comp
   (with-pass-thru _
     (main/stop-all))))

(deftask compile-cljs []
  (comp
   (cljs)
   (target :dir #{"target"})))

(deftask serve-with-clj-reload []
  (comp
   (with-pass-thru _
     (main/start-all))
   (watch)
   (reload :port 3002 :ip "0.0.0.0")
   (cljs-repl)
   (cljs)
   (target :dir #{"target"})))

(deftask test []
  (set-env! :source-paths #(conj % "test"))
  (comp
    (test/run-tests)))

(deftask run []
  (comp
    (cljs)
    (target :dir #{"target"})
    (with-pass-thru _
      (main/run))))
