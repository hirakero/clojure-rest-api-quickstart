(ns minimal-api.core
  (:gen-class)
  (:require
   [integrant.core :as ig]
   [ring.adapter.jetty :as jetty]
   [ring.util.response :as response]))

;;
(defn hello-world [request]
  (response/response "hello world"))

(defmethod ig/init-key ::app [_ _]
  hello-world)

;;
(defmethod ig/init-key ::server [_ {:keys [app options]}]
  (jetty/run-jetty app options))

(defmethod ig/halt-key! ::server [_ server]
  (.stop server))

;;
(def config
  {::app {}
   ::server {:app (ig/ref ::app)
             :options {:port 3000
                       :join? false}}})

;;
(defn -main [& args]
  (ig/init config))
