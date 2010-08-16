(defproject parser "1.0.0-SNAPSHOT"
  :description "A LOGO interpreter written in Clojure and being executed in the HTML canvas"
  :dependencies [[org.clojure/clojure "1.2.0-RC3"]
                 [org.clojure/clojure-contrib "1.2.0-RC3"]
                 [compojure "0.4.1"]
                 [hiccup "0.2.6"]
                 [ring/ring-jetty-adapter "0.2.5"]
                 [appengine "0.4-SNAPSHOT"]]
  :dev-dependencies [[leiningen/lein-swank "1.2.0-SNAPSHOT"]])
