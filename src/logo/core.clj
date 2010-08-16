(ns logo.core
  (:use [clojure.contrib.logging]))

(defn parsing-success? [result]
  (:success result))

(defn parsing-failure? [result]
  (not (parsing-success? result)))

(defn success [result]
  {:success true :result result})

(defn failure [result]
  {:success false :result result})

(defn parsing-result [result]
  (:result result))

(defn parser
  ([f tag]
     (with-meta  (fn [i] (let [result (f i)]
                           (if (parsing-failure? result)
                             (do (log :debug (str "parser " tag " could not parse input '" i "'"))
                                 result)
                             result)))
       {:parser-tag (str tag)}))
  ([f] (parser f (str "anonymous parser " f))))

(defn satisfy
  "builds elementary parsers for terminal symbols"
  ([bf tag] (parser (fn [i] (if (bf (first i)) (success [(first i) (rest i)]) (failure [nil i]))) tag))
  ([bf] (satisfy bf (str "anonymous satisfy " bf))))

(defn either
  "choice combinator"
  ([pa pb parser-tag] (parser (fn [i] (let [oa (pa i)]
                                        (if (parsing-failure? oa) (pb i) oa)))
                              parser-tag))
  ([pa pb] (either pa pb (str "either " (:parser-tag (meta pa)) " | " (:parser-tag (meta pb))))))


(defn- run-many
  ([i o f]
     (if (empty? i) (success [o nil])
         (let [result (f i)]
           (if (parsing-success? result)
             (let [[op r] (parsing-result result)]
               (if (nil? op) (success [o i]) (recur r (conj o op) f)))
             (success [o i]))))))

(defn many
  "Runs a parser as many times as possible"
  ([p parser-tag] (parser (fn [i] (run-many i [] p))) )
  ([p] (many p (str "many " (:parser-tag p)))))

(defn many-1
  "Runs a parser that must consume at least one input"
  ([p parser-tag] (parser (fn [i] (let [result ((many p) i)]
                                    (if (parsing-failure? result) result
                                        (let [parsed (first (parsing-result result))]
                                          (if (or (nil? parsed) (empty? parsed))
                                            (failure [nil i])
                                            result)))))
                          parser-tag))
  ([p] (many-1 p (str "many-1 " (:parser-tag p)))))

(defn maybe
  "Runs a parser 0 or 1 times"
  ([p parser-tag] (parser (fn [i] (let [result (p i)]
                                    (if (parsing-success? result)
                                      result
                                      (success [nil i]))))
                          parser-tag))
  ([p] (maybe p (str "maybe " (:parser-tag (meta p))))))

