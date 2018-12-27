(ns ishorty.core
  (:require
    [ishorty.last-msgs-section-app :as last-msgs-app]))

;; enable cljs to print to the JS console of the browser
(enable-console-print!)

(when-let [section (.getElementById js/document "last-msgs-section")]
  (last-msgs-app/init section))
