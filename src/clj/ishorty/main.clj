(ns ishorty.main
  "Entrypoint for initializing system"
  (:require [com.stuartsierra.component :as component]
            [ishorty.web-server :as web-server]
            [environ.core :refer [env]]))

(def system nil)
(def http-port (Integer. (env :http-port "8080")))

(defn- system-map [config]
  (component/system-map
    :http (web-server/new-web-server (:port config))))

(defn- block-forever []
  @(promise))

(defn new-system [config]
  (system-map config))

(defn start-all []
  (let [config {:port http-port}]
    (def system (component/start (new-system config)))))

(defn stop-all []
  (component/stop system))

(defn run []
  (start-all)
  (println (format "Serving HTTP on %s. Press control+c to quit" (str http-port)))
  (block-forever))
