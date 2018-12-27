(ns ishorty.last-msgs-section-app
  (:require [reagent.core :as r]
            [cljs.reader :refer (read-string)]))

(def app-state (r/atom {}))

(defn teste []
  (fn []
    (let [state @app-state
          received-msgs (-> state :received-msgs )]
      [:div
       [:table
        [:thead
         [:tr
          [:th "Last 20 messages"]]]
        [:tbody
         (for [msg received-msgs]
           ^{:key (keyword "index" {:ts msg})}
           [:tr
            [:td
             (str (:ts msg))]
            [:td
             (str (:msg msg))]])]]])))

(defn init [section]
  (let [es (new js/EventSource "/api/top/messages")]
    (.addEventListener
      es "message"
      (fn [ev]
        (let [received-msg (read-string (.-data ev))
              current-time (.getTime (js/Date.))]
        (swap! app-state
               #(update-in %
                           [:received-msgs]
                           (fn [coll x] (take 20 (cons x coll)))
                           {:ts current-time :msg received-msg}))))))
  (r/render-component [teste] section))
