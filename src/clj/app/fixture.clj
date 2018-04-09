(ns app.fixture
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :as resp]
            [schema.core :as s]
            [monger.collection :as mc]))

(defn clean-db [db]
  (mc/remove db "todos")
  (mc/remove db "users"))

(defn simple-fixture [db]
  (log/warn "applying simple-fixture")
  (clean-db db)
  (mc/insert-batch db "todos" [{:_id "todo1"
                                :date "2018-04-09T12:34:56.123"
                                :user "user1"
                                :tags ["tag1" "tag2" "tag3"]
                                :message "Jiihaa"}
                               {:_id "todo2"
                                :date "2018-04-10T12:34:56.123"
                                :user "user1"
                                :tags ["tag2" "tag3"]
                                :message "Foozaa"}
                               {:_id "todo3"
                                :date "2018-04-11T12:34:56.123"
                                :user "user2"
                                :tags ["tag2" "tag3" "tag4"]
                                :message "BaBaBa"}])
  (mc/insert-batch db "users" [{:_id "user1"
                                :email "a@a.a"
                                :todos ["todo1" "todo2"]}
                               {:_id "user2"
                                :email "b@b.b"
                                :todos ["todo3"]}]))

(def fixtures {:simple simple-fixture})

(defn apply-fixture [request]
  (if-let [fixture (-> request :parameters :path :fixture-id keyword fixtures)]
    (do (fixture (-> request :ctx :db))
        (resp/ok "fixture applied"))
    (resp/not-found)))

(def routes
  [["/fixture/:fixture-id" {:name :fixture
                            :post {:handler apply-fixture
                                   :parameters {:path {:fixture-id s/Keyword}}
                                   :responses {200 {:body s/Str}}}}]])
