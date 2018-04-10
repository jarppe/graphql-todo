(ns app.gql.resolvers.todos
  (:require [monger.collection :as mc]
            [monger.operators :refer :all]
            [app.db.util :as u]))

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
  (mc/find-map-by-id db "users" user))

(defn add-todo [{:keys [db]} {:keys [userId tags message]} _]
  (let [todo {:_id (u/new-object-id)
              :user userId
              :tags tags
              :message message}]
    (mc/insert db "todos" todo)
    todo))

(def resolvers {:ToDo/todos find-todos
                :ToDo/user find-todo-user
                :ToDo/add-todo add-todo})
