FROM sgrio/java-oracle:server_jre_8
RUN apt-get update
RUN apt-get install -y maven
COPY pom.xml /usr/local/service/pom.xml
COPY src /usr/local/service/src
WORKDIR /usr/local/service
RUN mvn package
#CMD ["java","-cp","target/Pinterest-1.0-SNAPSHOT-jar-with-dependencies.jar","RunBackEnd", "controller"]
#CMD ["java","-cp","target/Pinterest-1.0-SNAPSHOT-jar-with-dependencies.jar","RunBackEnd", "client", "post"]