
(ns wk1.exercises
  ;(:require )
  ;(:use )
  ;(:import )
  )


; W1E1:	Syntax
; Why does the first one raise an exception and what does the error message mean?
; user=> (1 2 3)
; java.lang.ClassCastException: java.lang.Integer cannot be cast to clojure.lang.IFn (NO_SOURCE_FILE:0)
; user=> '(1 2 3)
; (1 2 3)

; In the first form, what is being attempted is an execution of the expression (1 2 3),
;; However, there is no method defined or referenced, so Clojure cannot determine what type
; of call to make (is it an if statement, a macro, a method?).  It knows 1 is an Integer,
; but cannot convert that to any of it's special forms or types.

; In the second form, the precedeing ' indicates that we want to take the literal
; of the entire expression () and return that as a variable (so to speak, I can't recall the
; Clojure equivalent of a literal or value).

; *** W1E2: Binding
; Look at the following Clojure code and try to guess the value of "y" inside the 'let'
;  and 'binding' forms. Once you are done use the REPL to verify your answers.
(def x 1)								; Global?
(def y 1)								; Global?
(let [x 5 y x] y)				; Local?, y = 5
(binding [x 5	y	x] y)		; Redefines global of x = 5, and y = x, outside of (binding)


; W1E3: Binding
; Looking at the following snippet, can you predict the output of both binding forms?
; Use the REPL to check your answers.
(defn method-one [& args] (apply println "Inside method #1 =>" args))
(defn method-two [& args] (apply println "Inside method #2 =>" args))

(binding [method-one method-two]		; binding is local, so method-one is redefined as
	(method-one 1 2 3 ))							;   method-two and will invoke method-two

((binding [method-one method-two]		; binding is thread-local, but the anonymous function
	 #(method-one 1 2 3)))						;  is another thread, so we call method-one
																		;  instead of the bound method-two.


; W1E4: Create a Function
; Write a function called nth-fib which calculates the nth Fibonacci number using the
; following formula:
;   Fib(n) = round(Phi^n / sqrt(5))
; where Phi is defined as
;   Phi = (1 + sqrt(5)) / 2.
; For simplicity's sake you can ignore n < 0.
;
; To see if your function works, compare it to the following REPL transcript:
; user=> (map nth-fib [10 20 30 40 50 60 70])
; (55 6765 832040 102334155 12586269025 1548008755920 190392490709135)
(def phi (/ (+ 1 (Math/sqrt 5)) 2))
(defn fib [n]
	(Math/round (/ (Math/pow phi n) (Math/sqrt 5))))

; W1E5: Destructuring
; For this exercise, you will be modifying the file 'book_list.clj'.
; This is a simple Clojure program that prints the top ten Amazon best-selling books on 16 March 2010. 
; Do not worry about any forms you do not understand, you will only be modifying the print-book function.

; In order to use the program from the REPL, make sure the 'book_list.clj' is in your classpath.
; From the REPL, you can execute the following to load the module and see the results:
; user=> (use 'book-list)
; nil
; user=> (map print-book best-sellers)
; (Title: The Big Short
;   Author:  Michael Lewis
;   Price: $15.09
; ...
; If you choose to edit the file externally, it can be reloaded using the following command:
; user=> (require :reload 'book-list)
; nil

; 1.1 Destructuring a map

; Note how the author, title, and price are accessed by using the book map as a function.
;   This function would be more concise if the book argument were destructured into
;   corresponding symbols. Rewrite print-book as follows with the appropriate destructuring.

; (defn print-book
;   "Prints out information about a book."
;   [ {var-name :key-name, ... } ]                ; <- add destructuring here
;   (println "Title:" title)
;   (println "  Author: " (comma-sep authors))
;   (println "  Price:" (money-str price)))

;(defn print-book
;  "Prints out information about a book."
;  [{book :title book :authors book :price}]
;  (println "Title:" title)
;  (println "  Author: " (comma-sep authors))
;  (println "  Price:" (money-str price)))


; 1.2 Destructuring a map using :keys

; Now refine the print-book function to use the ':keys' method to destructure the map.

;(defn print-book
;  "Prints out information about a book."
;  [{:keys [title authors price] }]
;  (println "Title:" title)
;  (println "  Author: " (comma-sep authors))
;  (println "  Price:" (money-str price)))

; 2.1 Destructuring a vector

; The requirements have changed, and now any book with three or more authors needs to be
;   printed out as 'First Author, Second Author, et. al.' Modify print-book as follows
;   and destructure the authors vector.

; (defn print-book
;   "Prints out information about a book."
;   [{:keys ... }]                                ; <- contains destructuring from 1.2
;   (println "Title:" title)
;   (let [... authors]                            ; <- add destructuring of authors here
;     (println "  Author:" (comma-sep
;                            (filter seq [first
;                                         second
;                                         (when more "et. al.")]))))
;   (println "  Price:" (money-str price)))

;(defn print-book
;  "Prints out information about a book."
;  [{:keys [title authors price] }]
;  (println "Title:" title)
;	(let [[first second & more] authors]
;	 (println "  Author: " (comma-sep
;													(filter seq [first
;																			 second
;																			(when more "et. al.")]))))
;  (println "  Price:" (money-str price)))

; 2.2 Destructuring with :as

; The requirements change yet again! Now the print-book function must output a Clojure
;   reader compatible representation of the book map as 'Raw'. Use ':as' in your
;   destructuring to capture the entire function argument as 'book' in the revised
;   function below:

; (defn print-book
;   "Prints out information about a book."
;   [{:keys ... }]                                ; <- contains destructuring from 1.2
;   (println "Title:" title)
;   (let [... authors]                            ; <- contains destructuring from 2.1
;     (println "  Author:" (comma-sep
;                            (filter seq [first
;                                         second
;                                         (when more "et. al.")]))))
;   (println "  Price:" (money-str price))
;   (println "  Raw:" (pr-str book)))