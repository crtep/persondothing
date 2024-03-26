(ns persondothing.views
  (:require [hiccup.page :as page]
            [hiccup.core :as h]
            [persondothing.db :as db]
            [persondothing.clients :refer :all]
            [persondothing.pdt :as pdt]
            [org.httpkit.server :refer [send!]]
            [ring.util.anti-forgery :as util]))

(defn gen-page-head
  [title]
  [:head
   [:title "Person Say Thing"]
   [:script 
    "let socket = new WebSocket(\"ws://crtep.com:8000/ws\");
    socket.onmessage = function(event) {
        document.getElementById(\"chat-area\").innerHTML = event.data;
    }"]
   [:meta {:name "viewport" :content "initial-scale=1.0, maximum-scale=1.0, user-scalable=no"}]
   ;(page/include-css "/css/gruvbox-dark.css")
   (page/include-css "/css/gruvbox-light.css")
   (page/include-css "/css/styles.css")
   ])

(def header-links
  [:div#header-links
   "[ "
   [:a {:href "/clear"} "clear chat log" ]
   " ]"
   [:hr]
   (for [word pdt/pdt-list]
     [:span word " "])
   ])

(defn input-area
  ([]
   [:div [:p "person: " [:input {:type "text" :name "user" :autofocus true}]]
     
     [:input {:type "hidden" :name "text" :value ":)"}]
     ])
  ([username]
   [:div [:input {:type "hidden" :name "user" :value username}]
    [:input {:type "text"
             :name "text" 
             :style "width: 100%;"
             :placeholder (format "%s say:" username)
             :autofocus true}]
    ])
  ([username text]
   [:div [:input {:type "hidden" :name "user" :value username}]
    [:p "no good!"]
    [:input {:type "text"
             :name "text" 
             :style "width: 100%;"
             :value text 
             :autofocus true
             :onfocus "this.select()"}]
    ]))

(defn chat-body []
  (let [all-texts (db/get-all-texts)]
    (for [text all-texts]
      [:p [:strong (:LOCATIONS/THEUSER text) " say: " ] (:LOCATIONS/THETEXT text) ])))

(defn html-chat-body []
  (str (h/html (chat-body))))

(defn chat-page-contents
  [& args]
  (page/html5
    (gen-page-head "Person Say Thing")
    header-links
    [:h1 "Person Say Thing"]
    [:div#chat-area (chat-body)]
    [:hr]
    [:form {:action "/chat" :method "POST" :autocomplete "off"}
     (util/anti-forgery-field) ; prevents cross-site scripting attacks
     (apply input-area args)
     ]
    ))

(defn notify-clients []
  (doseq [ch @clients_]
    (send! ch {:status 200 :headers {"Content-Type" "text/html"}
               :body (html-chat-body)}
      ;; false ; Uncomment to use chunk encoding for HTTP streaming
      )))

(defn chat-page
  ([] (chat-page-contents))
  ([{:keys [user text]}]
   (do
     (if (pdt/valid text)
       (do
         (db/add-text-to-db user text)
         (notify-clients)
         (chat-page-contents user))
       (chat-page-contents user text)))))

(defn clear-chat-log [] (do  (db/clear-db)(notify-clients)))



