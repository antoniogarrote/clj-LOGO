(ns logo.translations)

(defn translate [res lang]
  (condp = res
    :logo-web-interpreter (condp = lang
                            "es" "interprete web de LOGO"
                            "en" "LOGO web interpreter"
                            "LOGO web interpreter")
    :change-language (condp = lang
                       "es" "cambiar idioma &nbsp;"
                       "en" "change language &nbsp;")
    :alternate-lang (condp = lang
                      "es" "/?lang=en"
                      "en" "/?lang=es"
                      "/?lang=es")
    :alternate-lang-name (condp = lang
                           "es" "ingl&eacute;s"
                           "en" "Spanish"
                           "Spanish")
    :canvas-warning (condp = lang
                      "es" "Su navegador no sporta el canvas HTML que es necesario para utilizar esta demo"
                      "en" "Your browser does not support HTML canvas which is required to run this demo"
                      "Your browser does not support HTML canvas which is required to run this demo")
    :lang-value-input (condp = lang
                        "es" "es"
                        "en" "en"
                        "en")
    :type-some-logo-here (condp = lang
                           "es" "Escribe un poco de LOGO aqu&iacute; &nbsp;"
                           "en" "Type some LOGO here  &nbsp;"
                           "Type some LOGO here  &nbsp;")
    :sample-logo (condp = lang
                   "es" "a := 1\nrepite 100 [ \n  c := a % 16\n  pc c\n  avanza a\n  giraderecha 40\n  a := a + 1 ; incrementamos un poco el valor\n]"
                   "en" "a := 1\nrepeat 100 [ \n  c := a % 16\n  setpc c\n  forward a\n  right 40\n  a := a + 1 ; we increase the value slightly\n]"
                   "a := 1\nrepeat 100 [ \n  c := a % 16\n  setpc c\n  forward a\n  right 40\n  a := a + 1 ; we increase the value slightly\n]")
    :evaluate (condp = lang
                "es" "evaluar"
                "en" "evaluate"
                "evaluate")
    :logo-reference (condp = lang
                      "es" "referencia de LOGO"
                      "en" "LOGO reference"
                      "LOGO reference")
    :procedure (condp = lang
                 "es" "funci&oacute;n"
                 "en" "function"
                 "function")
    :alias (condp = lang
             "es" "alias"
             "en" "alias"
             "alias")
    :arguments (condp = lang
                 "es" "argumentos"
                 "en" "arguments"
                 "arguments")
    :example (condp = lang
               "es" "ejemplo"
               "en" "example"
               "example")
    :repeat (condp = lang
              "es" "repite"
              "en" "repeat"
              "repeat")
    :num-iterations-list-sentences (condp = lang
                                     "es" "num. de iteraciones, lista de sentencias"
                                     "en" "num. of iterations, list of sentences"
                                     "num. of iterations, list of sentences")
    :repeat-example (condp = lang
                      "es" "repite 10 [av 30 gd 10]"
                      "en" "repeat 10 [fd 30 rt 10]"
                      "repeat 10 [fd 30 rt 10]")
    :forward (condp = lang
               "es" "avanza"
               "en" "forward"
               "forward")
    :fd (condp = lang
          "es" "av"
          "en" "fd"
          "fd")
    :num-pixels (condp = lang
                  "es" "number of pixels"
                  "en" "number of pixels"
                  "number of pixels")
    :forward-50 (condp = lang
                  "es" "avanza 50"
                  "en" "forward 50"
                  "forward 50")
    :back (condp = lang
            "es" "retrocede"
            "en" "back"
            "bk")
    :bk (condp = lang
          "es" "re"
          "en" "bk"
          "bk")
    :back-50 (condp = lang
               "es" "retrocede 50"
               "en" "back 50"
               "back 50")
    :left (condp = lang
            "es" "giraizquierda"
            "en" "left"
            "left")
    :lt (condp = lang
          "es" "gi"
          "en" "lt"
          "lt")
    :left-90 (condp = lang
               "es" "giraizquierda 90"
               "en" "left 90"
               "left 90")
    :right (condp = lang
             "es" "giraderecha"
             "en" "right"
             "right")
    :arc (condp = lang
           "es" "arco"
           "en" "arc"
           "arc")
    :radius-degrees (condp = lang
                      "es" "radio, grados"
                      "en" "radius, degrees"
                      "radius, degrees")
    :arc-20-270 (condp = lang
                  "es" "arco 20 270"
                  "en" "arc 20 270"
                  "arc 20 270")
    :clearscreen (condp = lang
                   "es" "limpiapantalla"
                   "en" "clearscreen"
                   "clearscreen")
    :setpencolor (condp = lang
                   "es" "poncolor"
                   "en" "setpencolor"
                   "setpencolor")
    :setpc (condp = lang
             "es" "pc"
             "en" "setpc"
             "setpc")
    :setpencolor-2 (condp = lang
                     "es" "poncolor 2"
                     "en" "setpencolor 2"
                     "setpencolor 2")
    :degrees (condp = lang
               "es" "grados"
               "en" "degrees"
               "degrees")
    :right-180 (condp = lang
                 "es" "giraderecha 180"
                 "en" "right 180"
                 "right 180")
    :rn (condp = lang
          "es" "gd"
          "en" "rt"
          "rt")
    :heading (condp = lang
               "es" "un int&eacute;rprete de LOGO escrito con Clojure y el elemento canvas de HTML"
               "en" "a LOGO interperter built with Clojure and the canvas element"
               "a LOGO interperter built with Clojure and the HTML canvas element")

    :color-number (condp = lang
                    "es" "c&oacute;digo de color"
                    "en" "color number"
                    "color number")

    :rt (condp = lang
          "es" "gd"
          "en" "rt"
          "rt")

    :pendown (condp = lang
               "es" "bajapincel"
               "en" "pendown"
               "pendown")

    :penup (condp = lang
             "es" "subepincel"
             "en" "penup"
             "penup")

    :pd (condp = lang
          "es" "bp"
          "en" "pd"
          "pd")

    :pu (condp = lang
          "es" "sp"
          "en" "pu"
          "pu")

    :save-logo-session (condp = lang
                         "es" "Guarda la sesi&oacute;n LOGO como un dibujo"
                         "en" "Save this LOGO session as a sketch"
                         "Save this LOGO session as a sketch")

    :test-session (condp = lang
                    "es" "probar"
                    "en" "test"
                    "test")

    :sketch-title (condp = lang
                    "es" "t&iacute;tulo"
                    "en" "title"
                    "title")

    :sketch-author (condp = lang
                     "es" "autor"
                     "en" "author"
                     "author")

    :sketch-code (condp = lang
                   "es" "c&ocaute;digo"
                   "en" "code"
                   "code")

    :save (condp = lang
            "es" "guardar"
            "en" "save"
            "save")

   :by (condp = lang
         "es" "&nbsp; por &nbsp;"
         "en" "&nbsp; by &nbsp;"
         "&nbsp; by &nbsp;")

   :latest-sketches (condp = lang
         "es" "&Uacute;ltimos dibujos"
         "en" "Latest sketches"
         "Latest sketches")

   :github (condp = lang
             "es" "C&oacute;digo disponible en <a href='http://antoniogarrote.github.com/clj-logo'>Github</a>"
             "en" "Source code available at <a href='http://antoniogarrote.github.com/clj-logo'>Github</a>"
             "Source code available at <a href='http://antoniogarrote.github.com/clj-logo'>Github</a>")

;   : (condp = lang
;         "es" ""
;         "en" ""
;         "")

    (str res)))