(defmacro sequential-inner [input orig-input bindings & body]
  (if (empty? bindings)
    `(success [(do ~@body) ~input])
    (let [[output-binding parser] (first bindings)
          rest-bindings (rest bindings)]
      `(let [output# (~parser ~input)]
         (if (parsing-success? output#)
           (do (let [~output-binding (first (parsing-result output#))]
                 (sequential-inner (second (parsing-result output#)) ~orig-input ~rest-bindings ~@body)))
           (do  (failure [nil ~orig-input])))))))

(defmacro sequential [bindings & body]
  (let [bindings-part (partition 2 bindings)]
    `(parser (fn [input#] (sequential-inner input# input# ~bindings-part ~@body))
             "sequential")))

(defn return
  "Returns a ne token type"
  ([token-type token-content]
     {:token token-type :content token-content})
  ([token-content] (return :generic token-content)))

;; utilities

(defn parse
  ([parser input] (parser input)))

(defn result-conj
  ([col d]
     (if (string? col)
       (str col d)
       (if (or (nil? col) (coll? col))
         (conj (vec col) d)
         (conj [col] d)))))

(defn result-cons
  ([col d]
     (if (string? col) (str d col)
         (if (or (nil? col) (coll? col))
           (vec (cons d (vec col)))
           (vec (conj d [col]))))))

;; Simple parsers

(defn empty-word
  ([] (satisfy #(if (nil? %1)
                  true
                  (if (coll? %1)
                    (empty? %1)
                    false))
               "empty word")))

(defn character
  "Checks if the input matches the char"
  ([c] (satisfy #(= c %1) (str "character " c))))

(defn digit
  "Checks if the input is a number 0-9"
  ([] (satisfy #(Character/isDigit %1) "digit")))

(defn letter
  "Checks if the input is a letter"
  ([] (satisfy #(Character/isLetter %1) "letter")))

(defn space
  "Checks if the input is a blank character"
  ([] (satisfy #(Character/isSpaceChar %1) "space")))

(defn matches
  "Checks if the character matches the provied regular expression"
  ([pattern] (satisfy  #(re-matches pattern (str %1)) (str "matches '" pattern  "'"))))

(defn word
  "Consumes a word understood as a sequence of letters"
  ([] (parser (fn [i] ((either (empty-word)
                               (sequential [c (letter)
                                            w (maybe (word))]
                                           (return :word (apply str (result-cons (:content w) c))))) i))))
  ([w] (parser (fn [i]
                 (let [orig i]
                   (loop [should-continue true
                          ai i
                          l w]
                     (if should-continue
                       (let [result ((satisfy #(= (first l) %1)) ai)]
                         (if (parsing-success? result)
                           (if (empty? (rest l))
                             (recur false result nil)
                             (recur true (rest ai) (rest l)))
                           result))
                       (if (parsing-success? ai)
                         (let [result (parsing-result ai)]
                           (success [{:token :word :content (vec w)} (second result)]))
                         (failure [nil orig])))))))))

(defn number
  "Consumes a number understood as a sequence of digits"
  ([] (parser (fn [i] (let [result ((many-1 (digit)) i)]
                        (if (parsing-success? result)
                          (let [content-number (first (parsing-result result))
                                number (Integer/parseInt (apply str content-number))]
                            (success [{:token :number :content number} (second (parsing-result result))]))
                          result))))))

;; LOGO supported functions

(def *logo-colors* {0  :black
                    1  :blue
                    2  :green
                    3  :cyan
                    4  :red
                    5  :magenta
                    6  :yellow
                    7  :white
                    8  :brown
                    9  :tan
                    10 :forest
                    11 :aqua
                    12 :salmon
                    13 :purple
                    14 :orange
                    15 :grey})

(def *logo-procedures-en*
     {"forward"     :forward
      "fd"          :forward
      "back"        :back
      "bk"          :back
      "left"        :left
      "lt"          :left
      "right"       :right
      "rt"          :right
      "setxy"       :setxy
      "home"        :home
      "arc"         :arc
      "showturtle"  :showturtle
      "st"          :showturtle
      "hideturtle"  :hideturtle
      "ht"          :hideturtle
      "clearscreen" :clearscreen
      "pendown"     :pendown
      "pd"          :pendown
      "penup"       :penup
      "pu"          :penup
      "setpencolor" :setpencolor
      "setpc"       :setpencolor
      "repeat"      :repeat
      })

(def *logo-procedures-en-reverse*
     {:forward     "forward"
      :fd          "fd"
      :back        "back"
      :bk          "bk"
      :left        "left"
      :lt          "lt"
      :right       "right"
      :rt          "rt"
      :setxy       "setxy"
      :home        "home"
      :arc         "arc"
      :showturtle  "showturtle"
      :st          "st"
      :hideturtle  "hideturtle"
      :ht          "ht"
      :clearscreen "clearscreen"
      :pendown     "pendown"
      :pd          "pd"
      :penup       "penup"
      :pu          "pu"
      :setpencolor "setpencolor"
      :setpc       "setpc"
      :repeat      "repeat"
      })

(def *logo-procedures-es*
     {"avanza"         :forward
      "av"             :forward
      "retrocede"      :back
      "re"             :back
      "giraizquierda"  :left
      "gi"             :left
      "giraderecha"    :right
      "gd"             :right
      "pontortuga"     :setxy
      "inicio"         :home
      "arco"           :arc
      "muestratortuga" :showturtle
      "mt"             :showturtle
      "ocultatortuga"  :hideturtle
      "ot"             :hideturtle
      "limpiapantalla" :clearscreen
      "bajapincel"     :pendown
      "bp"             :pendown
      "subepincel"     :penup
      "sp"             :penup
      "poncolor"       :setpencolor
      "pc"             :setpencolor
      "repite"         :repeat
      })

(def *logo-procedures-es-reverse*
     {:forward     "avanza"
      :fd          "av"
      :back        "retrocede"
      :bk          "re"
      :left        "giraizquierda"
      :lt          "gd"
      :right       "giraderecha"
      :rt          "gd"
      :setxy       "pontortuga"
      :home        "inicio"
      :arc         "arco"
      :showturtle  "muestratortuga"
      :st          "mt"
      :hideturtle  "ocultatortuga"
      :ht          "ot"
      :clearscreen "limpiapantalla"
      :pendown     "bajapincel"
      :pd          "bp"
      :penup       "subepincel"
      :pu          "sp"
      :setpencolor "poncolor"
      :setpc       "pc"
      :repeat      "repeat"
      })

(defn key-for-val ([val map] (first (first (filter #(= (second %1) val) map)))))

(defn fn-symbol-for [lang word]
  (let [procedure
        (condp = lang
          "es" (get *logo-procedures-es* word)
          "en" (get *logo-procedures-en* word)
          (throw (Exception. "not supported language")))]
    (if (nil? procedure) (throw (Exception. "unknown function")) procedure)))

(defn fn-name-for [lang kw]
  (let [name (condp = lang
               "es" (get *logo-procedures-es-reverse* kw)
               "en" (get *logo-procedures-en-reverse* kw)
               (throw (Exception. "not supported language")))]
    (if (nil? name) (throw (Exception. (str "unbounded function identifier" name))) name)))

;; a LOGO parser

(defn logo-word ([] (either (word) (number))))

(defn new-line ([] (satisfy #(= \newline %1))))

(defn token-separator ([] (either (space) (new-line))))

(declare sentence)

(defn logo-list-item ([lang] (fn [i]
                               (parse (either
                                       (sequential [s (sentence lang)
                                                    _ (many (token-separator))]
                                                   (return :list-item s))
                                       (sequential [w (logo-word)
                                                    _ (many (token-separator))]
                                                   (return :list-item w))) i))))

(defn logo-arithmetic-operator ([] (matches #"[+-/*%]{1,1}")))

(defn arithmetic-expression ([] (sequential [va (logo-word)
                                             _  (many (token-separator))
                                             op (logo-arithmetic-operator)
                                             _  (many (token-separator))
                                             vb (logo-word)]
                                            (return :arithmetic-operation {:operation (str op) :arguments [va vb]}))))

(defn assignation-operator ([] (word ":=")))

(defn assignation ([] (either (sequential [lvalue (word)
                                           _      (many (token-separator))
                                           _      (assignation-operator)
                                           _      (many (token-separator))
                                           rvalue (arithmetic-expression)
                                           _      (many (token-separator))]
                                          (return :assignation {:lvalue lvalue :rvalue rvalue}))
                              (sequential [lvalue (word)
                                           _      (many (token-separator))
                                           _      (assignation-operator)
                                           _      (many (token-separator))
                                           rvalue (logo-word)
                                           _      (many (token-separator))]
                                          (return :assignation {:lvalue lvalue :rvalue rvalue})))))


(defn logo-list ([lang] (sequential [_ (character \[)
                                     _ (many (token-separator))
                                     args (many (logo-list-item lang))
                                     _ (many (token-separator))
                                     _ (character \])
                                     _ (many (token-separator))]
                                    (return :list args))))

(defn logo-word-not-fn
  ([lang] (parser
           (fn [i] (let [result (parse (logo-word) i)]
                     (if (parsing-success? result)
                       (if (number? (:content (first (parsing-result result))))
                         result
                         (let [content (apply str (:content (first (parsing-result result))))]
                           (try (do (fn-symbol-for lang content)
                                    (failure [nil i]))
                                (catch Exception ex result))))
                       result))))))

(defn arguments ([lang] (either (sequential [word (logo-word-not-fn lang)
                                             _ (many (token-separator))]
                                            (return (:token word) (:content word)))
                            (logo-list lang))))


(defn fn-call
  ([lang] (parser (fn [i] (let [result (parse (word) i)]
                            (if (parsing-success? result)
                              (try
                               (let [fn-symbol (fn-symbol-for lang (apply str (:content (first (parsing-result result)))))]
                                 (if (or (= fn-symbol :forward)
                                         (= fn-symbol :back)
                                         (= fn-symbol :left)
                                         (= fn-symbol :right)
                                         (= fn-symbol :setpencolor))
                                   (success [{:fn-name fn-symbol} (second (parsing-result result))])
                                   (failure [nil i])))
                               (catch Exception ex (failure [nil i])))))))))

(defn two-args-fn-call
  ([lang] (parser (fn [i] (let [result (parse (word) i)]
                            (if (parsing-success? result)
                              (try
                               (let [fn-symbol (fn-symbol-for lang (apply str (:content (first (parsing-result result)))))]
                                 (if (or (= fn-symbol :arc)
                                         (= fn-symbol :repeat))
                                   (success [{:fn-name fn-symbol} (second (parsing-result result))])
                                   (failure [nil i])))
                               (catch Exception ex (failure [nil i])))))))))

(defn no-arguments-fn-call
  ([lang] (parser (fn [i] (let [result (parse (word) i)]
                            (if (parsing-success? result)
                              (try
                               (let [fn-symbol (fn-symbol-for lang (apply str (:content (first (parsing-result result)))))]
                                 (if (or (= fn-symbol :home)
                                         (= fn-symbol :showturtle)
                                         (= fn-symbol :hideturtle)
                                         (= fn-symbol :clearscreen)
                                         (= fn-symbol :pendown)
                                         (= fn-symbol :penup))
                                   (success [{:fn-name fn-symbol} (second (parsing-result result))])
                                   (failure [nil i])))
                               (catch Exception ex (failure [nil i])))))))))


(defn anything-but-new-line
  ([] (parser (fn [i]
                (loop [ai i
                       ao []]
                  (if (or (empty? ai)
                          (nil? ai)) (success [ao []])
                      (if (= (first ai) \newline)
                        (success [ao (rest ai)])
                        (recur (rest ai) (cons (first ai) ao)))))))))

(defn logo-comment ([] (sequential [_ (character \;)
                                    text (anything-but-new-line)]
                                   (return :comment (apply str text)))))

(defn sentence ([lang] (either
                        (sequential [c (logo-comment)
                                     _ (many (token-separator))]
                                    c)
                        (either
                         (assignation)
                        (either
                         (sequential [fc (no-arguments-fn-call lang)
                                      _  (many (token-separator))]
                                     (return :sentence {:function-call fc :args []}))
                        (either
                         (sequential [fc (fn-call lang)
                                      _ (many (token-separator))
                                      args (arguments lang)]
                                     (return :sentence {:function-call fc :args [args]}))
                         (sequential [fc (two-args-fn-call lang)
                                      _ (many (token-separator))
                                      arg1 (arguments lang)
                                      arg2 (arguments lang)]
                                     (return :sentence {:function-call fc :args [arg1 arg2]}))))))))

(defn logo-program ([lang] (many (sentence lang))))
