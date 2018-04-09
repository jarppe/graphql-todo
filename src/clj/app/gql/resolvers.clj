(ns app.gql.resolvers
  (:require [monger.collection :as mc]
            [monger.operators :refer :all]))

(defn find-todos [{:keys [db]} {:keys [_id userId tags from to]} _]
  (mc/find-maps db "todos"
                (cond-> {}
                        _id (assoc :_id _id)
                        userId (assoc :user userId)
                        tags (assoc :tags {$all tags})
                        from (assoc :date {$gte from})
                        to (assoc :date {$lte to})
                        (and from to) (assoc :date {$gte from, $lte to}))))

(defn find-todo-user [{:keys [db]} _ {:keys [user]}]
  (mc/find-by-id db "users" user))

(defn find-users [{:keys [db]} {:keys [_id name email]} value]
  (mc/find-maps db "users"
                (cond-> {}
                        _id (assoc :_id _id)
                        email (assoc :email email)
                        name (assoc :name {$regex (str ".*" name ".*") $options "i"}))))

(defn find-user-todos [{:keys [db]} {:keys [tags from to] :as args} {user-id :_id :as value}]
  (println "find-user-todos:" (pr-str args) (pr-str value))
  (mc/find-maps db "todos" (cond-> {:user user-id}
                                   tags (assoc :tags {$all tags})
                                   from (assoc :date {$gte from})
                                   to (assoc :date {$lte to})
                                   (and from to) (assoc :date {$gte from, $lte to}))))

(def resolvers {:ToDo/todos find-todos
                :ToDo/user find-todo-user
                :User/users find-users
                :User/todos find-user-todos})
