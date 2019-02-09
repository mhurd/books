FROM mhurd/build-lein:1.0
WORKDIR /root
USER root
RUN /bin/sh -c 'git clone https://github.com/mhurd/books.git'
WORKDIR /root/books
RUN /bin/sh -c '/usr/lib/jvm/java-8-openjdk-amd64/bin/java -client -XX:+TieredCompilation -Xbootclasspath/a:/root/.lein/self-installs/leiningen-2.8.3-standalone.jar -Dfile.encoding=UTF-8 -Dmaven.wagon.http.ssl.easy=false -Dleiningen.original.pwd=/root/books -cp /root/.lein/self-installs/leiningen-2.8.3-standalone.jar clojure.main -m leiningen.core.main uberjar'

FROM openjdk:8u191-jdk-alpine3.9
WORKDIR /bin/books
COPY --from=0 /root/books/target/books-0.1.0-SNAPSHOT-standalone.jar /bin/books/books-0.1.0-SNAPSHOT-standalone.jar
RUN /bin/sh -c 'addgroup -S books'
RUN /bin/sh -c 'adduser -S books -G books'
RUN /bin/sh -c 'chown -R books:books /bin/books'
USER books
WORKDIR /bin/books
RUN /bin/sh -c 'ls -alrt /bin/books'
CMD ["java -jar /bin/books/books-0.1.0-SNAPSHOT-standalone.jar"]  

