;; Map =================================================================
;;
;;  translated from the Java version by Nicolas Buduroi

;;  more functional version by Takahiro Hozumi


;; fuctional model

(defn create-area []
  {:width 0
   :height 0
   :walls {}
   :type :area})

(defn point [x y] [x y])

(defn create-player-point
  ([] (point 0 0))
  ([x y] (point x y)))

(defn reset-game [players area]
  (dosync (ref-set (players 0) (create-player-point))
	  (ref-set (players 1) (create-player-point))
	  ;(ref-set players [player1 player2])
	  (ref-set area (create-area))))

(def test-player1 (ref nil))
(def test-player2 (ref nil))
(def test-players (ref [test-player1 test-player2]))
(def test-area (ref nil))

(reset-game test-players test-area)

(defn init-area [w h area]
  (dosync
   (alter area assoc :width w,
		     :height h,
		     :walls {})))

(defn wall? [x y {w :width, h :height, walls :walls}]
  (if (or (< x 0) (< y 0)
          (>= x w) (>= y h))
    true
    (walls (point x y))))

(defn set-wall [x y area]
  (dosync (alter area assoc :walls (assoc (@area :walls)
					 (point x y) true))))

(defn set-player [player x y]
  (dosync (ref-set player (create-player-point x y))))

(def directions
     {:north 1
      :east  2
      :south 3
      :west  4})

(defn exit [n] (System/exit n))

(defn error [& message]
  (.println *err* (apply str "FATAL ERROR: " message))
  (.flush *err*)
  (exit 1))



(defn check-game-over [input]
  (when (re-seq #"^(|exit)$" input)
    (exit 1)))

(defn check-first-line [input]
  (when (= 2 (count input))
    (error "The first line of input should be two integers separated "
           "by a space. Instead, got: " input)))

(defn parse-int [s]
  (try (Integer/parseInt s)
       (catch NumberFormatException e
         (error "Can't parse: " s))))

(defn check-line-length [x y area]
  (when-not (= x (area :width))
    (error "Invalid line length: " x " at line " y " should be "
	   (area :width))))

(defn check-player-found [players-found key]
  (when (players-found key)
    (error "Found two locations for player " key " in the map!")))

(defn check-player-missing [players-found key]
  (when-not (players-found key)
    (error "Did not find a location for player " key "!")))

(defn check-spaces-read [n area]
  (when-not (= n (* (area :width) (area :height)))
    (error "Wrong number of spaces in the map.")))

(defn my-read-line
  "Reads the next line from stream that is the current value of *in* ."
  ([]
     (if (instance? clojure.lang.LineNumberingPushbackReader *in*)
       (.readLine #^clojure.lang.LineNumberingPushbackReader *in*)
       (.readLine #^java.io.BufferedReader *in*)))
  ([input]
     (if (instance? clojure.lang.LineNumberingPushbackReader input)
       (.readLine #^clojure.lang.LineNumberingPushbackReader input)
       (.readLine #^java.io.BufferedReader input))))

(defn initialize [input players area]
  (letfn [(parse-first-line
	   []
	   (when-let [line (my-read-line input)]
	     (let [first-line (.trim line)
		   [w h] (map parse-int (seq (.split first-line " ")))]
	       (check-game-over first-line)
	       (check-first-line first-line)
	       (init-area w h area))))]
    (parse-first-line)
    (loop [x 0 y 0
	   players-found {}
	   number-of-spaces 0]
      (if (>= y (@area :height))
	(do (check-spaces-read number-of-spaces @area)
	    (check-player-missing players-found \1)
	    (check-player-missing players-found \2))
	(let [c (.read input)]
	  (condp = (char c)
	    \newline (do (check-line-length x y @area)
			 (recur 0 (inc y) players-found number-of-spaces))
	    \return  (recur x y players-found number-of-spaces)
	    \space   (recur (inc x) y players-found (inc number-of-spaces))
	    \#       (do (set-wall x y area)
			 (recur (inc x) y players-found (inc number-of-spaces)))
	    \1       (do (check-player-found players-found \1)
			 (set-player (@players 0) x y)
			 (recur (inc x) y (assoc players-found \1 true) (inc number-of-spaces)))
	    \2       (do (check-player-found players-found \2)
			 (set-player (@players 1) x y)
			 (recur (inc x) y (assoc players-found \2 true) (inc number-of-spaces)))
	    (if (< c 0)
	      (exit 0)
	      (error "Invalid character received: " (char c)))))))))

(defn make-move [direction]
  (println (directions direction))
  (flush))

(def in (java.io.BufferedReader. 
	       (java.io.FileReader. "maps/empty-room.txt")))