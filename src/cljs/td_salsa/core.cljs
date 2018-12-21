(ns td-salsa.core
  (:require
   [reagent.core :as r]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(defonce app-state
  (r/atom {:click-count 0 :todo [] :text ""}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Page components
(defn counting-component [ratom]
  (let [click-count (:click-count @app-state)]
    [:div
     "The atom " [:code "click-count"] " has value: " click-count ". "
     [:input {:type "button" :value "Click me!"
              :on-click #(swap! app-state
                                (fn [a] (assoc a :click-count
                                               (inc (:click-count a)))))}]]))
(defn save []
  "")

(defn clone-component [ratom]
    [:div
     [:input {:type "text" :value (:text @app-state)}]])

(defn todo-component [ratom]
    [:div
     [:input {:type "text" :value (:text @app-state)
              :on-change #(swap! app-state (fn [a]
                                             (assoc a :text
                                                 (-> % .-target .-value))))
              :on-key-down #(case (.-which %)
                               13 (save)
                               nil)}]])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Page

(defn page [ratom]
  [:div
   "Welcome to reagent-figwheel."
   ;;(counting-component ratom)
   ;;(todo-component ratom)
   ;;(clone-component ratom)
  ]
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")
    ))

(defn reload []
  (r/render [page app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
