(ns ishorty.core
  (:require
    [ishorty.top-urls-section-app :as top-urls-app]))

;; enable cljs to print to the JS console of the browser
(enable-console-print!)

(when-let [section (.getElementById js/document "top-urls-section")]
  (top-urls-app/init section))
