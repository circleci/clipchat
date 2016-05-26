(ns clipchat.users
  (:require [clj-http.client :as client]
            [clipchat.core :refer [setup-call-body api-url]]))

(defn create [auth-token {:keys [email name title is_group_admin password timezone] :as opts} & [hostname]]
  (let [{:keys [is_group_admin password timezone]
         :or {is_group_admin 0 timezone "UTC" password ""}} opts
        url (str (api-url hostname) "/users/create")
        body (-> (assoc opts :auth_token auth-token :format "json")
                client/generate-query-string
                setup-call-body)]
    (client/post url body)))

(defn update [auth-token {:keys [user_id email name title is_group_admin password timezone] :as opts} & [hostname]]
  (let [{:keys [is_group_admin timezone]
         :or {is_group_admin 0 timezone "UTC"}} opts
        url (str (api-url hostname) "/users/update")
        body (-> (assoc opts :auth_token auth-token :format "json")
                 client/generate-query-string
                 setup-call-body)]
    (client/post url body)))

(defn delete [auth-token {:keys [user_id] :as opts} & [hostname]]
  (let [url (str (api-url hostname) "/users/delete")
        body (-> (assoc opts :auth_token auth-token)
                 client/generate-query-string
                 setup-call-body)]
    (client/post url body)))

(defn list [auth-token & [hostname]]
  (let [url (str (api-url hostname) "/users/list")
        query {:format "json" :auth_token auth-token}]
    (client/get url {:query-params query})))

(defn show [auth-token {:keys [user_id] :as opts} & [hostname]]
  (let [url (str (api-url hostname) "/users/list")
        query (assoc opts :auth_token auth-token)]
    (client/get url {:query-params query})))



