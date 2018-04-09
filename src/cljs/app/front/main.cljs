(ns app.front.main
  (:require [rum.core :as rum]
            [citrus.core :as citrus]))

(rum/defc MainView < rum/reactive [r]
  [:div.label "Here we go again..."])

;;
;; Init:
;;

(def reconciler (citrus/reconciler {:state (atom {})}))

(do (js/console.log "init...")
    (citrus/broadcast-sync! reconciler :init)
    (rum/mount (MainView reconciler) (js/document.getElementById "app")))
