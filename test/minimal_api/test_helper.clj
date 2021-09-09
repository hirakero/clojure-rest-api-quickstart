(ns minimal-api.test-helper
  (:require
   [clj-http.client :as client]
   [clojure.java.io :as io]
   [integrant.core :as ig]
   [minimal-api.db :as db]))

(defn test-config []
  (-> (io/resource "config.edn")
      slurp
      ig/read-string
      (assoc-in [:minimal-api-lein.server/server :options :port] 3001)
      (doto ig/load-namespaces)))

(defn test-system []
  (ig/prep (test-config)))

(def test-url-prefix "http://localhost:3001")

;; macros for testing context

(defmacro with-system [[bound-sym binding-expr] & body]
  `(let [~bound-sym (ig/init ~binding-expr)]
     (try
       ~@body
       (finally (ig/halt! ~bound-sym)))))

(defmacro with-db-data [db-data-map & body]
  `(let [old-val# @db/todos]
     (try
       (reset! db/todos ~db-data-map)
       ~@body
       (finally (reset! db/todos old-val#)))))

;; HTTP client

(defn http-get [path]
  (client/get (str test-url-prefix path)
              {:accept :json
               :as :json
               :coerce :always
               :throw-exceptions? false}))

(defn http-post [path body]
  (client/post (str test-url-prefix path)
               {:form-params body
                :content-type :json
                :accept :json
                :as :json
                :coerce :always
                :throw-exceptions? false}))

(defn http-put [path body]
  (client/put (str test-url-prefix path)
               {:form-params body
                :content-type :json
                :accept :json
                :as :json
                :coerce :always
                :throw-exceptions? false}))

(defn http-delete [path]
  (client/delete (str test-url-prefix path)
              {:accept :json
               :as :json
               :coerce :always
               :throw-exceptions? false}))
