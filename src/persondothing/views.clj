(ns persondothing.views
  (:require [hiccup.page :as page]
            [persondothing.db :as db]
            [persondothing.pdt :as pdt]
            [ring.util.anti-forgery :as util]))

(defn gen-page-head
  [title]
  [:head
   [:title (str "Locations: " title)]
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

(defn chat-page-contents
  [& args]
  (let [all-texts (db/get-all-texts)]
    (page/html5
      (gen-page-head "Chat")
      header-links
      [:h1 "Person Say Thing"]
      (for [text all-texts]
        [:p [:strong (:LOCATIONS/THEUSER text) " say: "] (:LOCATIONS/THETEXT text) ]
        )
      [:hr]
      [:form {:action "/chat" :method "POST" :autocomplete "off"}
       (util/anti-forgery-field) ; prevents cross-site scripting attacks
       (apply input-area args)
       ]
      )))

(defn chat-page
  ([] (chat-page-contents))
  ([{:keys [user text]}]
   (do
     (if (pdt/valid text)
       (do
         (db/add-text-to-db user text)
         (chat-page-contents user))
       (chat-page-contents user text)))))

(defn clear-chat-log [] (db/clear-db))



