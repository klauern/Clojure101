
(ns wk2.exercises
  ;(:require )
  ;(:use )
  ;(:import )
  )


(def cities [{:city "Vienna" :temp 55} {:city "Krakow" :temp 52} {:city "Pune" :temp 85} {:city "Houston" :temp 57}])

; 1. Write a program that outputs the average temperature of the above cities rounded to 2 decimal places.

; (Hint: have a look at the documentation for the 'map' and 'reduce' forms, they may come in handy for this exercise.)
(def summation (reduce + (map :temp cities)))

(defn avg [] 
	(/ summation
	 (count cities)))

(def cities-temp-avg
	(float (avg)))				; coerce the average 249/4 to a float, 62.25


; 2. Since two of the four cities are in Europe, also add a function to convert temperatures between Fahrenheit to Celsius.
(defn f-to-c [f-temp]
	(*											; 1. subtract 32 from temp
	 (/											; 2. divide by 9
		(- f-temp 32) 9) 5))		; 3. multiply by 5.


; W2E3: (Intro to reference types) -
; Intro to reference types: With the new constructs introduced this week, it should be possible
;   for us to build on last week's destructuring exercise and turn its code into a reasonable first
;   approximation to an application one might use for managing one's personal library. (A few bits
;   will remain for weeks 3 & 4, but in doing the next several exercises, you should definitely
;   start seeing the general shape of the app come together!)

; Exercise goals:
;   Upon completion of this exercise, you should have a body of code which should allow you to store a
;   collection of books in memory, add and remove books from said collection and query the collection
;   for books matching simple criteria (e.g. a books-by-author query should be doable).

; Basic design: The first order of business is to put the collection of books in memory somewhere while
;   making it possible to manipulate it (add and remove books, amend book data etc.). This involves at
;   least two design choices:
;    - The choice of the correct reference type to use.
;    - The choice of the correct type of collection to hold the book data.
;   Note that while last week's exercise used a vector of StructMaps, that vector was meant to hold a
;     best-sellers list, which has an intrinsic order. Your personal book collection might be different
;     in this regard. So, give some consideration to the choice of collection type.

; See book-list.clj in wk2 for the solution set.

; In addition to this, consider the basic StructMap used for storing information on books. If you feel
;   that a slightly different structure would be more appropriate for a personal library app, feel free to
;   provide a definition of one. Do keep in mind, though, that StructMaps can have additional keys `conj`ed
;   onto them, so if you think there is some type of information which you will likely only want to attach
;   to a limited number of books, it is probably a good idea not to include it in the book StructMap itself.
;   As a baseline book StructMap, let us use the one from last week minus the `:price` key (it might be
;   difficult to attach a price to every book in one's personal library!):
;
;     (defstruct book :title :authors)

; Functional abstraction: Having prepared an initial design for the foundations of the library management
;   app, provide definitions for functions to be called as below:

; (add-book book)
     ; OR
;     (add-book book-coll book)

;     (remove-book book)
     ; OR
;     (remove-book book-coll book)
; Note that the calls which omit the book-coll argument should probably use some default value. Support one
;   of the two argument signatures or both; in either case, be prepared to provide a short discussion of
;   your choice. What designs can you think of to support both kinds of calls with as little code
;   duplication as possible?



;; W2E4: (Brainteaser Coordination) - Suppose that you have two Refs:

; (def x (ref 1))
;     (def y (ref 1))
; Depending on some circumstance or other, you might want to increase the value of either one; however,
;   the sum is never to exceed 3. Here's a function you might use in a transaction to see whether it is
;   ok to increase one of them:
;
; (defn ok-to-inc? [x y]
;       (< (+ x y) 3))
;
; And here is a piece of code which will exercise the above:

; (do
;
;       (future
;        (dosync
;         (let [xv @x
;               yv @y]
;           ;; let's take a 1 second nap...
;           (Thread/sleep 1000)
;           (if (ok-to-inc? xv yv)
;             (alter x inc)))))

;       (future
;        (dosync
;         (let [xv @x
;               yv @y]
;           (Thread/sleep 1000)
;           (if (ok-to-inc? xv yv)
;             (alter y inc))))))
; Do you see the problem with this code? How would you go about correcting it?

;; W2E7: (Transactions) - Our personal library app is looking good. We could already use it to store a
;   catalogue of books (albeit only in memory, for now); in this exercise we shall take it upwards on
;   the usefulness scale by implementing loan-tracking functionality. Let us never again wonder whether
;   that book which seems nowhere to be found might have been borrowed by someone!

; Exercise goals: Upon completion of this exercise, you should be able to use the library app to store
;   information on books lent to friends (and possibly also on those you yourself have borrowed, if you
;   care to add that functionality). The code should be robust enough easily to scale to what an actual
;   library might use, with the potential to record multiple lend/return transactions in parallel.

; Implementation: There are two basic approaches one could take to the task at hand:

; 1. keep all book data in a single reference (meaning an object of some appropriate reference type);
; 2. split the collection of books in two parts: one for the books currently available in-house and one
;    for those which have been lent to someone and not yet returned.
; With the second design we would have to use Refs, while with the first design both Refs and Atoms could
;   conceivably be used. (Do you see why that is?) Pick one approach, define your Atom / Ref(s), then
;   define the functions to be called as follows:

; (add-loan person item)
;
;     (accept-return person item)
;
; `add-loan` should accept optional keyword arguments:
;
;     (add-loan person item :return-by some-date)

; Feel free to handle other types of information you might find useful. (An actual library would likely want
;   to count 'renewals' etc.) The suggested approach to storing loan information for this exercise is to:
; (assoc item :loan-data {:person "..." ...})
; The `item` argument to `add-loan` and `accept-return` should be a map containing enough information to
;   identify the object being loaned (a primary key); for our purposes the title together with the list of
;   authors (`{:title "..." :authors [...]}`) will be acceptable.

; Would data integrity be preserved if your functions were being called simultaneously by multiple threads?
;   (For instance, is there any possibility of a book being added to the 'on loan' collection while not being 
;   removed from the 'in house' collection?)