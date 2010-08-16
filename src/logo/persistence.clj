(ns logo.persistence
  (:use [appengine.datastore]))

; Define some entities.
(defentity Sketch ()
  ((id :key clojure.contrib.string/lower-case)
   (lang)
   (date)
   (code)
   (title)
   (author)))


; Initialize the environment for the repl.
;(appengine.environment/init-repl)

;; ; Make a continent record.
;; (def *europe* (continent {:iso-3166-alpha-2 "eu" :name "Europe" :location {:latitude 1 :longitude 2}}))

;; ; Make a country record (a country must have a continent as it's parent).
;; (def *germany* (country *europe* {:iso-3166-alpha-2 "de" :name "Germany" :location {:latitude 1 :longitude 2}}))

;; ; Find a contient (returns nil, because the continent has not been saved yet).
;; (find-entity *europe*)

;; ; Save the continent to the datastore.
;; (save-entity *europe*)

;; ; Find all continents.
;; (find-continents "Europe")

;; ; Find all continents by name.
;; (find-continents-by-name "Europe")

;; ; Delete the entity from the datastore.
;; (delete-entity *europe*)
