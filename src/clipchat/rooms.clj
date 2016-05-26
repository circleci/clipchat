(ns clipchat.rooms
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]
            [clipchat.core :refer [setup-call-body api-url]]))

(defn list-rooms [auth-token  & [hostname]]
  "Returns a vector of maps defining the rooms available.
  https://www.hipchat.com/docs/api/method/rooms/list
  'list' means something in clojure, so the name is 'list-rooms' instead."
  (let [url (str (api-url hostname) "/rooms/list")
        query {:format "json" :auth_token auth-token}]
    (:rooms
     (json/read-str
      (:body
       (client/get url {:query-params query}))
      :key-fn keyword))))

(defn history [auth-token {:keys [room_id date] :as opts} & [hostname]]
  "Returns the history of messages to the specified room as a vector of maps.
  https://www.hipchat.com/docs/api/method/rooms/history"
  (let [url (str (api-url hostname) "/rooms/history")
        query (assoc opts :auth_token auth-token :format "json")]
    (:messages
     (json/read-str
      (:body
       (client/get url {:query-params query}))
      :key-fn keyword))))


(defn message [auth-token {:keys [room_id from message notify color] :as opts} & [hostname]]
  "Sends a notification message to the specified room. If a custom hostname is given
  then it will be used as the api url, otherwise https://api.hipchat.com will be used."
  (doall
   (map #(if (nil? (get opts %))
          (throw (java.lang.Exception. (str "Missing argument: " %))))
        [:room_id :from :message]))
  (let [{:keys [notify color] :or {notify 0 color "yellow"}} opts
        url (str (api-url hostname) "/rooms/message")
        body (-> (assoc opts :auth_token auth-token :format "json")
                 client/generate-query-string
                 setup-call-body)]
    (:status
     (json/read-str
      (:body
       (client/post url body))
      :key-fn keyword))))

(defn show [auth-token {:keys [room_id] :as opts} & [hostname]]
  (let [url (str (api-url hostname) "/rooms/show")
        query (assoc opts :auth_token auth-token :format "json")]
    (:room
     (json/read-str
      (:body
       (client/get url {:query-params query}))
      :key-fn keyword))))

(defn create [auth-token {:keys [name owner_user_id privacy topic guest_access] :as opts} & [hostname]]
  (let [url (str (api-url hostname) "/rooms/create")
        body (-> {:auth_token auth-token
                  :format "json"
                  :guest_access 0
                  :topic ""
                  :privacy "public"}
                 (merge opts)
                 client/generate-query-string
                 setup-call-body)]
    (:room
     (json/read-str
      (:body
       (client/post url body))
      :key-fn keyword))))

(defn delete [auth-token {:keys [room_id] :as opts} & [hostname]]
  (let [url (str (api-url hostname) "/rooms/delete")
        body (-> (assoc opts :auth_token auth-token :format "json")
                 client/generate-query-string
                 setup-call-body)]
    (:deleted
     (json/read-str
      (:body
       (client/post url body))
      :key-fn keyword))))
