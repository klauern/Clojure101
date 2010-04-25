(comment
Sample clojure source file
)
(ns klauer.rubylearning.clj101
    (:gen-class))

(defn -main
    ([greetee]
  (println (str "Hello " greetee "!")))
  ([] (-main "world")))
