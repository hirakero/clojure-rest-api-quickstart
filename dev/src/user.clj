(ns user)

(defn dev
  "load and switch to the 'dev namespace"
  []
  (require 'dev)
  (in-ns 'dev)
  :loaded)
