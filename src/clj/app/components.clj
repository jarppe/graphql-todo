(ns app.components
  (:require [integrant.core :as ig]
            [potpuri.core :as p]
            [syksy.core :as syksy]
            [syksy.web.index :as index]
            [syksy.web.resource :as resource]
            [syksy.web.resources :as resources]
            [app.routes :as routes]
            [app.db.core :as db]))

(defn components []
  (p/deep-merge
    (syksy/default-components {:index-body (index/index {:title "GraphQL - TODO"})
                               :ctx {:db (ig/ref ::db/db)}
                               :routes routes/handler
                               :addon-handlers [(ig/ref [::resource/handler ::graphql])
                                                (ig/ref [::resources/handler ::graphql])]})
    {[::resource/handler ::graphql] {:match? {"/graphiql" "graphiql/index.html"
                                              "/graphiql/" "graphiql/index.html"
                                              "/favicon.ico" "public/favicon.ico"}}
     [::resources/handler ::graphql] {:asset-prefix "/graphiql/"
                                      :asset-dir "graphiql/"}
     ::db/conn {}
     ::db/db {:conn (ig/ref ::db/conn)
              :db-name "todo"}}))
