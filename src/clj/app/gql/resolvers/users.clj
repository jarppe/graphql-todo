(ns app.gql.resolvers.users
  (:require [monger.collection :as mc]
            [monger.operators :refer :all]
            [app.db.util :as u]))

(defn find-users [{:keys [db]} {:keys [_id name email]} value]
  (mc/find-maps db "users"
                (cond-> {}
                        _id (assoc :_id _id)
                        email (assoc :email email)
                        name (assoc :name {$regex (str ".*" name ".*") $options "i"}))))

(defn find-user-todos [{:keys [db]} {:keys [tags from to]} {user-id :_id}]
  (mc/find-maps db "todos" (cond-> {:user user-id}
                                   tags (assoc :tags {$all tags})
                                   from (assoc :date {$gte from})
                                   to (assoc :date {$lte to})
                                   (and from to) (assoc :date {$gte from, $lte to}))))

(defn add-user [{:keys [db]} {:keys [name email]} _]
  (let [user {:_id (u/new-object-id)
              :name name
              :email email}]
    (mc/insert db "users" user)
    user))

(def resolvers {:User/users find-users
                :User/todos find-user-todos
                :User/add-user add-user})
