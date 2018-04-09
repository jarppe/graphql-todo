(ns app.gql.schema
  (:require [com.walmartlabs.lacinia :as gql]
            [com.walmartlabs.lacinia.schema :as gql.schema]
            [com.walmartlabs.lacinia.util :as gql.util]
            [app.gql.resolvers :as resolvers]))

(def schema-data
  {:objects '{:ToDo {:fields {:_id {:type ID}
                              :date {:type :String}
                              :user {:type :User
                                     :resolve :ToDo/user}
                              :tags {:type (list String)}
                              :message {:type String}}}
              :User {:fields {:_id {:type ID}
                              :name {:type String}
                              :email {:type String}
                              :gravatar {:type String}
                              :todos {:type (list :ToDo)
                                      :args {:tags {:type (list String)}
                                             :from {:type String}
                                             :to {:type String}}
                                      :resolve :User/todos}}}}

   :queries '{:todos {:type (list :ToDo)
                      :args {:_id {:type ID}
                             :userId {:type ID}
                             :tags {:type (list String)}
                             :from {:type String}
                             :to {:type String}}
                      :resolve :ToDo/todos}

              :users {:type (list :User)
                      :args {:_id {:type ID}
                             :name {:type String}
                             :email {:type String}}
                      :resolve :User/users}}

   :mutations '{:addUser {:type :User
                          :args {:name {:type (non-null String)}
                                 :email {:type (non-null String)}}
                          :resolve :User/add-user}
                :addToDo {:type :ToDo
                          :args {:userId {:type (non-null ID)}
                                 :tags {:type (list String)}
                                 :message {:type (non-null String)}}
                          :resolve :ToDo/add-todo}}})

(def schema (-> schema-data
                (gql.util/attach-resolvers resolvers/resolvers)
                (gql.schema/compile)))

(comment

  (gql/execute schema
               "query { todos(limit: 10) { message } }"
               nil
               nil)

  )