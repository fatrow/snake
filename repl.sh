#!/bin/sh
java -cp .:\
/Users/fatrow/opt/swank-clojure/src/main/clojure:\
/Users/fatrow/code/shcloj-code:\
/Users/fatrow/code/shcloj-code/lib/commons-io-1.4.jar:\
/Users/fatrow/code/shcloj-code/lib/commons-fileupload-1.2.1.jar:\
/Users/fatrow/code/shcloj-code/lib/commons-codec-1.3.jar:\
/Users/fatrow/code/shcloj-code/lib/jline-0.9.94.jar:\
/Users/fatrow/code/shcloj-code/lib/clojure.jar:\
/Users/fatrow/code/shcloj-code/lib/clojure-contrib.jar:\
/Users/fatrow/code/shcloj-code/lib/ant.jar:\
/Users/fatrow/code/shcloj-code/lib/ant-launcher.jar:\
/Users/fatrow/code/shcloj-code/lib/compojure.jar:\
/Users/fatrow/code/shcloj-code/lib/hsqldb.jar:\
/Users/fatrow/code/shcloj-code/lib/jetty-6.1.14.jar:\
/Users/fatrow/code/shcloj-code/lib/jetty-util-7.1.14.jar:\
/Users/fatrow/code/shcloj-code/lib/servlet-api-2.5-6.1.14.jar:\
/Users/fatrow/code/shcloj-code/classes \
jline.ConsoleRunner clojure.lang.Repl
