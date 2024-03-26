(ns persondothing.core
  (:require 
            [persondothing.views :as views] ; add this require
            [ring.adapter.jetty :as jetty]
            [ring.util.response :refer [redirect]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)])
  (:gen-class))

(defn ok [html]
  {:status 200
   :body html
   :headers {"Content-Type" "text/html"}})

(defn app-routes [request]
  (let [uri (:uri request)
        method (:request-method request)]
    (views/chat-page)
    (cond
      (and (= uri "/") (= method :get)) (ok (views/chat-page))
      (and (= uri "/chat") (= method :get)) (ok (views/chat-page))
      (and (= uri "/clear") (= method :get)) (do (views/clear-chat-log)
                                                 (redirect "/chat"))
      (and (= uri "/chat") (= method :post)) (let [params (get request :params)]
                                               (ok (views/chat-page params)))
      :else (views/chat-page))))


(def app
  (wrap-defaults #'app-routes site-defaults))

(defn -main []
  (jetty/run-jetty #'app {:port 8000}))

(comment
  ;; evaluate this def form to start the webapp via the REPL:
  ;; :join? false runs the web server in the background!
  (def server (jetty/run-jetty #'app {:port 8000 :join? false}))
  ;; evaluate this form to stop the webapp via the the REPL:
  (.stop server)
  )
