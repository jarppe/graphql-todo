(ns app.gql.resolvers
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

(def resolvers {:ToDo/todos find-todos
                :ToDo/user find-todo-user
                :ToDo/add-todo add-todo

                :User/users find-users
                :User/todos find-user-todos
                :User/add-user add-user})

(comment

  (def db (-> integrant.repl.state/system :app.db.core/db))

  (mc/find-one-as-map db "users" {:_id "5acbbea91d0456eb3e707c28"})
  (mc/find-map-by-id db "users" "5acbbea91d0456eb3e707c28")
  )