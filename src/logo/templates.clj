(ns logo.templates
  (:use [hiccup.core]
        [logo.translations]))

(defn show-sketch-template [sketch]
  [:html
   [:head
    [:title (:title sketch)]
    [:script {:type "text/javascript" :src "/static/js/jquery.js"}]
    [:script {:type "text/javascript" :src "/static/js/interpreter.js"}]
    [:link {:type "text/css" :rel "stylesheet" :media "screen" :href "/static/css/interpreter.css"}]]
   [:body
    [:div {:id "sketch-lang" :style "display:none"} (:lang sketch)]
    [:div {:id "sketch-heading"}
     [:div {:id "show-sketch-title"} (:title sketch)]
     [:div {:id "show-sketch-subtitle"}
      [:span {:id "show-sketch-author"} (:author sketch)]
      [:span {:id "show-sketch-date"} (str (:date sketch))]]]
    [:div {:id "canvas-container"}
     [:div {:id "spinner"}
      [:img {:src "/static/images/spinner.gif"}]]
     [:canvas {:id "canvas" :width "600" :height "400" :style "display: none"} [:div {:id "fallback"} (translate :canvas-warning "en")]]

     [:div {:id "show-sketch-code"}
      [:pre
       (:code sketch)]]
     [:div {:id "edit-more"} [:a {:href "/"} "edit a new LOGO sketch"]]]
    [:div {:id "about-show"} "Antonio Garrote, 2010"]]])



(defn index-template [lang latest-sketches]
  [:html
   [:head
    [:title (translate :logo-web-interpreter lang)]
    [:script {:type "text/javascript" :src "/static/js/jquery.js"}]
    [:script {:type "text/javascript" :src "/static/js/interpreter.js"}]
    [:link {:type "text/css" :rel "stylesheet" :media "screen" :href "/static/css/interpreter.css"}]]
   [:body
    [:div {:id "change-language"} (translate :change-language lang) [:a {:href (translate :alternate-lang lang)} (translate :alternate-lang-name lang)]]
    [:div {:id "heading"} (translate :heading lang)]
    [:div {:id "canvas-container"}
     [:canvas {:id "canvas" :width "600" :height "400"} [:div {:id "fallback"} (translate :canvas-warning lang)]]]
    [:div {:id "repl"}
     [:form {:action "/repl" :method "post" :id "logo-sentence-form"}
      [:input {:type "hidden" :id "logo-language" :value (translate :lang-value-input lang)}]
      [:b
       [:label {:for "logo-sentence"} (translate :type-some-logo-here lang)]]
      [:br]
      [:p
       [:textarea {:id "logo-sentence" :name "logo-sentence" :rows "10" :cols "50"} (translate :sample-logo lang)]]
      [:p [:input {:type "submit" :value (translate :evaluate lang) :id "evaluate"}]]]
     [:div {:id "reference"}
      [:table
       [:thead [:th [:b (translate :procedure lang) ]] [:th [:b (translate :alias lang)]] [:th [:b (translate :arguments lang)]] [:th [:b (translate :example lang)]]]
       [:tbody
        [:tr [:td (translate :repeat lang)] [:td ""] [:td (translate :num-iterations-list-sentences lang)] [:td (translate :repeat-example lang)]]
        [:tr [:td (translate :forward lang)] [:td (translate :fd lang)] [:td (translate :num-pixels lang)] [:td (translate :forward-50 lang)]]
        [:tr [:td (translate :back lang)] [:td (translate :bk lang)] [:td (translate :num-pixels lang)] [:td (translate :back-50 lang)]]
        [:tr [:td (translate :left lang)] [:td (translate :lt lang)] [:td (translate :degrees lang)] [:td (translate :left-90 lang)]]
        [:tr [:td (translate :right lang)] [:td (translate :rt lang)] [:td (translate :degrees lang)] [:td (translate :right-180 lang)]]
        [:tr [:td (translate :arc lang)] [:td ""] [:td (translate :radius-degrees lang)] [:td (translate :arc-20-270 lang)]]
;        [:tr [:td (translate :setxy lang)] [:td ""] [:td (translate :position-position lang)] [:td (translate :setxy-300-200 lang)]]
;        [:tr [:td (translate :home lang)] [:td ""] [:td ""] [:td (translate :home lang)]]
;        [:tr [:td (translate :showturtle lang)] [:td (translate :st lang)] [:td ""] [:td (translate :showturtle lang)]]
;        [:tr [:td (translate :hideturtle lang)] [:td (translate :ht lang)] [:td ""] [:td (translate :hideturtle lang)]]
        [:tr [:td (translate :clearscreen lang)] [:td ""] [:td ""] [:td (translate :clearscreen lang)]]
        [:tr [:td (translate :pendown lang)] [:td (translate :pd lang)] [:td ""] [:td (translate :pendown lang)]]
        [:tr [:td (translate :penup lang)] [:td (translate :pu lang)] [:td ""] [:td (translate :penup lang)]]
        [:tr [:td (translate :setpencolor lang)] [:td (translate :setpc lang)] [:td (translate :color-number lang)] [:td (translate :setpencolor-2 lang)]]
        ]]]]

    [:div {:id "mid-part"}
     [:div {:id "save-form"}
      [:form {:action "/sketch" :method "post" :id "save-sketch"}
       [:b (translate :save-logo-session lang)]
       [:br]
       [:br]
       [:br]
       [:b
        [:label {:for "sketch-title"} (translate :sketch-title lang)]]
       [:br]
       [:p [:input {:type "text" :name "sketch-title" :id "sketch-title"}]]

       [:b
        [:label {:for "sketch-author"} (translate :sketch-author lang)]]
       [:br]
       [:p [:input {:type "text" :name "sketch-author" :id "sketch-author"}]]

       [:b
        [:label {:for "sketch-code"} (translate :sketch-code lang)]]
       [:br]
       [:p [:textarea {:id "sketch-code" :name "sketch-code" :rows "20" :cols "50"}]]

       [:input {:type "hidden" :id "sketch-lang" :name "sketch-lang" :value lang}]

       [:p [:input {:type "submit" :value (translate :save lang) :id "save"}]]
       [:p [:input {:type "submit" :value (translate :test-session lang) :id "test-session"}]]
       ]]
     [:div {:id "latest-sketches"}
      [:b (translate :latest-sketches lang)]
      (map (fn [sk] [:div {:class "sketch-link"}
                     [:a {:href (str "/sketch/" (:id sk))}
                      [:span {:class "sketch-title"} (:title sk)]]
                     (translate :by lang)
                     [:span {:class "sketch-author"} (:author sk)]])
           latest-sketches)]
     [:div {:id "github"} [:b (translate :github lang)]]]
    [:div {:id "about"}
     "Antonio Garrote, 2010"]]])
