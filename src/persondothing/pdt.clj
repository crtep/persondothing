(ns persondothing.pdt
  (:require
    [clojure.string :as str]
    ))

(def pdt-list
  ["person" "place" "thing" "do" "eat" "feel" "get" "go" "have" "like" "make" "say"
   "see" "think" "use" "want" "big" "far" "fast" "good" "hot" "many" "other" "real"
   "same" "after" "before" "again" "and" "but" "in" "on" "with" "more" "yes" "no"])

(def pdt-set (set pdt-list))

(defn valid [string]
  (every? (complement nil?) 
        (map pdt-set 
             (map str/lower-case 
                  (str/split string #"[^a-zA-Z\d]+")))))

(comment
  (def example "This is an example text. Person   want place, thing person123 do.")
  (valid "Person want place: thing person do.")
  )
