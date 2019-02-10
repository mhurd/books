# First build does the actual compile
FROM nexus.home:8083/build-jdk8:latest
WORKDIR /root
USER root
RUN /bin/sh -c 'git clone https://github.com/mhurd/books.git'
WORKDIR /root/books
# Required to deploy to jar
ARG MAJOR_VERSION
ARG MINOR_VERSION
# Build the uberjar
RUN /bin/sh -c '/usr/lib/jvm/java-8-openjdk-amd64/bin/java -client -XX:+TieredCompilation -Xbootclasspath/a:/root/.lein/self-installs/leiningen-2.8.3-standalone.jar -Dfile.encoding=UTF-8 -Dmaven.wagon.http.ssl.easy=false -Dleiningen.original.pwd=/root/books -cp /root/.lein/self-installs/leiningen-2.8.3-standalone.jar clojure.main -m leiningen.core.main uberjar'
# Deploy the uberjar
WORKDIR /root/books/target
RUN /bin/sh -c 'mvn --settings /root/settings.xml deploy:deploy-file -DgroupId=com.mhurd -DartifactId=books -Dversion=$MAJOR_VERSION.$MINOR_VERSION -DgeneratePom=true -Dpackaging=jar -DrepositoryId=nexus-releases -Durl="https://nexus.home/repository/maven-releases" -Dfile=`ls books-*-standalone.jar`'
RUN /bin/sh -c 'mv `ls books-*-standalone.jar` books.jar'

# Second build step takes the built jar from the previous image and adds it to a minimal image to be used at runtime
FROM openjdk:8u191-jdk-alpine3.9
WORKDIR /bin/books
COPY --from=0 /root/books/target/books.jar /bin/books/
RUN /bin/sh -c 'addgroup -S books'
RUN /bin/sh -c 'adduser -S books -G books'
RUN /bin/sh -c 'chown -R books:books /bin/books'
USER books
WORKDIR /bin/books
RUN /bin/sh -c 'ls -alrt /bin/books'
ENTRYPOINT ["/usr/bin/java", "-jar", "books.jar"]  

