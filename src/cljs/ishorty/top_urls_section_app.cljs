(ns ishorty.top-urls-section-app
  (:require [reagent.core :as r]
            [cljs.reader :refer (read-string)]))

(def app-state (r/atom {}))

(defn teste []
  (fn []
    (let [state @app-state
          top-urls (-> state :top-urls :top-long-urls)]
      [:div
       [:table
        [:thead
         [:tr
          [:th "Top 10 Urls"]]]
        [:tbody
         (for [url top-urls]
           ^{:key (keyword "index" url)}
           [:tr
            [:td
             [:a {:href url} url]]])]]])))

(defn init [section]
  (let [es (new js/EventSource "/api/top/messages")]
    (.addEventListener
      es "message"
      (fn [ev]
        (swap! app-state #(assoc % :top-urls (read-string (.-data ev)))))))
  (r/render-component [teste] section))
