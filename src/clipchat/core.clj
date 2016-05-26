(ns clipchat.core)

(def default-api-url "https://api.hipchat.com/v1")
(def urlencoded "application/x-www-form-urlencoded")

(defn setup-call-body [body]
  {:content-type urlencoded
   :accept "json"
   :body body})

(defn api-url [hostname]
  (if (seq hostname)
    (str hostname "/v1")
    default-api-url))
