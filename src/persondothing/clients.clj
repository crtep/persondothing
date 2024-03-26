(ns persondothing.clients
  (:require 
            [org.httpkit.server :refer :all]
))

(def clients_ (atom #{}))
