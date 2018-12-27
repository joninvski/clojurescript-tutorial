(ns ishorty.repository
  (:require [taoensso.carmine :as redis]
            [com.stuartsierra.component :as component]))

(declare ^:private redis-conn)
(defmacro wcar* [& body] `(redis/wcar redis-conn ~@body))

(defn save [short-url long-url]
  (wcar* (redis/set short-url long-url)))

(defn inc-score [short-url]
  (wcar* (redis/zincrby "top-urls" 1 short-url)))

(defn fetch [short-url]
  (wcar* (redis/get short-url)))

(defn flush-all []
  (wcar* (redis/flushall)))

(defn top-urls [n]
  (if (pos? n)
    (wcar* (redis/zrevrange "top-urls" 0 (dec n)))
    []))

(defn create-redis-conn [redis-url]
  (def redis-conn {:pool {} :spec {:uri redis-url}})
  redis-conn)

(defrecord Repository [redis-url]
  component/Lifecycle

  (start [component]
    (println ";; starting redis connection")
    (let [redis-conn (create-redis-conn redis-url)]
      (assoc component :redis-conn redis-conn)))

  (stop [component]
    (println ";; stopping redis connection")
    (assoc component :redis-conn nil)))

(defn new-repository [redis-url]
  (map->Repository {:redis-url redis-url}))
