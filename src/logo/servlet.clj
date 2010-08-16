(ns logo.servlet
  (:use [compojure core]
        [compojure response]
        [ring.util.response]
        [ring.util.servlet]
        [clojure.contrib.json]
        [logo.core]
        [logo.persistence]
        [logo.templates]
        [clojure.contrib.logging]
        [appengine.datastore]
        [hiccup.core])
  (:gen-class :extends javax.servlet.http.HttpServlet))


(defn find-language-headers
  ([request]
     (let [params-lang (get (:params request) "lang")]
       (if (and (not (nil? params-lang))
                (or (= params-lang "es")
                    (= params-lang "en"))) params-lang
                    (let [lang-header(get (:headers request) "accept-language")]
                      (if (nil? lang-header) "en"
                          (let [lang-header (.toLowerCase lang-header)]
                            (let [result  (aget (.split  (aget (.split lang-header ",") 0) "-") 0)]
                              (if (nil? result) "en"
                                  (if (= result "es") "es" "en"))))))))))

(defn random-uuid []
  (.replace (str (java.util.UUID/randomUUID)) "-" ""))


(defroutes logo-web

  (POST "/evaluate" request (do
                              (try
                               (let [result (parse (logo-program (get  (:form-params request) "logoLanguage"))
                                                   (.toLowerCase (get  (:form-params request) "code")))]
                                 (if (and  (parsing-success? result)
                                           (empty? (second (parsing-result result))))
                                   (let [sentences (filter #(not (= (:token %1) :comment)) (first (parsing-result result)))]
                                     {:status 200
                                      :body (json-str {:result "success"
                                                       :sentences sentences})
                                      :headers {"Content-Type" "application/json"}})
                                   {:status 200
                                    :body (json-str {:result "error"
                                                     :sentences (first (parsing-result result))})}))
                               (catch Exception ex
                                 {:status 200
                                  :body (json-str {:result "error" :sentences (.getMessage ex)})}))))

  (GET "/sketch/:uuid" request (do
                                 (let [*uuid* (get (:params request) "uuid")
                                       *sketches* (select "sketch" (make-key "sketch" *uuid*))]
                                   (if (empty? *sketches*)
                                     (html [:body
                                            [:div [:h1 "We could not found the requested sketch"]]
                                            [:div [:a {:href "/"} "write some LOGO"]]])
                                     (html (show-sketch-template (first *sketches*)))))))

  (POST "/sketch" request (do
                            (let [*uuid* (random-uuid)
                                  *sketch* (sketch {:lang (get (:form-params request) "sketch-lang")
                                                    :id *uuid*
                                                    :date (java.util.Date.)
                                                    :code (get (:form-params request) "sketch-code")
                                                    :title (get (:form-params request) "sketch-title")
                                                    :author (get (:form-params request) "sketch-author")})]
                              (save-entity *sketch*)
                              (redirect (str "/sketch/" *uuid*)))))

  (GET "/" request (let [lang (find-language-headers request)
                         latest-sketches (take 5 (select "sketch" order-by (:date :desc)))]
                     (if (= lang "es")
                       (html (index-template "es" latest-sketches))
                       (html (index-template "en" latest-sketches))))))

(defservice logo-web)
