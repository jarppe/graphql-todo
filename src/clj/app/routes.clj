(ns app.routes
  (:require [reitit.ring :as ring]
            [reitit.coercion.schema]
            [reitit.ring.coercion :as rrc]
            [syksy.util.mode :as mode]
            [app.gql.routes :as gql]
            [app.fixture :as fixture]))

(def handler
  (ring/ring-handler
    (ring/router
      (concat gql/routes
              (when (mode/dev-mode?) fixture/routes))
      {:data {:coercion reitit.coercion.schema/coercion
              :middleware [rrc/coerce-exceptions-middleware
                           rrc/coerce-request-middleware
                           rrc/coerce-response-middleware]}})))
