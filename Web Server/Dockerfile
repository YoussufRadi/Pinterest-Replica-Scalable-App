FROM maven:3.3-jdk-8
COPY pom.xml /tmp/pom.xml
RUN mvn -B -f /tmp/pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve

#FROM alpine/git
#WORKDIR /app

#FROM maven:3.5-jdk-8-alpine
#WORKDIR /app
#COPY  . /app
#RUN mvn -B -f /app/pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
#RUN mvn install

COPY ./target/classes/ /app
WORKDIR /app
ENTRYPOINT ["java","Server.HTTPServer"]
