(ns app.db.core
  (:require [clojure.tools.logging :as log]
            [integrant.core :as ig]
            [monger.core :as mg]
            [monger.collection :as mc]
            [app.db.util :as u]))

;;
;; DB initialization:
;;

(defn init-db! [db]
  (doto db
    (u/create "todos")
    (u/create "users")
    (mc/ensure-index "todos" (array-map :date 1) {:name "todos-date"})
    (mc/ensure-index "users" (array-map :email 1) {:name "users-email"
                                                   :unique true})))

;;
;; DB connection pool:
;;

(def default-config {:host "localhost"
                     :port 27017})

(defmethod ig/init-key ::conn [_ {:keys [config]}]
  (log/info "connecting to MongoDB...")
  (mg/connect (merge default-config config)))

(defmethod ig/halt-key! ::conn [_ conn]
  (log/info "disconnecting from MongoDB...")
  (mg/disconnect conn))

(defmethod ig/init-key ::db [_ {:keys [conn db-name]}]
  (log/info "getting MongoDB database" db-name "...")
  (doto (mg/get-db conn db-name)
    (init-db!)))

;;
;; Nothing to see here, move along...
;;

(comment

  (def db (-> integrant.repl.state/system :app.db.core/db))
  (require '[monger.operators :refer [$gte $lte]])

  (let [_id nil
        userId "user1"
        tags nil
        from "2018-04-09T10:34:56.123"
        to "2018-04-12T10:34:56.123"]
    (mc/find-maps db "todos" (cond-> {}
                                     _id (assoc :_id _id)
                                     userId (assoc :user userId)
                                     tags (assoc :tags tags)
                                     from (assoc :date {$gte from})
                                     to (assoc :date {$lte to})
                                     (and from to) (assoc :date {$gte from, $lte to}))))

  (mc/count db "todos")
  (mc/find-maps db "todos" {:user "user2"})

  (mc/insert db "foo" {:a1 ["a" "b" "c"]})
  (mc/insert db "foo" {:a1 ["c" "d" "e"]})
  (mc/find-maps db "foo" {:a1 {"$all" ["c" "a"]}})

  )


