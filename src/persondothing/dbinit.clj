(ns persondothing.dbinit
  (:require [next.jdbc :as jdbc]))

(def db-spec {:dbtype "h2" :dbname "./my-db"})

(defn create-tables []
  (jdbc/execute! db-spec ["CREATE TABLE IF NOT EXISTS locations 
                          (id bigint primary key auto_increment, 
                          theuser varchar(255),
                          thetext varchar(1024)
                          )"]))

(defn -main [& args]
  (create-tables)
  (println "Database initialized."))

