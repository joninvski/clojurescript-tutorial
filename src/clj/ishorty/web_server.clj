(ns ishorty.web-server
  (:require [yada.yada :as yada]
            [selmer.parser :as selmer]
            [clojure.core.async :as async]
            [com.stuartsierra.component :as component]))

(defn top-urls-updates [sse-mult]
  (yada/resource
    {:methods
     {:get
      {:produces "text/event-stream"
       :response (fn [ctx]
                   (async/tap sse-mult (async/chan 10)))}}}))

(defn shorten-frontend []
  (yada/resource
    {:methods
     {:get
      {:produces "text/html"
       :response
       (fn [ctx] (selmer/render-file
                  "shorten.html"
                  {:title "iShorty Shorten" :ctx ctx}))}}}))

(defn api [sse-mult]
  ["/"
    [[["api/"]
      [["top/messages" (top-urls-updates sse-mult)]]]
     ["index.html" (shorten-frontend)]
     ["css/" (yada/as-resource (clojure.java.io/file "target/css"))]
     ["js/" (yada/as-resource (clojure.java.io/file "target/js"))]]])

(defn- periodical-top-url-update [top-url-refresh-time sse-chan]
  (future
    (while (not (Thread/interrupted))
      (do
        (Thread/sleep top-url-refresh-time)
        (async/put! sse-chan (str "hello"))))))

(defn create-web-server [port sse-mult]
  (yada/listener
    (api sse-mult)
    {:port port}))

(defn stop [server update-future sse-chan]
  ((:close server))
  (future-cancel update-future)
  (async/close! sse-chan))

(defrecord WebServer [port web-server redis]
  component/Lifecycle

  (start [component]
    (println (str ";; starting webserver"))
    (let [sse-chan (async/chan (async/sliding-buffer 3))
          sse-mult (async/mult sse-chan)
          server (create-web-server port sse-mult)
          update-top-url-future (periodical-top-url-update 1000 sse-chan) ]
      (-> component
        (assoc :web-server server)
        (assoc :sse-chan sse-chan)
        (assoc :sse-mult sse-mult)
        (assoc :update-future update-top-url-future))))

  (stop [component]
    (println ";; stopping webserver")
    (stop (:web-server component)
          (:update-future component)
          (:sse-chan component))
    (assoc component :web-server nil)))

(defn new-web-server [port]
  (let [dependencies [:redis]]
    (component/using
      (map->WebServer {:port port})
      dependencies)))