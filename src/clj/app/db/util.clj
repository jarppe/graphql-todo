(ns app.db.util
  (:require [monger.collection :as mc])
  (:import org.bson.types.ObjectId))

(defn new-object-id []
  (str (ObjectId/get)))

(defn create [db coll]
  (when-not (mc/exists? db coll)
    (mc/create db coll {}))
  db)

(defn id->_id [m]
  (when m
    (-> m
        (assoc :_id (:id m))
        (dissoc :id))))

(defn _id->id [m]
  (when m
    (-> m
        (assoc :id (:_id m))
        (dissoc :_id))))
