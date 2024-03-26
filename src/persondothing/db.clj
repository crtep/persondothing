(ns persondothing.db
  (:require
    [next.jdbc.sql :as sql]
    ))

(def db-spec {:dbtype "h2" :dbname "./my-db"})

(defn add-text-to-db
  [user text]
  (let [results (sql/insert! db-spec :locations {:theuser user :thetext text})]
    (assert (and (map? results) (:LOCATIONS/ID results)))
    results))

(defn get-user-text
  [loc-id]
  (let [results (sql/query db-spec
                           ["select theuser, thetext from locations where id = ?" loc-id])]
    (assert (= (count results) 1))
    (first results)))

(defn get-all-texts
  []
  (sql/query db-spec ["select id, theuser, thetext from locations"]))

(defn clear-db []
  (sql/query db-spec ["delete from locations"]))

(comment
  (get-all-locations)
  ;; => [#:LOCATIONS{:ID 1, :X 8, :Y 9}]
  (get-xy 1)
  ;; => #:LOCATIONS{:X 8, :Y 9}
  )
